package com.krios.pro.Dto;
import lombok.Data;
import java.util.List;
@Data
public class ProductCustomerDTO {

    private String productName;
    private String description;
    private Double price;
    private Integer quantity;
    private String category;
    private List<CustomerDTO> customers;
}
