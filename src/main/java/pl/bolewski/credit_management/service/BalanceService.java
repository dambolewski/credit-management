package pl.bolewski.credit_management.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.bolewski.credit_management.model.Balance;
import pl.bolewski.credit_management.dto.BalanceDTO;
import pl.bolewski.credit_management.repository.BalanceRepository;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class BalanceService {

    private final BalanceRepository balanceRepository;

    @Transactional
    public void addBalance(Balance balance) {
        balanceRepository.findByAccountId(balance.getAccountId())
                .map(exisitingBalance -> updateExistingBalance(exisitingBalance, balance))
                .orElseGet(() -> balanceRepository.save(balance));
    }

    @Transactional(readOnly = true)
    public BalanceDTO getWholeBalance() {
        return balanceRepository.findByAccountId(1L)
                .map(this::toBalanceDTO)
                .orElseGet(this::createEmptyBalanceDTO);
    }



    private Balance updateExistingBalance(Balance exisitingBalance, Balance balance) {
        exisitingBalance.setOkoBalance(balance.getOkoBalance());
        exisitingBalance.setCreditBalance(balance.getCreditBalance());
        return balanceRepository.save(exisitingBalance);
    }

    private BalanceDTO toBalanceDTO(Balance balance) {
        return BalanceDTO.builder()
                .creditBalance(balance.getCreditBalance())
                .okoBalance(balance.getOkoBalance())
                .build();
    }

    private BalanceDTO createEmptyBalanceDTO() {
        return BalanceDTO.builder()
                .creditBalance(BigDecimal.ZERO)
                .okoBalance(BigDecimal.ZERO)
                .build();
    }
}
