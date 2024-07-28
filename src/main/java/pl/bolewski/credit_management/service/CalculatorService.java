package pl.bolewski.credit_management.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.bolewski.credit_management.model.Balance;
import pl.bolewski.credit_management.model.Money;
import pl.bolewski.credit_management.repository.BalanceRepository;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CalculatorService {

    private final BalanceRepository balanceRepository;

    public void updateBalance(BigDecimal cash, String account, String transaction) {
        Balance balance = balanceRepository.findByAccountId(1L).orElseGet(() -> createNewBalance(1L));
        updateAccountBalance(balance, cash, account, transaction);
        balanceRepository.save(balance);
    }

    private Balance createNewBalance(long accountId) {
        return Balance.builder()
                .accountId(accountId)
                .okoBalance(BigDecimal.ZERO)
                .creditBalance(BigDecimal.ZERO)
                .build();
    }

    private void updateAccountBalance(Balance balance, BigDecimal cash, String account, String transaction) {
        switch (account) {
            case "oko" -> {
                if ("deposit".equals(transaction)) {
                    balance.setOkoBalance(balance.getOkoBalance().add(cash));
                } else if ("withdraw".equals(transaction)) {
                    balance.setOkoBalance(balance.getOkoBalance().subtract(cash));
                }
            }
            case "credit" -> {
                if ("deposit".equals(transaction)) {
                    balance.setCreditBalance(balance.getCreditBalance().add(cash));
                } else if ("withdraw".equals(transaction)) {
                    balance.setCreditBalance(balance.getCreditBalance().subtract(cash));
                }
            }
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
