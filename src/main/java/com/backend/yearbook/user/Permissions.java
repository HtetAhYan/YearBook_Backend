package com.backend.yearbook.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Permissions {
    ADMIN_READ("admin:read"),
    ADMIN_UPDATE("admin:update"),
    ADMIN_CREATE("admin:create"),
    ADMIN_DELETE("admin:delete"),
     TEACHER_READ  ("teacher:read"),
     TEACHER_UPDATE("teacher:update"),
     TEACHER_CREATE("teacher:create"),
     TEACHER_DELETE("teacher:delete");
        @Getter
  private final String permissions;
}
