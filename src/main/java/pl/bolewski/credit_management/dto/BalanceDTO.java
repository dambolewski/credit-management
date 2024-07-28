package pl.bolewski.credit_management.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Builder
@Getter
@Setter
public class BalanceDTO {
    private BigDecimal okoBalance;
    private BigDecimal creditBalance;
}
