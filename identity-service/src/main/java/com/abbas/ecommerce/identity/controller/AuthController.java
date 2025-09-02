package com.abbas.ecommerce.identity.controller;
import com.abbas.ecommerce.identity.dto.LoginRequest;
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


@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {


    private final UserService userService;

    private final JwtUtil jwtUtil;

    private final TokenBlacklistService tokenBlacklistService;



    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody RegisterRequest request) {
        userService.registerUser(request.username(),request.email(),request.password());
        return ResponseEntity.ok("Kayıt başarılı!");
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@Valid @RequestBody LoginRequest request) {
        String token = userService.login(request);
        return ResponseEntity.ok(token);
    }


    @GetMapping("/test")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> test(){
        return ResponseEntity.ok("JWT testten başarıyla geçti");
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            long expiration = jwtUtil.getExpirationMillis(token);
            tokenBlacklistService.blacklistToken(token, expiration);
        }

        return ResponseEntity.ok("Logged out successfully");
    }
}

