package com.ntd.practical_test_ntd_backend.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "USER")
@Table(name = "USER")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "USER_ID", nullable = false)
    private Long id;

    @Size(max = 200)
    @NotNull
    @Column(name = "USERNAME", nullable = false, length = 200)
    private String username;

    @Size(max = 200)
    @NotNull
    @Column(name = "PASSWORD", nullable = false, length = 200)
    private String password;

    @NotNull
    @Column(name = "STATUS", nullable = false)
    private Boolean status = false;

    public User(String username, String password, Boolean status)
    {
        this.username = username;
        this.password = password;
        this.status = status;
    }
}