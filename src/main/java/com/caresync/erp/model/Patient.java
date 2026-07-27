package com.caresync.erp.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(
        name = "patients",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "email"),
                @UniqueConstraint(columnNames = "phone"),
                @UniqueConstraint(columnNames = "user_id")
        }
)
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Patient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false,length = 100)
    private String name;

    @Column(nullable = false)
    private Integer age;

    @Column(nullable = false,length = 10)
    private String gender;

    @Column(nullable = false,length = 15)
    private String phone;

    @Column(nullable = false,length = 150)
    private String email;

    @Column(length = 255)
    private String address;

    @Column(nullable = false)
    private Boolean active;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = true)
    private User user;


    private String bloodGroup;
    private String emergencyContact;
    private String emergencyName;
    private String medicalHistory;
    private String allergies;
    private String maritalStatus;
    private String occupation;
    private String insuranceProvider;
    private String insuranceNumber;

    @Column(updatable = false)
    private LocalDate registrationDate;

    @PrePersist
    protected void onCreate() {
        registrationDate = LocalDate.now();
    }
}
