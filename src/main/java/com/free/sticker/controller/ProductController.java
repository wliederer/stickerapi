package com.free.sticker.controller;

import com.free.sticker.models.Event;
import com.free.sticker.models.Product;
import com.free.sticker.models.ProductDTO;
import com.free.sticker.service.EventService;
import com.free.sticker.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private EventService eventService;

    @GetMapping
    public ResponseEntity<List<ProductDTO>> getAllProducts() {
        List<Product> products = productService.getAllProducts();
        List<ProductDTO> productDTOs = productService.mapToDTOList(products);
        return new ResponseEntity<>(productDTOs, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getProductById(@PathVariable Long id) {
        Optional<Product> product = productService.getProductById(id);
        return product.map(value -> new ResponseEntity<>(productService.mapToDTO(value), HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<ProductDTO> createProduct(@RequestBody ProductDTO productDTO) {
        Optional<Event> eventOptional = eventService.getEventById(productDTO.getEventId());
        if (eventOptional.isPresent()) {
            Product product = new Product();
            product.setTitle(productDTO.getTitle());
            product.setPrice(productDTO.getPrice());
            product.setImage(productDTO.getImage());
            product.setEvent(eventOptional.get());

            Product createdProduct = productService.saveProduct(product);
            return new ResponseEntity<>(productDTO, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        Optional<Product> product = productService.getProductById(id);
        if (product.isPresent()) {
            productService.deleteProduct(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
