package pl.bolewski.credit_management.dto;

import lombok.*;

import java.math.BigDecimal;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TreasuryBondDTO {
    String month;
    String year;
    BigDecimal treasuryBond;
}
