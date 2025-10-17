package com.BTL_JAVA.BTL.Entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@Table(name = "review")

public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    User user;

    @Column(columnDefinition = "TEXT")
    String comment;

    LocalDateTime createdAt;

    @PrePersist
    protected void onCreat() {
        createdAt = LocalDateTime.now();
    }
}
