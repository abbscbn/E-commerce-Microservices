# 🛒 E-commerce Microservices

Bu proje, **Spring Boot** tabanlı mikroservis mimarisi ile geliştirilmiş bir e-ticaret uygulamasıdır.  
Her servis bağımsız olarak çalışır ve ortak bir iletişim yapısı ile haberleşir (örn: RabbitMQ).

## 📂 Proje Yapısı

```plaintext
E-commerce-Microservices/
│── identity-service/      → Kullanıcı yönetimi (Register, Login, Roles)
│── ... (diğer servisler eklenecek)
│── pom.xml                → Parent Maven pom
│── README.md              → Bu dosya
```
## 🔑 Identity Service (İlk Servis)

Identity Service, kullanıcı yönetimini sağlar.
- Kullanıcı kayıt (Register)
- Kullanıcı girişi (Login)
- Rol yönetimi (varsayılan: `USER`)

### 📦 Kullanılan Teknolojiler
- **Java 17**
- **Spring Boot 3**
- **Spring Data JPA (Hibernate)**
- **Spring Validation**
- **Spring Web**
- **Spring Security** (ileride eklenecek)
- **PostgreSQL**
- **Redis** (session/token cache için, ileride)

### 📌 Model
- `User`: id, username, email, password, roles
- `Role`: `USER`, `ADMIN`

### 📌 DTO’lar
- `RegisterRequest`
- `LoginRequest`

## ⚙️ Çalıştırma

1. PostgreSQL’de Identity Service için bir veritabanı oluşturun.
2. `application.properties veya application.yml` dosyasında bağlantı ayarlarını yapın.
3. Servisi başlatın:
   ```bash
   mvn spring-boot:run
   ```
4. Sağlık kontrolü için:
```http://localhost:8080/actuator/health```

## 📌 Planlanan Servisler

- **Identity Service** ✅
- **Product Service** ⏳
- **Order Service** ⏳  