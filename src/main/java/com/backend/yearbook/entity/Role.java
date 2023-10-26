package com.backend.yearbook.entity;

import com.backend.yearbook.user.Permissions;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.backend.yearbook.user.Permissions.*;

@RequiredArgsConstructor
public enum Role {
    STUDENT(Collections.emptySet()),
    TEACHER(Set.of(TEACHER_UPDATE, TEACHER_DELETE, TEACHER_CREATE, TEACHER_READ)),
    ADMIN(
        Set.of(
            TEACHER_UPDATE, TEACHER_DELETE, TEACHER_CREATE, TEACHER_READ,
            ADMIN_CREATE, ADMIN_UPDATE, ADMIN_DELETE, ADMIN_READ
        ));

    @Getter
    private final Set<Permissions> permissions;

    public List<SimpleGrantedAuthority> getAuthorities() {
        var authorities = getPermissions()
            .stream()
            .map(permission -> new SimpleGrantedAuthority(permission.getPermissions()))
            .collect(Collectors.toList());
        authorities.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
        return authorities;
    }
}
