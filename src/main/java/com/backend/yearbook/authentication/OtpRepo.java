package com.backend.yearbook.authentication;

import com.backend.yearbook.entity.Otp;
import com.backend.yearbook.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OtpRepo extends JpaRepository<Otp,Long> {

    Otp findByOtpCode(String receivedOtp);

    Otp findByUser(User user);
}
