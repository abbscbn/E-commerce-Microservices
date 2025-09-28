package com.abbas.ecommerce.identity.controller;
import com.abbas.ecommerce.common.response.RootResponse;
import com.abbas.ecommerce.identity.dto.LoginRequest;
import com.abbas.ecommerce.identity.dto.LoginResponse;
import com.abbas.ecommerce.identity.dto.RegisterRequest;
import com.abbas.ecommerce.identity.jwt.JwtUtil;
import com.abbas.ecommerce.identity.services.TokenBlacklistService;
import com.abbas.ecommerce.identity.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;


@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {


    private final UserService userService;

    private final JwtUtil jwtUtil;

    private final TokenBlacklistService tokenBlacklistService;



    @PostMapping("/register")
    public ResponseEntity<RootResponse<String>> register(@Valid @RequestBody RegisterRequest request, WebRequest webRequest) {

        String response = userService.registerUser(request.username(), request.email(), request.password());
        return ResponseEntity.ok(RootResponse.ok(response,webRequest));
    }

    @PostMapping("/login")
    public ResponseEntity<RootResponse<LoginResponse>> login(@Valid @RequestBody LoginRequest request, WebRequest webRequest) {

        LoginResponse loginResponse = userService.login(request);
        return ResponseEntity.ok(RootResponse.ok(loginResponse,webRequest));
    }


    @GetMapping("/test")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> test(){
        return ResponseEntity.ok("JWT testten başarıyla geçti");
    }

    @PostMapping("/logout")
    public ResponseEntity<RootResponse<String>> logout(HttpServletRequest request, WebRequest webRequest) {
        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            long expiration = jwtUtil.getExpirationMillis(token);
            tokenBlacklistService.blacklistToken(token, expiration);
        }

        return ResponseEntity.ok(RootResponse.ok("Çıkış Yapıldı",webRequest));
    }

    @PostMapping("/admin")
    public ResponseEntity<RootResponse<LoginResponse>> adminLogin(@Valid @RequestBody LoginRequest request, WebRequest webRequest){
        return ResponseEntity.ok(RootResponse.ok(userService.adminLogin(request),webRequest));
    }
}

