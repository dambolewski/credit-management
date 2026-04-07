package pl.bolewski.creditmanagement.dto;

import lombok.*;

import java.math.BigDecimal;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BalanceDTO {
    private BigDecimal okoBalance;
    private BigDecimal creditBalance;
}
