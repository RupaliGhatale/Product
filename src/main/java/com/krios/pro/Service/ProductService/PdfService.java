package com.krios.pro.Service.ProductService;

import jakarta.servlet.ServletOutputStream;

public interface PdfService {

    void generateInvoicePdf(int id, ServletOutputStream outputStream) throws Exception;
}
