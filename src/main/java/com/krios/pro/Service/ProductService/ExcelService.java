package com.krios.pro.Service.ProductService;

import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public interface ExcelService{
    void exportProductsToExcel(HttpServletResponse response) throws IOException;

    ;
}
