package com.backend.yearbook.authentication;

import com.backend.yearbook.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterRequest {
    public Role role;
private String fullName;
    private String email;
    private String password;
}
