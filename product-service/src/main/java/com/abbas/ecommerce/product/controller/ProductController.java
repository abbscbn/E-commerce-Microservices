package com.abbas.ecommerce.product.controller;

import com.abbas.ecommerce.product.dto.RequestProduct;
import com.abbas.ecommerce.product.dto.ResponseProduct;
import com.abbas.ecommerce.product.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    ProductService productService;

    @GetMapping("/test")
    public ResponseEntity<String> test(){
      return  ResponseEntity.ok("Test başarılı");
    }

    @PostMapping("/save")
    public ResponseEntity<ResponseProduct> createProduct(@RequestBody RequestProduct request){
        ResponseProduct responseProduct = productService.createProduct(request);

        return ResponseEntity.ok(responseProduct);

    }

    @PutMapping("/update/{productId}")
    public ResponseEntity<ResponseProduct> updateProduct(@RequestBody RequestProduct request,@PathVariable(name = "productId") Long productId){
        ResponseProduct responseProduct = productService.updateProduct(request, productId);
        return ResponseEntity.ok(responseProduct);
    }

    @DeleteMapping("/delete/{productId}")
    public ResponseEntity<String> deleteProductByProductId(@PathVariable(name = "productId") Long productId){
        String info = productService.deleteProduct(productId);
        return ResponseEntity.ok(info);

    }
    @GetMapping("/get/{productId}")
    public ResponseEntity<ResponseProduct> getProductByProductId(@PathVariable(name = "productId") Long productId){
        ResponseProduct productByProductId = productService.getProductByProductId(productId);
        return ResponseEntity.ok(productByProductId);
    }
    @GetMapping("/get")
    public ResponseEntity<List<ResponseProduct>> getAllProducts(){
        List<ResponseProduct> allProducts = productService.getAllProducts();
        return ResponseEntity.ok(allProducts);
    }



}
