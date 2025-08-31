# 🛒 E-commerce Microservices

Bu proje, **Spring Boot** tabanlı mikroservis mimarisi ile geliştirilmiş bir e-ticaret uygulamasıdır.  
Her servis bağımsız olarak çalışır ve **RabbitMQ** üzerinden event-driven iletişim sağlar.  
Ayrıca bir `common-lib` modülü ile servisler arası ortak DTO ve Event yapıları paylaşılır.

## 📂 Proje Yapısı

```plaintext
E-commerce-Microservices/
│── common-lib/           → Ortak DTO ve Event sınıfları (OrderCreatedEvent vb.)
│── identity-service/     → Kullanıcı yönetimi (Register, Login, Roles, JWT, Redis)
│── product-service/      → Ürün yönetimi (CRUD, Security, Stok kontrolü)
│── order-service/        → Sipariş yönetimi (CRUD, Event publishing)
│── pom.xml               → Parent Maven pom
│── README.md             → Bu dosya
```

---

## 🔑 Identity Service

Identity Service, kullanıcı yönetimini sağlar.
- Kullanıcı kayıt (Register)
- Kullanıcı girişi (Login)
- Rol yönetimi (`USER , ADMIN`)
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
- `AuthResponse`
---

## 📦 Product Service

Product Service, ürün CRUD işlemlerini yönetir ve sipariş event’lerini dinler. Jwt token ile erişim vardır

### 📌 Özellikler
- Ürün ekleme, güncelleme, silme, listeleme
- JWT doğrulama ve rol bazlı güvenlik
- Redis ile token blacklist kontrolü
- RESTful API mimarisi
- RabbitMQ ile OrderCreatedEvent dinleme
- Stok kontrolü ve ürün rezervasyonu

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

---

## 📦 Order Service

Order Service, sipariş yönetimi ve event publishing işlemlerini sağlar.

### Özellikler
- Sipariş oluşturma (`createOrder`) ve listeleme
- `OrderItem` ile ürün ilişkisi
- RabbitMQ ile `OrderCreatedEvent` yayınlama
- RESTful API

### Model
- **Order**: `id`, `userId`, `totalPrice`, `status`, `items`
- **OrderItem**: `id`, `productId`, `quantity`, `price`, `order`


## 📦 Common Lib

Ortak kullanılan event ve DTO sınıfları:

- `OrderCreatedEvent`
- `OrderCreatedEvent.OrderItemDto`

---

## ⚙️ RabbitMQ

Docker üzerinden çalıştırılabilir:

```bash
docker run -d --name rabbitmq -p 5672:5672 -p 15672:15672 rabbitmq:3-management
```

- `Order Service: OrderCreatedEvent publish`

- `Product Service: OrderCreatedEvent listen`

## ⚙️ Çalıştırma

1. PostgreSQL’de servisler için ayrı veritabanları oluşturun.  
   (Örn: `identity_db`, `product_db`, `order_db`)

2. `application.properties` veya `application.yml` dosyalarında bağlantı ayarlarını yapın.

3. Redis sunucusunu başlatın (token blacklist için).

4. RabbitMQ sunucusunu Docker ile çalıştırın.

```bash
docker run -d --name rabbitmq -p 5672:5672 -p 15672:15672 rabbitmq:3-management
```

## 🚀 Servisleri Başlatma

Servisleri başlatmak için her bir servis dizininde şu komutu çalıştırın:

```bash
mvn spring-boot:run
```

## 📌 Planlanan Servisler

- **Identity Service** ✅
- **Product Service** ✅
- **Order Service** ✅
- **Common Lib** ✅
