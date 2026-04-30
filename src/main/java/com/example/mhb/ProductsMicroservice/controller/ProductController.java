package com.example.mhb.ProductsMicroservice.controller;

import java.util.Date;

import com.example.mhb.ProductsMicroservice.model.CreateProductRestModel;
import com.example.mhb.ProductsMicroservice.model.ErrorMessage;
import com.example.mhb.ProductsMicroservice.services.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/products") //http://localhost:<port>/products
public class ProductController {

    ProductService productService;
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("/create-product-async")
    public ResponseEntity<Object> createProductAsync(@RequestBody CreateProductRestModel product) {

        String productId;
        try {
            productId = productService.createProductAsync(product);
        } catch (Exception e) {
            //e.printStackTrace();
            LOGGER.error(e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorMessage(new Date(), e.getMessage(),"/products"));
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(productId);
    }
    @PostMapping("/create-product-sync")
    public ResponseEntity<Object> createProductSync(@RequestBody CreateProductRestModel product) {

        String productId;
        try {
            productId = productService.createProductSync(product);
        } catch (Exception e) {
            //e.printStackTrace();
            LOGGER.error(e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorMessage(new Date(), e.getMessage(),"/products"));
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(productId);
    }
}
