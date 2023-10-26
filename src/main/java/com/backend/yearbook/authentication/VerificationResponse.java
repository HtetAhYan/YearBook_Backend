package com.backend.yearbook.authentication;

public class VerificationResponse {
    private boolean success;
    private String error;

    public VerificationResponse(boolean success, String error) {
        this.success = success;
        this.error = error;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getError() {
        return error;
    }
}
