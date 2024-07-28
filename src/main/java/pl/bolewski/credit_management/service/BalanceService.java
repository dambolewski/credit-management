package pl.bolewski.credit_management.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.bolewski.credit_management.model.Balance;
import pl.bolewski.credit_management.dto.BalanceDTO;
import pl.bolewski.credit_management.repository.BalanceRepository;

import java.math.BigDecimal;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BalanceService {

    private final BalanceRepository balanceRepository;

    public Balance addBalance(Balance balance) {
        Optional<Balance> balanceOptional = balanceRepository.findByAccountId(balance.getAccountId());
        if (balanceOptional.isPresent()) {
            Balance updateBalance = balanceOptional.get();
            updateBalance.setOkoBalance(balance.getOkoBalance());
            updateBalance.setCreditBalance(balance.getCreditBalance());
            return balanceRepository.save(updateBalance);
        } else
            return balanceRepository.save(balance);
    }

    public BalanceDTO getWholeBalance() {
        Optional<Balance> balanceOptional = balanceRepository.findByAccountId(1L);
        if (balanceOptional.isPresent()) {
            Balance balance = balanceOptional.get();
            return BalanceDTO.builder()
                    .creditBalance(balance.getCreditBalance())
                    .okoBalance(balance.getOkoBalance())
                    .build();
        } else
            return BalanceDTO.builder()
                    .creditBalance(BigDecimal.valueOf(0))
                    .okoBalance(BigDecimal.valueOf(0))
                    .build();
    }
}
