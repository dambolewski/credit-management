package pl.bolewski.creditmanagement.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.bolewski.creditmanagement.dto.TreasuryBondDTO;
import pl.bolewski.creditmanagement.model.AccountType;
import pl.bolewski.creditmanagement.model.Money;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TreasuryBondService {

    private final MoneyService moneyService;

    public BigDecimal calculateDepositedTreasuryBond() {
        return moneyService.getMoneyByAccountType(AccountType.TREASURY_BOND)
                .stream()
                .map(Money::getCash)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public List<TreasuryBondDTO> getTreasuryBondList() {
        return moneyService.getMoneyByAccountType(AccountType.TREASURY_BOND)
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
