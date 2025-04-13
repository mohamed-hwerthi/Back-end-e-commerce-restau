package com.foodsquad.FoodSquad.service.declaration;

import com.foodsquad.FoodSquad.model.dto.MenuItemEntry;
import com.foodsquad.FoodSquad.model.entity.MenuItem;
import com.foodsquad.FoodSquad.model.entity.Order;
import com.foodsquad.FoodSquad.repository.OrderRepository;
import com.foodsquad.FoodSquad.service.impl.OrderService;
import jakarta.persistence.EntityNotFoundException;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Service

public class InvoiceServiceImp implements  InvoiceService {

    private  final OrderService orderService ;

    private final OrderRepository orderRepository;
    private final String invoice_template_path = "/jasper/invoice.jrxml";



    Logger log = LoggerFactory.getLogger(InvoiceService.class);


    public InvoiceServiceImp(OrderService orderService, OrderRepository orderRepository) {
        this.orderService = orderService;
        this.orderRepository = orderRepository;
    }


    @Override
    public File generateOrderInvoice(String orderId, Locale localeKey) {
        try {
             Order order = fetchOrder(orderId);
            if (order == null) {
                log.error("Order not found: {}", orderId);
                 throw  new EntityNotFoundException("Order not found for ID: " + orderId);
            }

            File pdfFile = createTempPdfFile();
            generateInvoicePdf(order, localeKey, pdfFile);
            log.info("Invoice PDF generated successfully: {}", pdfFile.getAbsolutePath());
            return pdfFile;
        } catch (IOException e) {
            log.error("Error creating temporary PDF file", e);
        } catch (Exception e) {
            log.error("An error occurred during PDF creation", e);
        }
        return null;
    }
    private Order fetchOrder(String orderId) {
      return  orderService.getSimpleOrderById(orderId);
    }

    private File createTempPdfFile() throws IOException {
        return File.createTempFile("my-invoice", ".pdf");
    }

    private void generateInvoicePdf(Order orderDTO, Locale localKey, File pdfFile) throws JRException, IOException {
        try (FileOutputStream pos = new FileOutputStream(pdfFile)) {
            JasperReport report = loadTemplate();
            Map<String, Object> parameters = createParameters(orderDTO, localKey);
            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(Collections.singletonList(orderDTO));
            JasperPrint jasperPrint = JasperFillManager.fillReport(report, parameters, dataSource);
            JasperExportManager.exportReportToPdfStream(jasperPrint, pos);
        }
    }

    private JasperReport loadTemplate() throws JRException {
        log.info(String.format("Invoice template path : %s", invoice_template_path));
        final InputStream reportInputStream = getClass().getResourceAsStream(invoice_template_path);
        final JasperDesign jasperDesign = JRXmlLoader.load(reportInputStream);
        return JasperCompileManager.compileReport(jasperDesign);
    }

    private Map<String, Object> createParameters(Order order, Locale localeKey) {
        final Map<String, Object> parameters = new HashMap<>();
        parameters.put("order",  order);
        parameters.put("REPORT_LOCALE", localeKey);
        parameters.put("ClientName", "Jean Dupont");
        parameters.put("InvoiceNumber", "F12345");
        parameters.put("InvoiceDate", new Date()); // Utilisez la date actuelle
        parameters.put("TotalAmount", 125.00);
        return parameters;
    }

    /**
     * Flattens a map of menu items and their associated quantities into a list of {@link MenuItemEntry} objects.
     * Each {@link MenuItemEntry} contains the title, description, price, quantity, and image URL of a menu item.
     *
     * @param menuItemsWithQuantity a map where the key is a {@link MenuItem} and the value is the quantity of that menu item.
     * @return a list of {@link MenuItemEntry} objects, each representing a menu item with its corresponding quantity.
     */
    public List<MenuItemEntry> flattenMenuItems(Map<MenuItem, Integer> menuItemsWithQuantity) {
        List<MenuItemEntry> entries = new ArrayList<>();
        for (Map.Entry<MenuItem, Integer> entry : menuItemsWithQuantity.entrySet()) {
            MenuItem menuItem = entry.getKey();
            Integer quantity = entry.getValue();
            // Create a MenuItemEntry for each MenuItem and its quantity
            MenuItemEntry menuItemEntry = new MenuItemEntry(
                    menuItem.getTitle(),
                    menuItem.getDescription(),
                    menuItem.getPrice(),
                    quantity
            );

            entries.add(menuItemEntry);
        }
        return entries;
    }
    public byte[] generateInvoice(String orderId) throws Exception {
        // Charger le fichier JRXML
        InputStream reportStream = getClass().getResourceAsStream(invoice_template_path);

        // Compiler le rapport
        JasperReport jasperReport = JasperCompileManager.compileReport(reportStream);

        // Récupérer les données de la commande
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        // Préparer les données pour le rapport
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("orderId", order.getId());
        parameters.put("totalCost", order.getTotalCost());
        parameters.put("createdOn", order.getCreatedOn());

        // Convertir les éléments de la commande en une liste de Map
        List<Map<String, Object>> items = new ArrayList<>();

        for (Map.Entry<MenuItem, Integer> entry : order.getMenuItemsWithQuantity().entrySet()) {
            Map<String, Object> item = new HashMap<>();
            item.put("menuItemName", entry.getKey().getTitle());
            item.put("quantity", entry.getValue());
            item.put("price", entry.getKey().getPrice());
            items.add(item);
        }

        // Créer une source de données pour le rapport
        JRDataSource dataSource = new JRBeanCollectionDataSource(items);

        // Remplir le rapport avec les données
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);

        // Exporter le rapport en PDF
        return JasperExportManager.exportReportToPdf(jasperPrint);
    }

}
