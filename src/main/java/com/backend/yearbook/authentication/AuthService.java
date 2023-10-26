package com.backend.yearbook.authentication;

import com.backend.yearbook.config.JwtService;
import com.backend.yearbook.entity.Otp;
import com.backend.yearbook.entity.Role;
import com.backend.yearbook.entity.User;
import com.backend.yearbook.model.MailModel;
import com.backend.yearbook.repo.UserRepo;
import com.backend.yearbook.service.MailService;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;


@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepo userRepo;
    private final OtpRepo otpRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final MailService mailService;

    public AuthenticationResponse register(RegisterRequest req) {
        if (userRepo.findByEmail(req.getEmail()).isPresent()) {
            // Handle the case where the user already exists.
            return AuthenticationResponse.builder()
                .token(null)
                .status("User with this email already exists")
                .error(true)
                .build();
        }

        // Create and save the new user with email verification status set to false
        User user = User.builder()
            .fullName(req.getFullName())

            .email(req.getEmail())
            .password(passwordEncoder.encode(req.getPassword()))
            .role(Role.STUDENT)
            .isEmailVerified(false) // Set email verification status to false
            .build();
        userRepo.save(user);

        // Generate OTP (you can use a library like RandomStringUtils)
        String otp = RandomStringUtils.randomNumeric(4);

        // Store the OTP in the user's record in your database
        Otp otp1=Otp.builder()
            .otpCode(otp)
            .user(user)
            .build();
    otpRepo.save(otp1);

        userRepo.save(user);

        // Send the OTP to the user's email
        return getAuthenticationResponse(otp, user);
    }

    // Add a method for email verification
    public VerificationResponse verifyEmail(String email, OtpWrapper otp) {
        String receivedOtp = otp.getOtp();

        User user = userRepo.findByEmail(email).orElse(null);
        Otp storedOtp = otpRepo.findByOtpCode(receivedOtp); // Fetch the stored OTP based on receivedOtp

        if (user != null && storedOtp != null && storedOtp.getOtpCode().equals(receivedOtp)) {
            user.setIsEmailVerified(true);
    otpRepo.deleteById(storedOtp.getId());

            userRepo.save(user);

            return new VerificationResponse(true, "Verification Success"); // Success
        } else {
            System.out.println("Received OTP: " + receivedOtp);
            System.out.println("Stored OTP: " + (storedOtp != null ? storedOtp.getOtpCode() : "null"));
            return new VerificationResponse(false, "Invalid OTP"); // Error message for an invalid or missing OTP
        }
    }



    public AuthenticationResponse authenticate(AuthenticationRequest req) {
        try {
            authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    req.getEmail(),
                    req.getPassword()
                )
            );

            var user = userRepo.findByEmail(req.getEmail()).orElseThrow();

            if (user.getIsEmailVerified()) {
                // Issue a JWT token only if the email is verified
                var jwtToken = jwtService.generateToken(user);
                return AuthenticationResponse.builder()
                    .token(jwtToken)
                    .error(false)
                    .status(user.getFullName())
                    .build();
            } else {
                return AuthenticationResponse.builder()
                    .token(null)
                    .status("Email not verified. Please verify your email.")
                    .error(false)
                    .build();
            }
        } catch (AuthenticationException e) {
            // Authentication failed, return an error response
            return AuthenticationResponse.builder()
                .token(null)
                .status("Invalid email or password. Please check your credentials.")
                .error(true)
                .build();
        }
    }

    public AuthenticationResponse requestOtp(String email) {
        User user = userRepo.findByEmail(email).orElse(null);

        if (user != null) {
            Otp existingOtp = otpRepo.findByUser(user);

            // Check if there is an unexpired OTP and it's within the cooldown period
            if (existingOtp != null &&  isWithinCooldownPeriod(existingOtp)) {

                return AuthenticationResponse.builder()
                    .token(null)
                    .status("You can request a new OTP after the cooldown 3 min period.")
                    .error(true)
                    .build();
            }else {
                assert existingOtp != null;
                otpRepo.deleteById(existingOtp.getId());
            // No unexpired OTP exists or it's outside the cooldown period; generate a new OTP
            String otp = RandomStringUtils.randomNumeric(6);
            // Store the new OTP in the user's record in your database
            Otp otp1 = Otp.builder()
                .otpCode(otp)
                .user(user)
                .build();
            otpRepo.save(otp1);

            // Send the new OTP
            return getAuthenticationResponse(otp, user);}
        } else {
            // Handle the case where the user is not found (e.g., invalid email)
            return AuthenticationResponse.builder()
                .token(null)
                .status("User not found. Please register or check your email.")
                .error(true)
                .build();
        }
    }

    private boolean isWithinCooldownPeriod(Otp otp) {
        Date currentTimestamp = new Date();
        Date lastRequestTime = otp.getCreatedAt(); // A hypothetical timestamp to track the last request
        int cooldownMinutes = 3; // Define the cooldown period (e.g., 3 minutes)

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(lastRequestTime);
        calendar.add(Calendar.MINUTE, cooldownMinutes);
        Date cooldownEndTime = calendar.getTime();

        return currentTimestamp.before(cooldownEndTime);
    }


// Rest of your code remains the same


    private AuthenticationResponse getAuthenticationResponse(String otp, User user) {
        String mail = user.getEmail();
        MailModel mailModel = new MailModel();
        mailModel.setSubject("Email Verification OTP");
        mailModel.setMessage("Your OTP for email verification is: " + otp);
        mailService.sendEmail(mail, mailModel);

        return AuthenticationResponse.builder()
            .token(null) // No token issued during registration
            .status("Successfully created. Check your email for the OTP.")
            .error(false)
            .build();
    }

    ;
}
