package com.backend.yearbook.entity;

import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;

public class Notification {
    private Long id;
    private String content;
    private NotificationType notificationType;
    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    private Date createdAt;

}
