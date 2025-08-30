# 🛒 E-commerce Microservices

Bu proje, **Spring Boot** tabanlı mikroservis mimarisi ile geliştirilmiş bir e-ticaret uygulamasıdır.  
Her servis bağımsız olarak çalışır ve ortak bir iletişim yapısı ile haberleşir (örn: RabbitMQ).

## 📂 Proje Yapısı

```plaintext
E-commerce-Microservices/
│── identity-service/      → Kullanıcı yönetimi (Register, Login, Roles, JWT, Redis)
│── product-service/       → Ürün yönetimi (CRUD, Security, Redis blacklist kontrolü)
│── ... (diğer servisler eklenecek)
│── pom.xml                → Parent Maven pom
│── README.md              → Bu dosya
```

---

## 🔑 Identity Service

Identity Service, kullanıcı yönetimini sağlar.
- Kullanıcı kayıt (Register)
- Kullanıcı girişi (Login)
- Rol yönetimi (varsayılan: `USER`)
- JWT tabanlı Authentication
- Redis ile token blacklist (Logout)

### 📦 Kullanılan Teknolojiler
- **Java 17**
- **Spring Boot 3**
- **Spring Data JPA (Hibernate)**
- **Spring Validation**
- **Spring Web**
- **Spring Security (JWT)**
- **PostgreSQL**
- **Redis**

### 📌 Model
- `User`: id, username, email, password, roles
- `Role`: `USER`, `ADMIN`

### 📌 DTO’lar
- `RegisterRequest`
- `LoginRequest`

---

## 📦 Product Service

Product Service, ürün CRUD işlemlerini yönetir.  
Tüm endpointler JWT ile korunur ve Redis blacklist kontrolü yapılır.

### 📌 Özellikler
- Ürün ekleme, güncelleme, silme, listeleme
- JWT doğrulama ve rol bazlı güvenlik
- Redis ile token blacklist kontrolü
- RESTful API mimarisi

### 📌 Model
- `Product`: id, name, description, price, stock

---

## ⚙️ Çalıştırma

1. PostgreSQL’de servisler için ayrı veritabanları oluşturun.  
   (Örn: `identity_db`, `product_db`)
2. `application.properties` veya `application.yml` dosyalarında bağlantı ayarlarını yapın.
3. Redis sunucusunu başlatın (token blacklist için).
4. Servisleri başlatın:
   ```bash
   mvn spring-boot:run
   ```
5. Sağlık kontrolü için:
   ```http
   http://localhost:8080/actuator/health   (Identity Service )
   http://localhost:8081/actuator/health   (Product Service )
   ```

---

## 📌 Planlanan Servisler

- **Identity Service** ✅
- **Product Service** ✅
- **Order Service** ⏳
