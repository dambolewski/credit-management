package pl.bolewski.creditmanagement.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class Money {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Integer id;
        @NotNull(message = "Cash amount is required")
        @DecimalMin(value = "0.01", message = "Cash amount must be at least 0.01")
        private BigDecimal cash;
        @Enumerated(EnumType.STRING)
        @Column(name = "account")
        @NotNull(message = "Account type is required")
        private AccountType accountType;
        @NotBlank(message = "Month is required")
        private String month;
        @NotBlank(message = "Year is required")
        private String year;
        private LocalDateTime addedAt;
        @Enumerated(EnumType.STRING)
        @Column(name = "transaction")
        @NotNull(message = "Transaction type is required")
        private TransactionType transactionType;
    }
