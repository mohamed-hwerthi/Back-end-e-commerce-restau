package com.foodsquad.FoodSquad.service.declaration;

public interface InvoiceService {

    byte[] generateInvoice(String orderId) throws Exception;


}
