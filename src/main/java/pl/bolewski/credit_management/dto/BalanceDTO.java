package pl.bolewski.credit_management.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class BalanceDTO {
    private Long okoBalance;
    private Long creditBalance;
}
