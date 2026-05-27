package pl.bolewski.creditmanagement.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.bolewski.creditmanagement.model.AccountType;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MoneyDTO {
    @NotNull(message = "Cash amount is required")
    @DecimalMin(value = "0.01", message = "Cash amount must be at least 0.01")
    private BigDecimal cash;
    @NotNull(message = "Account type is required")
    private AccountType accountType;
    @NotBlank(message = "Month is required")
    private String month;
}