package pl.bolewski.creditmanagement.dto;

import lombok.*;

import java.math.BigDecimal;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TreasuryBondDTO {
    private String month;
    private String year;
    private BigDecimal treasuryBond;
}
