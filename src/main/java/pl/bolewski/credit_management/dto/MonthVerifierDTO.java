package pl.bolewski.credit_management.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MonthVerifierDTO {
    private String month;
    private Long status;
}
