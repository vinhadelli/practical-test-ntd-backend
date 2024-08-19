package com.ntd.practical_test_ntd_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.time.Instant;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Component
public class RecordDTO {
    private Long id;
    private String operation;
    private Double amount;
    private Double userBalance;
    private String result;
    private Instant date;
}
