package pl.bolewski.credit_management.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.bolewski.credit_management.model.Balance;
import pl.bolewski.credit_management.repository.BalanceRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CalculatorService {

    private final BalanceRepository balanceRepository;

    public void updateBalance(Long cash, String account, String transaction) {
        Optional<Balance> balanceOptional = balanceRepository.findByAccountId(1L);
        if (balanceOptional.isPresent()) {
            Balance balance = balanceOptional.get();
            if (account.equals("oko") && transaction.equals("deposit")) {
                balance.setOkoBalance(balance.getOkoBalance() + cash);
            } else if (account.equals("credit") && transaction.equals("deposit")) {
                balance.setCreditBalance(balance.getCreditBalance() + cash);
            } else if (account.equals("oko") && transaction.equals("withdraw")) {
                balance.setOkoBalance(balance.getOkoBalance() - cash);
            } else if (account.equals("credit") && transaction.equals("withdraw")) {
                balance.setCreditBalance(balance.getCreditBalance() - cash);
            }
            balanceRepository.save(balance);
        } else {
            Balance emptyBalance = Balance.builder()
                    .accountId(1L)
                    .okoBalance(0L)
                    .creditBalance(0L)
                    .build();
            if (account.equals("oko")) {
                emptyBalance.setOkoBalance(cash);
            } else if (account.equals("credit")) {
                emptyBalance.setCreditBalance(cash);
            }
            balanceRepository.save(emptyBalance);
        }
    }
}
