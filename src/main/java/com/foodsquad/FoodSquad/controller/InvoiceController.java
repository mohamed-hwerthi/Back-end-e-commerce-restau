package com.foodsquad.FoodSquad.controller;

import com.foodsquad.FoodSquad.service.declaration.InvoiceService;
import com.foodsquad.FoodSquad.service.declaration.InvoiceServiceImp;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.time.LocalDateTime;
import java.util.Locale;

/**
 * Controller for managing menus in the Food Squad application.
 * Provides APIs for retrieving, creating, updating, and deleting menus.
 */
@Validated
@RestController
@RequestMapping("/api/invoice")

@Tag(name = "9. Invoice Management", description = "Invoice Management API")
public class InvoiceController {
    private final InvoiceServiceImp invoiceService;

    public InvoiceController(InvoiceService invoiceServiceImp, InvoiceServiceImp invoiceService) {

        this.invoiceService = invoiceService;
    }

//    @Operation(summary = "Generate Order Invoice", description = "Generates and returns an invoice for the given order ID and locale.")
//    @PostMapping("/generate")
//    public ResponseEntity<Resource> generateOrderInvoice(
//            @Parameter(description = "ID of the order", required = true) @RequestParam(name = "orderId") String orderId,
//            @Parameter(description = "Locale key for invoice generation", required = true) @RequestParam Locale localeKey) {
//        File pdfFile = invoiceService.generateOrderInvoice(orderId, localeKey);
//        if (pdfFile == null || !pdfFile.exists()) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
//        }
//        Resource resource = new FileSystemResource(pdfFile);
//        HttpHeaders headers = new HttpHeaders();
//        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=invoice_" + orderId + ".pdf");
//
//        return ResponseEntity.ok()
//                .headers(headers)
//                .contentLength(pdfFile.length())
//                .contentType(MediaType.APPLICATION_PDF)
//                .body(resource);
//    }

    /**
     *
     * author ismail benkraiem
     * @param orderId
     * @return
     */
    @Operation(summary = "Generate Order Invoice", description = "Generates and returns an invoice for the given order ID and locale.")
    @GetMapping("/{orderId}")
    public ResponseEntity<byte[]> downloadInvoice(@PathVariable String orderId) {
        try {
            byte[] pdfBytes = invoiceService.generateInvoice(orderId);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", "invoice_" + orderId + LocalDateTime.now()+".pdf");

            return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
