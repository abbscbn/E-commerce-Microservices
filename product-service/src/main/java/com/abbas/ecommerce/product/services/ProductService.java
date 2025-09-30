package com.abbas.ecommerce.product.services;

import com.abbas.ecommerce.common.exception.BaseException;
import com.abbas.ecommerce.common.exception.ErrorMessage;
import com.abbas.ecommerce.common.exception.ErrorMessageType;
import com.abbas.ecommerce.product.dto.RequestProduct;
import com.abbas.ecommerce.product.dto.ResponseProduct;
import com.abbas.ecommerce.product.dto.ResponseProductImage;
import com.abbas.ecommerce.product.model.Product;
import com.abbas.ecommerce.product.model.ProductImage;
import com.abbas.ecommerce.product.repository.ProductRepository;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

import java.util.*;

@Slf4j
@Service
@Transactional
public class ProductService {

    @Autowired
    ProductRepository productRepository;

    @Autowired
    private Cloudinary cloudinary;

    public ResponseProduct createProduct(RequestProduct request) throws IOException {

        Product product = new Product();
        product.setName(request.name());
        product.setDescription(request.description());
        product.setPrice(request.price());
        product.setStock(request.stock());

        // ProductImage oluştur
        ProductImage image = new ProductImage();
        image.setDesktopUrl(uploadToCloudinary(request.desktopImage(), "desktop"));
        image.setTabletUrl(uploadToCloudinary(request.tabletImage(), "tablet"));
        image.setMobileUrl(uploadToCloudinary(request.mobileImage(), "mobile"));
        image.setMain(true);

        product.setImage(image);  // bidirectional ilişkiyi set et

        Product savedProduct = productRepository.save(product); // tek save yeterli

        ResponseProduct responseProduct = new ResponseProduct();

        BeanUtils.copyProperties(savedProduct, responseProduct);

        if (savedProduct.getImage() != null) {
            ResponseProductImage imageDTO = ResponseProductImage.builder()
                    .desktopUrl(savedProduct.getImage().getDesktopUrl())
                    .tabletUrl(savedProduct.getImage().getTabletUrl())
                    .mobileUrl(savedProduct.getImage().getMobileUrl())
                    .isMain(savedProduct.getImage().isMain())
                    .build();
            responseProduct.setImage(imageDTO);
        }

        return responseProduct;
    }
    private String uploadToCloudinary(MultipartFile file, String folder) throws IOException {
        if (file == null || file.isEmpty()) return null;
        Map uploadResult = cloudinary.uploader().upload(file.getBytes(),
                ObjectUtils.asMap("folder", "products/" + folder));
        return uploadResult.get("secure_url").toString();
    }

    public ResponseProduct updateProduct(RequestProduct request, Long productId) throws IOException {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new BaseException(new ErrorMessage(ErrorMessageType.PRODUCT_NOT_FOUND, productId.toString())));

        // Mevcut alanlar güncelleniyor
        if (request.name() != null) product.setName(request.name());
        if (request.description() != null) product.setDescription(request.description());
        if (request.price() != null) product.setPrice(request.price());
        if (request.stock() != null) product.setStock(request.stock());

        // Görseller kontrol ediliyor
        boolean newImageUploaded = (request.desktopImage() != null && !request.desktopImage().isEmpty()) ||
                (request.tabletImage() != null && !request.tabletImage().isEmpty()) ||
                (request.mobileImage() != null && !request.mobileImage().isEmpty());

        if (newImageUploaded) {
            ProductImage image = product.getImage();
            if (image == null) image = new ProductImage();

            // Sadece gelen dosyaları upload et, diğerlerini koru
            if (request.desktopImage() != null && !request.desktopImage().isEmpty()) {
                image.setDesktopUrl(uploadToCloudinary(request.desktopImage(), "desktop"));
            }
            if (request.tabletImage() != null && !request.tabletImage().isEmpty()) {
                image.setTabletUrl(uploadToCloudinary(request.tabletImage(), "tablet"));
            }
            if (request.mobileImage() != null && !request.mobileImage().isEmpty()) {
                image.setMobileUrl(uploadToCloudinary(request.mobileImage(), "mobile"));
            }

            image.setMain(true);
            product.setImage(image); // ilişkiyi kur
        }

        Product updatedProduct = productRepository.save(product);

        // Response mapping
        ResponseProduct responseProduct = new ResponseProduct();
        BeanUtils.copyProperties(updatedProduct, responseProduct);

        if (updatedProduct.getImage() != null) {
            ResponseProductImage imageDTO = ResponseProductImage.builder()
                    .desktopUrl(updatedProduct.getImage().getDesktopUrl())
                    .tabletUrl(updatedProduct.getImage().getTabletUrl())
                    .mobileUrl(updatedProduct.getImage().getMobileUrl())
                    .isMain(updatedProduct.getImage().isMain())
                    .build();
            responseProduct.setImage(imageDTO);
        }

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

    @Transactional
    public void increaseStock(Long productId, Integer quantity) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found with id " + productId));

        product.setStock(product.getStock() + quantity);

        productRepository.save(product);
    }


}
