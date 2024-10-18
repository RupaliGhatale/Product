package com.krios.pro.Service.ProductService;

import com.krios.pro.Entity.Customer;
import com.krios.pro.Entity.Product;
import com.krios.pro.ProductRepository.ExcelRepository;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class ExcelServieImpl implements ExcelService {
    @Autowired
   private ExcelRepository excelRepository;

    @Override
    public void exportProductsToExcel(HttpServletResponse response) throws IOException {
        List<Product> products = excelRepository.findAll();

        // Create a new workbook and a sheet
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Products");

        // Create header row
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("Product ID");
        headerRow.createCell(1).setCellValue("Product Name");
        headerRow.createCell(2).setCellValue("Description");
        headerRow.createCell(3).setCellValue("Price");
        headerRow.createCell(4).setCellValue("Quantity");
        headerRow.createCell(5).setCellValue("Category");
        headerRow.createCell(6).setCellValue("Customer ID");
        headerRow.createCell(7).setCellValue("Customer Name");
        headerRow.createCell(8).setCellValue("Customer Email");

        // Create data rows
        int rowCount = 1;
        for (Product product : products) {
            List<Customer> customers = product.getCustomers();
            if (customers != null && !customers.isEmpty()) {
                for (Customer customer : customers) {
                    Row dataRow = sheet.createRow(rowCount++);
                    dataRow.createCell(0).setCellValue(product.getId());
                    dataRow.createCell(1).setCellValue(product.getProductName());
                    dataRow.createCell(2).setCellValue(product.getDescription());
                    dataRow.createCell(3).setCellValue(product.getPrice());
                    dataRow.createCell(4).setCellValue(product.getQuantity());
                    dataRow.createCell(5).setCellValue(product.getCategory());
                    dataRow.createCell(6).setCellValue(customer.getId());
                    dataRow.createCell(7).setCellValue(customer.getName());
                    dataRow.createCell(8).setCellValue(customer.getEmail());
                }
            } else {
                Row dataRow = sheet.createRow(rowCount++);
                dataRow.createCell(0).setCellValue(product.getId());
                dataRow.createCell(1).setCellValue(product.getProductName());
                dataRow.createCell(2).setCellValue(product.getDescription());
                dataRow.createCell(3).setCellValue(product.getPrice());
                dataRow.createCell(4).setCellValue(product.getQuantity());
                dataRow.createCell(5).setCellValue(product.getCategory());
                dataRow.createCell(6).setCellValue("N/A");
                dataRow.createCell(7).setCellValue("N/A");
                dataRow.createCell(8).setCellValue("N/A");
            }
        }

        // Set response headers
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment; filename=products.xlsx");

        // Write the output to the response
        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();
        outputStream.close();
    }
}

