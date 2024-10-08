package pl.bolewski.credit_management.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MonthVerifierDTO {
    private String month;
    private BigDecimal needed;
    private BigDecimal collected;
    private BigDecimal status;
}
