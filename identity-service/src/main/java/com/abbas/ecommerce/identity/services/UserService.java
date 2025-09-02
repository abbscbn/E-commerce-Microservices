package com.abbas.ecommerce.identity.services;

import com.abbas.ecommerce.common.exception.BaseException;
import com.abbas.ecommerce.common.exception.ErrorMessage;
import com.abbas.ecommerce.common.exception.ErrorMessageType;
import com.abbas.ecommerce.identity.dto.LoginRequest;
import com.abbas.ecommerce.identity.enumtype.Role;
import com.abbas.ecommerce.identity.jwt.JwtUtil;
import com.abbas.ecommerce.identity.model.User;
import com.abbas.ecommerce.identity.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    private final JwtUtil jwtUtil;


    public User registerUser(String username, String email, String password){

        if(userRepository.existsByUsername(username)){
            throw new BaseException(new ErrorMessage(ErrorMessageType.USERNAME_ALREADY_EXIST,username)); ///ileride düzenlenecek....
        }

        if(userRepository.existsByEmail(email)){
            throw new BaseException(new ErrorMessage(ErrorMessageType.EMAIL_ALREADY_EXIST,email));
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

    public String login(LoginRequest request){

        Optional<User> optUsername = userRepository.findByUsername(request.username());

        if(optUsername.isEmpty()){
            throw new BaseException(new ErrorMessage(ErrorMessageType.USER_OR_PASSWORD_INCORECT, request.username()));

        }

        if(!passwordEncoder.matches(request.password(),optUsername.get().getPassword())){

            throw new BaseException(new ErrorMessage(ErrorMessageType.USER_OR_PASSWORD_INCORECT, request.username()));
        }


        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.username(), request.password())
        );


        UserDetails userDetails = (UserDetails) authentication.getPrincipal();


        String token = jwtUtil.generateToken(userDetails.getUsername(),
                userDetails.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.toSet()));




        return token;
    }






}
