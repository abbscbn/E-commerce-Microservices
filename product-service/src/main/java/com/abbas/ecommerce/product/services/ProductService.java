package com.abbas.ecommerce.product.services;

import com.abbas.ecommerce.product.dto.RequestProduct;
import com.abbas.ecommerce.product.dto.ResponseProduct;
import com.abbas.ecommerce.product.model.Product;
import com.abbas.ecommerce.product.repository.ProductRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    ProductRepository productRepository;

    public ResponseProduct createProduct(RequestProduct request){

        Product product= new Product();
        ResponseProduct responseProduct= new ResponseProduct();

        product.setName(request.name());
        product.setDescription(request.description());
        product.setPrice(request.price());
        product.setStock(request.stock());

        Product savedProduct = productRepository.save(product);

        BeanUtils.copyProperties(savedProduct,responseProduct);
        return responseProduct;
    }
    public ResponseProduct updateProduct(RequestProduct request, Long productId){
        ResponseProduct responseProduct= new ResponseProduct();
        Optional<Product> optProduct = productRepository.findById(productId);

        if(optProduct.isEmpty()){
            System.out.println("Product bulunamadı hatası fırlat burada");
            return null;
        }
        Product product=optProduct.get();

        product.setName(request.name());
        product.setDescription(request.description());
        product.setPrice(request.price());
        product.setStock(request.stock());

        Product updatedProduct = productRepository.save(product);

        BeanUtils.copyProperties(updatedProduct,responseProduct);

        return responseProduct;
    }

    public String deleteProduct(Long productId){
        productRepository.deleteById(productId);
        return "Silme Başarılı";
    }

    public ResponseProduct getProductByProductId(Long productId){
        ResponseProduct responseProduct= new ResponseProduct();
        Optional<Product> optProduct = productRepository.findById(productId);
        if(optProduct.isEmpty()){
            System.out.println("Product Bulunamadı..");
            return null;
        }
        Product product = optProduct.get();
        BeanUtils.copyProperties(product,responseProduct);
        return responseProduct;
    }

    public List<ResponseProduct> getAllProducts(){
        List<ResponseProduct> responseProducts= new ArrayList<>();
        ResponseProduct responseProduct= new ResponseProduct();
        for(Product p: productRepository.findAll()){
            BeanUtils.copyProperties(p,responseProduct);
            responseProducts.add(responseProduct);
        }
        return responseProducts;

    }


}
