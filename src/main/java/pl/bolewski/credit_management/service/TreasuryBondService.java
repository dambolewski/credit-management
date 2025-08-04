package pl.bolewski.credit_management.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.bolewski.credit_management.dto.TreasuryBondDTO;
import pl.bolewski.credit_management.model.AccountType;
import pl.bolewski.credit_management.model.Money;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TreasuryBondService {

    private final MoneyService moneyService;

    public BigDecimal calculateDepositedTreasuryBond() {
        return moneyService.getMoneyByAccountType(AccountType.TREASURY_BOND)
                .orElse(List.of())
                .stream()
                .map(Money::getCash)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public List<TreasuryBondDTO> getTreasuryBondList() {
        return moneyService.getMoneyByAccountType(AccountType.TREASURY_BOND)
                .orElse(List.of())
                .stream()
                .map(this::mapToTreasuryBondDTO)
                .toList();
    }

    private TreasuryBondDTO mapToTreasuryBondDTO(Money money) {
        return TreasuryBondDTO.builder()
                .month(money.getMonth())
                .year(money.getYear())
                .treasuryBond(money.getCash())
                .build();
    }
}
