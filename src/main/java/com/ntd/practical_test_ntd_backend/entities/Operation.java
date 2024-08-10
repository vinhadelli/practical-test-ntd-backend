package com.ntd.practical_test_ntd_backend.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "OPERATION")
public class Operation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "OPERATION_ID", nullable = false)
    private Long id;

    @NotNull
    @Column(name = "TYPE", nullable = false)
    private int type;

    @NotNull
    @Column(name = "COST", nullable = false)
    private Double cost;

}