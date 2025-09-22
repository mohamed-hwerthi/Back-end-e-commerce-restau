package com.foodsquad.FoodSquad.service.declaration;

import com.foodsquad.FoodSquad.model.entity.Order;
import com.foodsquad.FoodSquad.model.entity.Product;
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

        prepareDiscountedPrices(order);

        Map<Tax, BigDecimal> taxDetails = new HashMap<>();
        List<Map<String, Object>> items = buildInvoiceItems(order, taxDetails);

        BigDecimal totalHT = items.stream()
                .map(i -> (BigDecimal) i.get("totalHT"))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalTTC = items.stream()
                .map(i -> (BigDecimal) i.get("totalTTC"))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Timbre timbre = timbreRepository.findAll().stream().findFirst().orElse(null);
        BigDecimal timbreAmount = (timbre != null) ? timbre.getAmount() : BigDecimal.ZERO;

        boolean addTimbre = totalTTC.compareTo(BigDecimal.ONE) >= 0;
        if (addTimbre) {
            totalTTC = applyTimbre(totalTTC, timbreAmount);
        }

        Map<String, Object> parameters = createParameters(order, totalHT, totalTTC, taxDetails, timbreAmount, addTimbre);

        JRDataSource dataSource = new JRBeanCollectionDataSource(items);
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);
        return JasperExportManager.exportReportToPdf(jasperPrint);
    }

    private void prepareDiscountedPrices(Order order) {
        if (order.getMenuItemsWithQuantity() != null) {
            for (Map.Entry<Product, Integer> entry : order.getMenuItemsWithQuantity().entrySet()) {
                Product product = entry.getKey();
                if (product != null) {
                    product.setPrice(menuItemService.findMenuItemDiscountedPrice(product.getId()));
                }
            }
        }
    }

    private List<Map<String, Object>> buildInvoiceItems(Order order, Map<Tax, BigDecimal> taxDetails) {
        List<Map<String, Object>> items = new ArrayList<>();

        for (Map.Entry<Product, Integer> entry : order.getMenuItemsWithQuantity().entrySet()) {
            Product item = entry.getKey();
            int quantity = entry.getValue();

            BigDecimal itemTTCUnit = item.getPrice();
            BigDecimal taxRate = (item.getTax() != null)
                    ? BigDecimal.valueOf(item.getTax().getRate()).divide(BigDecimal.valueOf(100), 4, RoundingMode.HALF_UP)
                    : BigDecimal.ZERO;

            BigDecimal itemHTUnit = itemTTCUnit.divide(BigDecimal.ONE.add(taxRate), 4, RoundingMode.HALF_UP);
            BigDecimal itemHT = itemHTUnit.multiply(BigDecimal.valueOf(quantity));
            BigDecimal itemTTC = itemTTCUnit.multiply(BigDecimal.valueOf(quantity));
            BigDecimal taxAmount = itemTTC.subtract(itemHT);

            itemHTUnit = round(itemHTUnit);
            itemHT = round(itemHT);
            itemTTC = round(itemTTC);
            taxAmount = round(taxAmount);

            if (item.getTax() != null) {
                taxDetails.merge(item.getTax(), taxAmount, BigDecimal::add);
            }

            Map<String, Object> itemData = new HashMap<>();
            itemData.put("menuItemName", item.getTitle());
            itemData.put("quantity", quantity);
            itemData.put("priceHT", itemHTUnit);
            itemData.put("priceTTC", itemTTCUnit);
            itemData.put("totalHT", itemHT);
            itemData.put("totalTTC", itemTTC);
            items.add(itemData);
        }

        return items;
    }

    private BigDecimal applyTimbre(BigDecimal totalTTC, BigDecimal timbreAmount) {
        return totalTTC.add(timbreAmount).setScale(2, RoundingMode.HALF_UP);
    }

    private Map<String, Object> createParameters(Order order,
                                                 BigDecimal totalHT,
                                                 BigDecimal totalTTC,
                                                 Map<Tax, BigDecimal> taxDetails,
                                                 BigDecimal timbreAmount,
                                                 boolean addTimbre) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("orderId", order.getId());
        parameters.put("totalHT", round(totalHT));
        parameters.put("totalTTC", round(totalTTC));
        parameters.put("createdOn", order.getCreatedAt());
        parameters.put("taxDetails", new ArrayList<>(taxDetails.entrySet()));
        parameters.put("addTimbre", addTimbre);
        parameters.put("timbreAmount", round(timbreAmount));
        parameters.put("net.sf.jasperreports.resource.path", "images");
        return parameters;
    }

    private BigDecimal round(BigDecimal value) {
        return value.setScale(2, RoundingMode.HALF_UP);
    }


    private double round(double value) {

        return new BigDecimal(value).setScale(2, RoundingMode.HALF_UP).doubleValue();
    }

}
