# 🛍️ E-Commerce Backend (Microservice Architecture)

Bu proje, Spring Boot ve mikroservis mimarisi kullanılarak geliştirilmiş bir **e-ticaret backend** uygulamasıdır.  
Kullanıcı kimlik doğrulama, ürün yönetimi ve sipariş işlemleri bağımsız servisler olarak tasarlanmıştır.  
Tüm servisler PostgreSQL veritabanlarını kullanır ve birbirleriyle **RabbitMQ** mesaj kuyruğu üzerinden iletişim kurar.

---

## 🧩 Mimarinin Genel Yapısı

Proje toplamda **1 ortak modül** ve **3 bağımsız servis**ten oluşur:

### 1. `common-lib` (Ortak Kütüphane Modülü)
Tüm servisler tarafından kullanılan ortak yapıları barındırır.  
Aşağıdaki paketlerden oluşur:
- `dto`
- `event`
- `exception`
- `handler`
- `response`

#### 🧱 RootResponse Yapısı
Tüm servislerin frontend’e döndüğü yanıtlar, `RootResponse<T>` generic yapısı üzerinden standartlaştırılmıştır.

**Amaç:**  
Hatalar, başarılı/başarısız sonuçlar, path bilgisi, host adı, timestamp ve hata detaylarını tek tip JSON formatında sunmaktır.

Örnek başarılı yanıt:
```json
{
  "result": true,
  "status": 200,
  "path": "/api/products",
  "hostName": "local",
  "localDateTime": "2025-10-01T12:34:56",
  "data": { ... }
}
```

---

### 2. `identity-service`
Kullanıcı kayıt, giriş ve çıkış işlemlerini yöneten servistir.

#### 🔐 Özellikler:
- JWT tabanlı kimlik doğrulama
- Token içerikleri: `username`, `roles`, `issuedAt`, `expiration`
- Redis tabanlı **token blacklisting (logout sonrası engelleme)**
- Uygulama başlatıldığında otomatik **Admin kullanıcısı** oluşturulur.
- Yeni kayıt olan kullanıcılar `USER` rolüyle kaydedilir.
- Token kontrolleri her serviste yapılır:
   - `null` token
   - `expired` token
   - `invalid` token

Bu hatalar `RootResponse` yapısıyla kullanıcıya bildirilir.

#### 👤 User Entity
Her kullanıcı `username`, `email`, `password` ve `roles` alanlarını içerir.  
Roller `USER` ve `ADMIN` olarak tanımlanmıştır.

---

### 3. `product-service`
Ürün oluşturma, güncelleme, silme ve listeleme işlemlerini yönetir.

#### 📦 Özellikler:
- `GET` işlemleri haricindeki tüm endpointler token doğrulaması ister.
- Ürün ve ürün görselleri `Product` ve `ProductImage` entity’leriyle ilişkilidir.
- Her ürünün tek bir görseli bulunur (`@OneToOne` ilişki).

---

### 4. `order-service`
Kullanıcıların sepet işlemleri ve sipariş oluşturma süreçlerini yönetir.

#### 🛒 Sepet (OrderBasket)
Kullanıcı bazlı sepet kayıtlarını tutar.  
Sepetteki ürünler `OrderBasketItem` entity’sinde tutulur.

#### 🧾 Sipariş (Order)
- Sipariş oluşturulduğunda `status = PENDING` olarak başlar.
- `RabbitMQ` üzerinden `identity-service` ve `product-service`’e mesaj gönderilir:
   - Kullanıcı doğrulaması
   - Ürün stok kontrolü
- Eğer her iki servis de onay dönerse:
   - `status = CONFIRMED`
- Eğer bir servis hata dönerse:
   - `status = FAILED`
- Hatalı mesajlar `FailedMessage` tablosunda saklanır.

---

## ⚙️ Kullanılan Teknolojiler

| Teknoloji | Açıklama |
|------------|-----------|
| **Spring Boot** | Servislerin temel çatısı |
| **Spring Data JPA (Hibernate)** | ORM ve veritabanı yönetimi |
| **Spring Security** | Kimlik doğrulama ve rol yönetimi |
| **Redis** | Token blacklisting için cache yapısı |
| **RabbitMQ** | Servisler arası iletişim için mesaj kuyruğu |
| **PostgreSQL** | Her servis için ayrı veritabanı |
| **Lombok** | Boilerplate kodların azaltılması |
| **Jakarta Validation** | Veri doğrulama işlemleri |

---

## 🏗️ Projeyi Çalıştırma

### Gereksinimler
- Java 17+
- Maven
- PostgreSQL
- Redis
- RabbitMQ

### Adımlar
1. Her servis için `.env` veya `application.yml` dosyalarında bağlantı bilgilerini düzenle:
   ```yaml
   spring:
     datasource:
       url: jdbc:postgresql://localhost:5432/identity_db
       username: postgres
       password: your_password
   ```
2. Ortak modülü (`common-lib`) build et:
   ```bash
   cd common-lib
   mvn clean install
   ```
3. Servisleri sırasıyla çalıştır:
   ```bash
   cd identity-service
   mvn spring-boot:run
   cd ../product-service
   mvn spring-boot:run
   cd ../order-service
   mvn spring-boot:run
   ```

---

## 🌐 Servislerin Default Portları

| Servis | Port | Açıklama |
|--------|------|-----------|
| common-lib | (kütüphane modülü) | bağımsız çalışmaz |
| identity-service | 8081 | Kullanıcı yönetimi ve JWT işlemleri |
| product-service | 8082 | Ürün CRUD işlemleri |
| order-service | 8083 | Sipariş ve sepet yönetimi |

---

## 📡 İletişim Akışı (RabbitMQ)
```
order-service  →  identity-service  ✅ Kullanıcı doğrulama
order-service  →  product-service   ✅ Stok kontrolü
```
Duruma göre siparişin `status` değeri güncellenir (`CONFIRMED` / `FAILED`).

---

## 🧰 Geliştirici Notları
- Tüm exception’lar `common-lib` altındaki global handler tarafından yakalanır.
- Frontend’e dönen tüm yanıtlar `RootResponse` formatındadır.
- Modüler mimari sayesinde yeni servis eklemek oldukça kolaydır.
- Her servis kendi `application.yml` dosyasında ayrı PostgreSQL bağlantısı kullanır.

---

## 🚀 Gelecekte Planlanan Geliştirmeler
- Mail doğrulama sistemi
- Ürün yorum sistemi
- Sipariş geçmişi için kullanıcı paneli
- Prometheus & Grafana ile servis monitoring

---

## 👨‍💻 Geliştirici
**Abbas Çoban**  
Full-Stack Developer (Spring Boot / React / PostgreSQL)
