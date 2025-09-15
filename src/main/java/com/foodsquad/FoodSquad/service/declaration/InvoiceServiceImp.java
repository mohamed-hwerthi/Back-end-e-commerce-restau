package com.foodsquad.FoodSquad.service.declaration;

import com.foodsquad.FoodSquad.model.entity.MenuItem;
import com.foodsquad.FoodSquad.model.entity.Order;
import com.foodsquad.FoodSquad.model.entity.Tax;
import com.foodsquad.FoodSquad.model.entity.Timbre;
import com.foodsquad.FoodSquad.repository.OrderRepository;
import com.foodsquad.FoodSquad.repository.TimbreRepository;
import lombok.RequiredArgsConstructor;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor

public class InvoiceServiceImp implements InvoiceService {


    private static final Logger log = LoggerFactory.getLogger(InvoiceServiceImp.class);
    private final OrderRepository orderRepository;
    private final TimbreRepository timbreRepository;
    private final MenuItemService menuItemService;

    @Override
    public byte[] generateInvoice(String orderId) throws Exception {

        String INVOICE_TEMPLATE_PATH = "/jasper/invoice.jrxml";
        InputStream reportStream = getClass().getResourceAsStream(INVOICE_TEMPLATE_PATH);
        JasperReport jasperReport = JasperCompileManager.compileReport(reportStream);

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        if (order.getMenuItemsWithQuantity() != null) {
            for (Map.Entry<MenuItem, Integer> entry : order.getMenuItemsWithQuantity().entrySet()) {
                MenuItem menuItem = entry.getKey();
                if (menuItem != null) {
                    menuItem.setPrice(menuItemService.findMenuItemDiscountedPrice(menuItem.getId()));

                }
            }
        }


        double totalHT = 0;
        double totalTTC = 0;
        Map<Tax, Double> taxDetails = new HashMap<>();

        List<Map<String, Object>> items = new ArrayList<>();

        for (Map.Entry<MenuItem, Integer> entry : order.getMenuItemsWithQuantity().entrySet()) {
            MenuItem item = entry.getKey();
            int quantity = entry.getValue();
            double itemTTCUnit = item.getPrice();
            double taxRate = item.getTax() != null ? item.getTax().getRate() : 0.0;

            double itemHTUnit = itemTTCUnit / (1 + (taxRate / 100));
            double itemHT = itemHTUnit * quantity;
            double itemTTC = itemTTCUnit * quantity;
            double taxAmount = itemTTC - itemHT;

            itemHTUnit = round(itemHTUnit);
            itemHT = round(itemHT);
            itemTTC = round(itemTTC);
            taxAmount = round(taxAmount);

            if (item.getTax() != null) {
                taxDetails.merge(item.getTax(), taxAmount, Double::sum);
            }

            totalHT += itemHT;
            totalTTC += itemTTC;

            Map<String, Object> itemData = new HashMap<>();
            itemData.put("menuItemName", item.getTitle());
            itemData.put("quantity", quantity);
            itemData.put("priceHT", itemHTUnit);
            itemData.put("priceTTC", itemTTCUnit);
            itemData.put("totalHT", itemHT);
            itemData.put("totalTTC", itemTTC);
            items.add(itemData);
        }

        Timbre timbre = timbreRepository.findAll().stream().findFirst().orElse(null);
        double timbreAmount = (timbre != null) ? timbre.getAmount() : 0.0;

        boolean addTimbre = (round(totalTTC) >= 1.0); // Changed from == 0.1 to >= 1.0
        if (addTimbre) {
            totalTTC += timbreAmount;
            totalTTC = round(totalTTC); // Round after adding timbre
        }

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("orderId", order.getId());
        parameters.put("totalHT", round(totalHT));
        parameters.put("totalTTC", round(totalTTC));
        parameters.put("createdOn", order.getCreatedAt());
        parameters.put("taxDetails", new ArrayList<>(taxDetails.entrySet()));
        parameters.put("addTimbre", addTimbre);
        parameters.put("timbreAmount", timbreAmount);
        parameters.put("net.sf.jasperreports.resource.path", "images");
        JRDataSource dataSource = new JRBeanCollectionDataSource(items);
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);
        return JasperExportManager.exportReportToPdf(jasperPrint);
    }

    private double round(double value) {

        return new BigDecimal(value).setScale(2, RoundingMode.HALF_UP).doubleValue();
    }

}
