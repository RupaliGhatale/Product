package com.krios.pro.Service.ProductService;
import com.krios.pro.Entity.Product;
import org.springframework.data.domain.Page;
import java.util.List;
import java.util.Optional;

public interface ProductService {
    void saveProductWithCustomers(Product product);

    Optional<Product> getProductById(Long id);
    
    void updateProduct(Long id, Product product);

    void deleteProduct(Long id);

    Page<Product> getAllProducts(int page, int size);

    List<Product> getAllProducts();
}
