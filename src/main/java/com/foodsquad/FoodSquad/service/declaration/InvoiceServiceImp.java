package com.foodsquad.FoodSquad.service.declaration;

import com.foodsquad.FoodSquad.model.entity.Order;
import com.foodsquad.FoodSquad.service.OrderService;
import jakarta.persistence.EntityNotFoundException;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
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
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;




@Service

public class InvoiceServiceImp implements  InvoiceService {

    private  final OrderService orderService ;
    private final String invoice_template_path = "/jasper/invoice.jrxml";



    Logger log = LoggerFactory.getLogger(InvoiceService.class);


    public InvoiceServiceImp(OrderService orderService) {
        this.orderService = orderService;
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

        return parameters;
    }


}
