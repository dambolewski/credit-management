package pl.bolewski.credit_management.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class YearlyVerifierDTO {
    private String year;
    private BigDecimal needed;
    private BigDecimal collected;
    private BigDecimal status;
}
