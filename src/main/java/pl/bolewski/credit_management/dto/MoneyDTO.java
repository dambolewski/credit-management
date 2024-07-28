package pl.bolewski.credit_management.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MoneyDTO {
    private BigDecimal cash;
    private String account;
    private String month;
}
