package com.abbas.ecommerce.product.controller;

import com.abbas.ecommerce.common.response.RootResponse;
import com.abbas.ecommerce.product.dto.RequestProduct;
import com.abbas.ecommerce.product.dto.ResponseProduct;
import com.abbas.ecommerce.product.services.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    ProductService productService;


    @PostMapping("/save")
    public ResponseEntity<RootResponse<ResponseProduct>> createProduct(@Valid @RequestBody RequestProduct request, WebRequest webRequest) {
        ResponseProduct responseProduct = productService.createProduct(request);

        return ResponseEntity.ok(RootResponse.ok(responseProduct, webRequest));

    }

    @PutMapping("/update/{productId}")
    public ResponseEntity<RootResponse<ResponseProduct>> updateProduct(@Valid @RequestBody RequestProduct request, @PathVariable(name = "productId") Long productId, WebRequest webRequest) {
        ResponseProduct responseProduct = productService.updateProduct(request, productId);
        return ResponseEntity.ok(RootResponse.ok(responseProduct, webRequest));
    }

    @DeleteMapping("/delete/{productId}")
    public ResponseEntity<RootResponse<String>> deleteProductByProductId(@PathVariable(name = "productId") Long productId, WebRequest webRequest) {
        String info = productService.deleteProduct(productId);
        return ResponseEntity.ok(RootResponse.ok(info, webRequest));

    }

    @GetMapping("/get/{productId}")
    public ResponseEntity<RootResponse<ResponseProduct>> getProductByProductId(@PathVariable(name = "productId") Long productId, WebRequest webRequest) {
        ResponseProduct productByProductId = productService.getProductByProductId(productId);
        return ResponseEntity.ok(RootResponse.ok(productByProductId, webRequest));
    }

    @GetMapping("/get")
    public ResponseEntity<RootResponse<List<ResponseProduct>>> getAllProducts(WebRequest webRequest) {
        List<ResponseProduct> allProducts = productService.getAllProducts();
        return ResponseEntity.ok(RootResponse.ok(allProducts, webRequest));
    }


}
