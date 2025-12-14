package com.abbas.ecommerce.identity.model;


import com.abbas.ecommerce.identity.enumtype.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "user")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false,unique = true)
    private String username;

    @Email
    @Column(nullable = false,unique = true)
    private String email;

    @Column(nullable = false)
    private String password;


    @ElementCollection(fetch = FetchType.EAGER)  //ElementCollection da fetch yaptığımızda bu gidip getirmek anlamı taşır ve bu FetchType.EAGER yaparsak o nesne döndüğün de içinde rollerle beraber getir demiş oluruz
    @CollectionTable(name = "user_roles",joinColumns = @JoinColumn(name = "user_id")) //CollectionTable da name oluşturulacak tablonun adı olur, joinColums dediğimizde de toblo içerisinde oluşturulacak kolon adı olur
    @Column(name = "role") // oluşturulan tabloda role kolonu oluşturmak için
    @Enumerated(EnumType.STRING) // hibernet Enum veriyi String olarak DB ye kayıt etmesi için yapar
    private Set<Role> roles;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        //bu methodun geri dönüş tipi GrantedAuthority classını extend etmiş classların set, list (colleciton) halinede geri döneceğini söyler
        // hasRole("USER") kullanacaksan => "ROLE_USER" formatı gerekir
        return roles == null ? Set.of()
                : roles.stream()
                .map(r -> new SimpleGrantedAuthority("ROLE_" + r.name()))//Spring Security mantığı olarak ROLE_ADMİN veya ROLE_USER olarak setlenmesi lazım ki biz hasRole("ADMİN") olarak kullanabilelim gerekli prefix yani
                .collect(Collectors.toUnmodifiableSet());// buradaki toUnModifiableSet ise değiştirilemez bir küme verdiğini söyler sonrasında manupüle edilmemesi için
    }
}
