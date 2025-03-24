package com.foodsquad.FoodSquad.service.declaration;

import java.io.File;
import java.util.Locale;

public interface InvoiceService {

      File generateOrderInvoice(String orderId  , Locale localeKey);

}
