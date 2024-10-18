package com.krios.pro.Service.ProductService;
import com.krios.pro.Entity.Customer;
import com.krios.pro.Entity.Product;
import com.krios.pro.Exception.ProductException;
import com.krios.pro.ProductRepository.CustomerRepository;
import com.krios.pro.ProductRepository.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {
    private static final Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CustomerRepository customerRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public void saveProductWithCustomers(Product product) {
        try {
            logger.info("Saving product with customers: {}", product.getProductName());

            List<Customer> customers = product.getCustomers().stream().map(customer -> {
                Customer newCustomer = new Customer();
                newCustomer.setName(customer.getName());
                newCustomer.setEmail(customer.getEmail());
                newCustomer.setProduct(product);  // Associate the product with each customer
                return newCustomer;
            }).collect(Collectors.toList());

            // Set the modified customer list back to the product
            product.setCustomers(customers);

            // Save the product along with associated customers (due to cascading)
            productRepository.save(product);

            logger.info("Product and associated customers saved successfully: {}", product.getProductName());
        } catch (Exception e) {
            logger.error("Error occurred while saving product with customers: {}", e.getMessage());
            throw new ProductException("Failed to save product and customers: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<Product> getProductById(Long id) {
        try {
            logger.info("Fetching product with ID: {}", id);
            Optional<Product> product = productRepository.findById(id);
            if (product.isPresent()) {
                logger.info("Product found: {}", product.get().getProductName());
            } else {
                logger.warn("Product not found for ID: {}", id);
            }
            return product;
        } catch (Exception e) {
            logger.error("Error fetching product by ID: {}", e.getMessage(), e);
            throw e; // Rethrow exception for handling in the controller
        }
    }

    @Override
    public void updateProduct(Long id, Product product) {
        try {
            logger.info("Updating product with ID: {}", id);
            Optional<Product> existingProductOpt = productRepository.findById(id);
            if (existingProductOpt.isPresent()) {
                Product existingProduct = existingProductOpt.get();
                existingProduct.setProductName(product.getProductName());
                existingProduct.setDescription(product.getDescription());
                existingProduct.setPrice(product.getPrice());
                existingProduct.setQuantity(product.getQuantity());
                existingProduct.setCategory(product.getCategory());

                // Update associated customers
                List<Customer> customers = product.getCustomers().stream().map(customer -> {
                    Customer newCustomer = new Customer();
                    newCustomer.setName(customer.getName());
                    newCustomer.setEmail(customer.getEmail());
                    newCustomer.setProduct(existingProduct); // Associate the product with each customer
                    return newCustomer;
                }).collect(Collectors.toList());

                existingProduct.setCustomers(customers);

                // Save the updated product
                productRepository.save(existingProduct);
                logger.info("Product updated successfully: {}", existingProduct.getProductName());
            } else {
                logger.warn("Product not found for ID: {}", id);
                throw new RuntimeException("Product not found");
            }
        } catch (Exception e) {
            logger.error("Error updating product: {}", e.getMessage(), e);
            throw e; // Rethrow exception for handling in the controller
        }
    }

    @Override
    public void deleteProduct(Long id) {
        try {
            logger.info("Deleting product with ID: {}", id);
            if (productRepository.existsById(id)) {
                productRepository.deleteById(id);
                logger.info("Product deleted successfully: ID {}", id);
            } else {
                logger.warn("Product not found for ID: {}", id);
                throw new RuntimeException("Product not found");
            }
        } catch (Exception e) {
            logger.error("Error deleting product: {}", e.getMessage(), e);
            throw e; // Rethrow exception for handling in the controller
        }
    }

    @Override
    public Page<Product> getAllProducts(int page, int size) {
        return productRepository.findAll(PageRequest.of(page, size));
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }


}















