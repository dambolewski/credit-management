package pl.bolewski.creditmanagement.model;

import jakarta.persistence.*;
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
        private BigDecimal cash;
        @Enumerated(EnumType.STRING)
        @Column(name = "account")
        private AccountType accountType;
        private String month;
        private String year;
        private LocalDateTime addedAt;
        @Enumerated(EnumType.STRING)
        @Column(name = "transaction")
        private TransactionType transactionType;
    }
