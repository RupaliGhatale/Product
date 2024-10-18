package com.krios.pro.ProductController;
import com.krios.pro.Entity.Product;
import com.krios.pro.Service.ProductService.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;
@RestController
@RequestMapping("/products")
public class ProductController {
    @Autowired
    private ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }
    @PostMapping()
    public ResponseEntity<String> createProductWithCustomers(@RequestBody Product product) {
        try {
            productService.saveProductWithCustomers(product);
            return ResponseEntity.ok("Product and customers saved successfully!");
        } catch (Exception e) {
            // Log the exception and return a meaningful response
            return ResponseEntity.status(500).body("Failed to save product and customers: " + e.getMessage());
        }
    }
    @GetMapping("/{id}")
    public ResponseEntity<?> getProductById(@PathVariable Long id) {
        try {
            Optional<Product> product = productService.getProductById(id);
            if (product.isPresent()) {
                return ResponseEntity.ok(product.get());
            } else {
                return ResponseEntity.status(404).body("Product not found");
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Failed to retrieve product: " + e.getMessage());
        }
    }
    @GetMapping("/page")
    public ResponseEntity<Page<Product>> getAllProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            Page<Product> products = productService.getAllProducts(page, size);
            return ResponseEntity.ok(products);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }
    @PutMapping("/{id}")
    public ResponseEntity<String> updateProduct(@PathVariable Long id, @RequestBody Product product) {
        try {
            productService.updateProduct(id, product);
            return ResponseEntity.ok("Product updated successfully!");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Failed to update product: " + e.getMessage());
        }
    }
        @DeleteMapping("/{id}")
        public ResponseEntity<String> deleteProduct(@PathVariable Long id) {
            try {
                productService.deleteProduct(id);
                return ResponseEntity.ok("Product deleted successfully!");
            } catch (Exception e) {
                return ResponseEntity.status(500).body("Failed to delete product: " + e.getMessage());
            }
        }
}
