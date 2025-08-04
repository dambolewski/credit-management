package pl.bolewski.credit_management.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.bolewski.credit_management.model.AccountType;
import pl.bolewski.credit_management.model.Balance;
import pl.bolewski.credit_management.model.Money;
import pl.bolewski.credit_management.model.TransactionType;
import pl.bolewski.credit_management.repository.BalanceRepository;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CalculatorService {

    private final BalanceRepository balanceRepository;
    private final BalanceService balanceService;

    @Transactional
    public void updateBalance(BigDecimal cash, AccountType accountType, TransactionType transactionType) {
        Balance balance = balanceService.getBalance();
        updateAccountBalance(balance, cash, accountType, transactionType);
        balanceRepository.save(balance);
    }

    private void updateAccountBalance(Balance balance, BigDecimal cash, AccountType accountType, TransactionType transactionType) {
        BigDecimal newAmount = switch (transactionType) {
            case DEPOSIT -> cash;
            case WITHDRAW -> cash.negate();
        };

        switch (accountType) {
            case OKO, TREASURY_BOND -> balance.setOkoBalance(balance.getOkoBalance().add(newAmount));
            case CREDIT -> balance.setCreditBalance(balance.getCreditBalance().add(newAmount));
        }
    }

    public BigDecimal calculateMoneyInsideList(List<Money> list) {
        BigDecimal result = BigDecimal.ZERO;
        for (Money money : list) {
            BigDecimal amount = switch (money.getTransactionType()) {
                case DEPOSIT -> money.getCash();
                case WITHDRAW -> money.getCash().negate();
            };
            result = result.add(amount);
        }
        return result;
    }
}
