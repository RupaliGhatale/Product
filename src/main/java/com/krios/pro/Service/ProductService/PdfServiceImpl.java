package com.krios.pro.Service.ProductService;
import com.krios.pro.Entity.Customer;
import com.krios.pro.Entity.Product;
import com.krios.pro.ProductRepository.PdfRepository;
import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.pdf.*;
import com.lowagie.text.pdf.draw.LineSeparator;
import jakarta.servlet.ServletOutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.awt.*;
import java.text.DecimalFormat;
import java.util.Optional;

@Service
public class PdfServiceImpl implements PdfService {
    private static final Logger logger = LoggerFactory.getLogger(PdfServiceImpl.class);
    // Mobile company details
    // Mobile company details
    private static final String COMPANY_NAME = "Mobile Inc.";
    private static final String COMPANY_LOGO = "C:\\Users\\hp\\Desktop\\Task\\com.krios\\productlogo.png"; // Corrected path
    private static final String PAID_LOGO = "C:\\Users\\hp\\Desktop\\Task\\com.krios\\paid.png"; // Paid logo path


    @Autowired
    private PdfRepository pdfRepository;


    @Override
    public void generateInvoicePdf(int id, ServletOutputStream outputStream) throws Exception {
        Optional<Product> productOptional = pdfRepository.findById(id);

        if (productOptional.isPresent()) {
            Product product = productOptional.get();

            // Create the PDF document
            Document document = new Document(PageSize.A4, 50, 50, 50, 50);
            PdfWriter writer = PdfWriter.getInstance(document, outputStream);
            writer.setPageEvent(new PdfPageEventHelper() {
                @Override
                public void onEndPage(PdfWriter writer, Document document) {
                    PdfContentByte cb = writer.getDirectContent();
                    // Footer (Page number)
                    ColumnText.showTextAligned(cb, Element.ALIGN_CENTER, new Phrase(
                                    "Page " + writer.getPageNumber()),
                            (document.right() - document.left()) / 2 + document.leftMargin(),
                            document.bottom() - 10, 0);
                    // Footer (Company details)
                    ColumnText.showTextAligned(cb, Element.ALIGN_CENTER, new Phrase(
                                    COMPANY_NAME + " | contact@mobileinc.com | +1-234-567-890"),
                            (document.right() - document.left()) / 2 + document.leftMargin(),
                            document.bottom() - 25, 0);
                }

                @Override
                public void onStartPage(PdfWriter writer, Document document) {
                    try {
                        // Load the company logo image
                        Image logo = Image.getInstance(COMPANY_LOGO);
                        logo.scaleToFit(100, 50); // Adjust the size as necessary
                        logo.setAlignment(Element.ALIGN_LEFT);
                        document.add(logo);

                        // Add "INVOICE" header
                        Paragraph header = new Paragraph("INVOICE", new Font(Font.HELVETICA, 20, Font.BOLD));
                        header.setAlignment(Element.ALIGN_CENTER);  // Center alignment
                        document.add(header);

                        // Add a line separator
                        LineSeparator ls = new LineSeparator();
                        document.add(new Chunk(ls));
                    } catch (Exception e) {
                        logger.error("Error adding header: ", e);
                    }
                }
            });

            // Open the document to start writing
            document.open();

            // Define fonts
            Font boldFont = new Font(Font.HELVETICA, 14, Font.BOLD);
            Font normalFont = new Font(Font.HELVETICA, 12, Font.NORMAL);

            // Billing Information
            Paragraph billTo = new Paragraph("BILL TO", boldFont);
            document.add(billTo);

            // Add customer details
            if (product.getCustomers() != null && !product.getCustomers().isEmpty()) {
                for (Customer customer : product.getCustomers()) {
                    Paragraph customerDetails = new Paragraph(
                            "Customer Name: " + customer.getName() + "\n" +
                                    "Customer Email: " + customer.getEmail() + "\n",
                            normalFont
                    );
                    document.add(customerDetails);
                }
            } else {
                Paragraph noCustomerMessage = new Paragraph("No customer information available.", normalFont);
                document.add(noCustomerMessage);
            }

            // Add space
            document.add(Chunk.NEWLINE);

            // Product Details Section
            Paragraph productDetails = new Paragraph("Invoice Details", boldFont);
            document.add(productDetails);

            // Add product name and ID at the top
            Paragraph productNameId = new Paragraph(
                    "Product Name: " + product.getProductName() + " (ID: " + product.getId() + ")", normalFont
            );
            productNameId.setSpacingBefore(10f);
            document.add(productNameId);

            // Formatting for price
            DecimalFormat priceFormat = new DecimalFormat("#,##0.00");

            // Create table for product details
            PdfPTable table = new PdfPTable(6); // 6 columns: Product Name, ID, Description, Quantity, Price, Total
            table.setWidthPercentage(100); // Full width
            table.setSpacingBefore(10f);
            table.setSpacingAfter(10f);
            float[] columnWidths = {2f, 1f, 4f, 2f, 2f, 2f}; // Adjust column widths
            table.setWidths(columnWidths);

            // Table Headers
            addTableHeader(table, "PRODUCT NAME", "ID", "DESCRIPTION", "QUANTITY", "PRICE", "TOTAL");

            // Product details row
            addProductRow(table, product.getProductName(), product.getId(), product.getDescription(), product.getQuantity(), product.getPrice(), priceFormat);

            // Add the table to the document
            document.add(table);

            // Add a "Thank You" message at the end
            Paragraph thankYouMessage = new Paragraph("Thank you for your purchase!", boldFont);
            thankYouMessage.setAlignment(Paragraph.ALIGN_CENTER);
            thankYouMessage.setSpacingBefore(20f);
            document.add(thankYouMessage);

            // Add "PAID" logo in the center at the end
            Image paidLogo = Image.getInstance("C:\\Users\\hp\\Desktop\\Task\\com.krios\\paid.png"); // Path to the "PAID" logo
            paidLogo.scaleToFit(100, 50); // Adjust size as necessary
            paidLogo.setAlignment(Element.ALIGN_CENTER); // Center alignment
            document.add(paidLogo);

            // Close the document after writing content
            document.close();

            // Log success message
            logger.info("Invoice generated successfully for Product ID: " + id);
        } else {
            logger.error("Product not found with ID: " + id);
            throw new Exception("Product not found with ID: " + id);
        }
    }

    // Method to add headers to the table
    private void addTableHeader(PdfPTable table, String... headers) {
        for (String header : headers) {
            PdfPCell cell = new PdfPCell(new Phrase(header, new Font(Font.HELVETICA, 12, Font.BOLD)));
            cell.setPadding(8f);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
        }
    }

    // Method to add product details row to the table
    private void addProductRow(PdfPTable table, String productName, Long productId, String description, int quantity, double price, DecimalFormat priceFormat) {
        PdfPCell productNameCell = new PdfPCell(new Phrase(productName, new Font(Font.HELVETICA, 12)));
        productNameCell.setPadding(8f);
        table.addCell(productNameCell);

        PdfPCell productIdCell = new PdfPCell(new Phrase(String.valueOf(productId), new Font(Font.HELVETICA, 12)));
        productIdCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        productIdCell.setPadding(8f);
        table.addCell(productIdCell);

        PdfPCell descriptionCell = new PdfPCell(new Phrase(description, new Font(Font.HELVETICA, 12)));
        descriptionCell.setPadding(8f);
        table.addCell(descriptionCell);

        PdfPCell quantityCell = new PdfPCell(new Phrase(String.valueOf(quantity), new Font(Font.HELVETICA, 12)));
        quantityCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        quantityCell.setPadding(8f);
        table.addCell(quantityCell);

        PdfPCell priceCell = new PdfPCell(new Phrase("$" + priceFormat.format(price), new Font(Font.HELVETICA, 12)));
        priceCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        priceCell.setPadding(8f);
        table.addCell(priceCell);

        PdfPCell totalCell = new PdfPCell(new Phrase("$" + priceFormat.format(price * quantity), new Font(Font.HELVETICA, 12)));
        totalCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        totalCell.setPadding(8f);
        table.addCell(totalCell);
    }
}