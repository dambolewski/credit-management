package pl.bolewski.creditmanagement.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Balance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull(message = "Account ID is required")
    private Long accountId;
    @NotNull(message = "OKO balance is required")
    private BigDecimal okoBalance;
    @NotNull(message = "Credit balance is required")
    private BigDecimal creditBalance;
}
