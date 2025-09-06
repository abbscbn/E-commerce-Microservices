package com.abbas.ecommerce.product.services;

import com.abbas.ecommerce.common.exception.BaseException;
import com.abbas.ecommerce.common.exception.ErrorMessage;
import com.abbas.ecommerce.common.exception.ErrorMessageType;
import com.abbas.ecommerce.product.dto.RequestProduct;
import com.abbas.ecommerce.product.dto.ResponseProduct;
import com.abbas.ecommerce.product.model.Product;
import com.abbas.ecommerce.product.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Slf4j
@Service
@Transactional
public class ProductService {

    @Autowired
    ProductRepository productRepository;

    public ResponseProduct createProduct(RequestProduct request) {

        Product product = new Product();
        ResponseProduct responseProduct = new ResponseProduct();

        product.setName(request.name());
        product.setDescription(request.description());
        product.setPrice(request.price());
        product.setStock(request.stock());

        Product savedProduct = productRepository.save(product);

        BeanUtils.copyProperties(savedProduct, responseProduct);
        return responseProduct;
    }

    public ResponseProduct updateProduct(RequestProduct request, Long productId) {
        ResponseProduct responseProduct = new ResponseProduct();
        Optional<Product> optProduct = productRepository.findById(productId);

        if (optProduct.isEmpty()) {
            throw new BaseException(new ErrorMessage(ErrorMessageType.PRODUCT_NOT_FOUND, productId.toString()));

        }
        Product product = optProduct.get();

        product.setName(request.name());
        product.setDescription(request.description());
        product.setPrice(request.price());
        product.setStock(request.stock());

        Product updatedProduct = productRepository.save(product);

        BeanUtils.copyProperties(updatedProduct, responseProduct);

        return responseProduct;
    }

    public String deleteProduct(Long productId) {
        productRepository.findById(productId).orElseThrow(() -> new BaseException(new ErrorMessage(ErrorMessageType.PRODUCT_NOT_FOUND, productId.toString())));
        productRepository.deleteById(productId);
        return "Silme Başarılı";
    }

    public ResponseProduct getProductByProductId(Long productId) {
        ResponseProduct responseProduct = new ResponseProduct();
        Optional<Product> optProduct = productRepository.findById(productId);
        if (optProduct.isEmpty()) {
            throw new BaseException(new ErrorMessage(ErrorMessageType.PRODUCT_NOT_FOUND, productId.toString()));
        }
        Product product = optProduct.get();
        BeanUtils.copyProperties(product, responseProduct);
        return responseProduct;
    }

    public List<ResponseProduct> getAllProducts() {
        List<ResponseProduct> responseProducts = new ArrayList<>();

        for (Product p : productRepository.findAll()) {

            ResponseProduct responseProduct = new ResponseProduct();
            BeanUtils.copyProperties(p, responseProduct);

            responseProducts.add(responseProduct);
        }
        return responseProducts;

    }


    public CheckProductModel checkProduct(Long productId, Integer quality) {
        CheckProductModel checkProductModel = new CheckProductModel();
        Optional<Product> optProduct = productRepository.findById(productId);

        try {
            if (optProduct.isEmpty()) {
                throw new BaseException(new ErrorMessage(ErrorMessageType.PRODUCT_NOT_FOUND, productId.toString()));

            } else {

                if (optProduct.get().getStock() - quality >= 0) {
                    checkProductModel.setCheck(true);
                    checkProductModel.setDesc(null);
                    return checkProductModel;

                } else {
                    checkProductModel.setCheck(false);
                    checkProductModel.setDesc("STOK YETERSİZ MEVCUT MİKTAR: " + optProduct.get().getStock());
                    return checkProductModel;
                }

            }
        } catch (BaseException e) {
            checkProductModel.setCheck(false);
            checkProductModel.setDesc("İLGİLİ PRODUCT ID BULUNAMADI PRODUCTID: : " + productId.toString());

            return checkProductModel;
        }

    }

    public void setProductStockByProductId(Long productId, Integer quality) {
        Optional<Product> optProduct = productRepository.findById(productId);

        if (optProduct.isPresent()) {
            Product product = optProduct.get();
            product.setStock(product.getStock() - quality);
            productRepository.save(product);
        }
    }


}
