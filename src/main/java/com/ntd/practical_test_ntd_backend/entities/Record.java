package com.ntd.practical_test_ntd_backend.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "RECORD")
public class Record {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "RECORD_ID", nullable = false)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "OPERATION_ID", nullable = false)
    private Operation operation;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "USER_ID", nullable = false)
    private User user;

    @NotNull
    @Column(name = "AMOUNT", nullable = false)
    private Double amount;

    @Column(name = "USER_BALANCE")
    private Double userBalance;

    @Size(max = 100)
    @NotNull
    @Column(name = "OPERATION_RESPONSE", nullable = false, length = 100)
    private String operationResponse;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "CREATION_DATE", nullable = true)
    private Instant creationDate;

    @Column(name = "DELETION_DATE")
    private Instant deletionDate;

    public Record(Operation operation, User user, Double amount, Double userBalance, String operationResponse)
    {
        this.operation = operation;
        this.user = user;
        this.amount = amount;
        this.userBalance = userBalance;
        this.operationResponse = operationResponse;
        this.creationDate = Instant.now();
    }
}