package pl.bolewski.credit_management.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.bolewski.credit_management.model.Balance;
import pl.bolewski.credit_management.model.Money;
import pl.bolewski.credit_management.repository.BalanceRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CalculatorService {

    private final BalanceRepository balanceRepository;

    public void updateBalance(BigDecimal cash, String account, String transaction) {
        Optional<Balance> balanceOptional = balanceRepository.findByAccountId(1L);
        if (balanceOptional.isPresent()) {
            Balance balance = balanceOptional.get();
            if (account.equals("oko") && transaction.equals("deposit")) {
                balance.setOkoBalance(balance.getOkoBalance().add(cash));
            } else if (account.equals("credit") && transaction.equals("deposit")) {
                balance.setCreditBalance(balance.getCreditBalance().add(cash));
            } else if (account.equals("oko") && transaction.equals("withdraw")) {
                balance.setOkoBalance(balance.getOkoBalance().subtract(cash));
            } else if (account.equals("credit") && transaction.equals("withdraw")) {
                balance.setCreditBalance(balance.getCreditBalance().subtract(cash));
            }
            balanceRepository.save(balance);
        } else {
            Balance emptyBalance = Balance.builder()
                    .accountId(1L)
                    .okoBalance(BigDecimal.valueOf(0))
                    .creditBalance(BigDecimal.valueOf(0))
                    .build();
            if (account.equals("oko")) {
                emptyBalance.setOkoBalance(cash);
            } else if (account.equals("credit")) {
                emptyBalance.setCreditBalance(cash);
            }
            balanceRepository.save(emptyBalance);
        }
    }

    public BigDecimal calculateMoneyInsideList(List<Money> list) {
        BigDecimal addedCash = BigDecimal.ZERO;
        BigDecimal withdrewCash = BigDecimal.ZERO;
        for (Money money : list) {
            if (money.getTransaction().equals("deposit"))
                addedCash = addedCash.add(money.getCash());
            else
                withdrewCash = withdrewCash.add(money.getCash());
        }
        return addedCash.subtract(withdrewCash);
    }
}
