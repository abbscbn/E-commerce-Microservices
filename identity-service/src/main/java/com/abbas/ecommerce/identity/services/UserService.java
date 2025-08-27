package com.abbas.ecommerce.identity.services;

import com.abbas.ecommerce.identity.enumtype.Role;
import com.abbas.ecommerce.identity.model.User;
import com.abbas.ecommerce.identity.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;


    public User registerUser(String username, String email, String password){

        if(userRepository.existsByUsername(username)){
            throw new RuntimeException("username already exist"); ///ileride düzenlenecek....
        }

        if(userRepository.existsByEmail(email)){
            throw new RuntimeException("email already exist");
        }

        User user= new User();

        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));

        // bütün herkes varsayılan olarak USER rolünde kaydedilir..
        Set<Role> roles= new HashSet<>();
        roles.add(Role.USER);
        user.setRoles(roles);

        User savedUser = userRepository.save(user);

        return savedUser;

    }


}
