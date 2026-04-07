package pl.bolewski.creditmanagement.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.bolewski.creditmanagement.model.Balance;
import pl.bolewski.creditmanagement.dto.BalanceDTO;
import pl.bolewski.creditmanagement.repository.BalanceRepository;

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
    public BalanceDTO getBalanceDto() {
        return balanceRepository.findByAccountId(1L)
                .map(this::toBalanceDTO)
                .orElseGet(this::createEmptyBalanceDTO);
    }

    @Transactional(readOnly = true)
    public Balance getBalance() {
        return balanceRepository.findByAccountId(1L)
                .orElseGet(() -> createNewBalance(1L));
    }

    @Transactional(readOnly = true)
    public BigDecimal getCombinedBalance() {
        BalanceDTO balanceDto = balanceRepository.findByAccountId(1L)
                .map(this::toBalanceDTO)
                .orElseGet(this::createEmptyBalanceDTO);
        return balanceDto.getCreditBalance().add(balanceDto.getOkoBalance());
    }

    private Balance updateExistingBalance(Balance existingBalance, Balance balance) {
        existingBalance.setOkoBalance(balance.getOkoBalance());
        existingBalance.setCreditBalance(balance.getCreditBalance());
        return balanceRepository.save(existingBalance);
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

    private Balance createNewBalance(long accountId) {
        return Balance.builder()
                .accountId(accountId)
                .okoBalance(BigDecimal.ZERO)
                .creditBalance(BigDecimal.ZERO)
                .build();
    }
}
