package com.ntd.practical_test_ntd_backend.entities;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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
public class User implements UserDetails {
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
    public User(String username, String password)
    {
        this.username = username;
        this.password = password;
        this.status = true;
    }

    @Override
    public boolean isAccountNonExpired() {
        return getStatus();
    }

    @Override
    public boolean isAccountNonLocked() {
        return getStatus();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return getStatus();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }
}