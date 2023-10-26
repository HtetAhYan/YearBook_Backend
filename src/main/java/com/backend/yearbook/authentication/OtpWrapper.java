package com.backend.yearbook.authentication;

import com.fasterxml.jackson.annotation.JsonProperty;

public class OtpWrapper {
    private String otp;

    @JsonProperty("otp")
    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }
}
