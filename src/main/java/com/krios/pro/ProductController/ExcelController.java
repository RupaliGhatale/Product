package com.krios.pro.ProductController;

import com.krios.pro.Service.ProductService.ExcelService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class ExcelController {
    @Autowired
    private ExcelService excelService;
    @GetMapping("/export")
    public void exportProductsToExcel(HttpServletResponse response) {
        try {
            excelService.exportProductsToExcel(response);
        } catch (IOException e) {
            // Log the exception and return an error response
            ResponseEntity.status(500).body("Failed to export products: " + e.getMessage());
        }
    }
}
