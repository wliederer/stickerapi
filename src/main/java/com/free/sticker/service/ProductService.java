package com.free.sticker.service;

import com.free.sticker.models.Product;
import com.free.sticker.models.ProductDTO;
import com.free.sticker.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }

    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }

    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    public ProductDTO mapToDTO(Product product) {
        ProductDTO dto = new ProductDTO();
        dto.setId(product.getId());
        dto.setTitle(product.getTitle());
        dto.setPrice(product.getPrice());
        dto.setImage(product.getImage());

        if (product.getEvent() != null) {
            dto.setEventId(product.getEvent().getId());
            dto.setEventTitle(product.getEvent().getTitle());
        }

        return dto;
    }

    public List<ProductDTO> mapToDTOList(List<Product> products) {
        return products.stream().map(this::mapToDTO).collect(Collectors.toList());
    }
}
