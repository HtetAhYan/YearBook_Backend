package com.backend.yearbook.authentication;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("/api/v1/auth")
@CrossOrigin(origins = "http://localhost:3000",allowCredentials = "true")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
@PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody RegisterRequest req){
    log.info("Received authentication request: {}", req);
return ResponseEntity.ok(authService.register(req));
};
@PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody AuthenticationRequest req){

    return ResponseEntity.ok(authService.authenticate(req));
    }
    @PostMapping("/verify/{mail}")
    public VerificationResponse sendMail(@PathVariable String mail, @RequestBody OtpWrapper otp) {
        return authService.verifyEmail(mail, otp);
    }
@PostMapping("/register/request-otp/{email}")
    public AuthenticationResponse requestOtp(@PathVariable String email){
    return authService.requestOtp(email);
}
}
