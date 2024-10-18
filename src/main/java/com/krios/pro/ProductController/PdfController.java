package com.krios.pro.ProductController;
import com.krios.pro.Service.ProductService.PdfService;
import com.krios.pro.Service.ProductService.ProductService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import java.io.IOException;
@RestController
public class PdfController {
    @Autowired
    private ProductService productService;

    @Autowired
    private PdfService pdfService;
    @GetMapping("/invoice/{id}")
    public void exportInvoiceToPdf(
            @PathVariable int id,
            HttpServletResponse response) {

        try {
            // Set the response type to PDF
            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "attachment; filename=invoice_" + id + ".pdf");

            // Call the service to generate the PDF invoice based on the invoice ID
            pdfService.generateInvoicePdf(id, response.getOutputStream());
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            try {
                response.getWriter().write("Error generating PDF: " + e.getMessage());
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }
}
