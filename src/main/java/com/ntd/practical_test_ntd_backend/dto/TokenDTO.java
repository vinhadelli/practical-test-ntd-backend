package com.ntd.practical_test_ntd_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TokenDTO {
    private String token;

    private long expiresIn;
}
