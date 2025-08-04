package pl.bolewski.credit_management.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.bolewski.credit_management.dto.TreasuryBondDTO;
import pl.bolewski.credit_management.model.AccountType;
import pl.bolewski.credit_management.model.Balance;
import pl.bolewski.credit_management.model.Money;
import pl.bolewski.credit_management.model.TransactionType;
import pl.bolewski.credit_management.repository.BalanceRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

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
