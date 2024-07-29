package pl.bolewski.credit_management.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.junit.jupiter.Testcontainers;
import pl.bolewski.credit_management.dto.BalanceDTO;
import pl.bolewski.credit_management.model.Balance;
import pl.bolewski.credit_management.repository.BalanceRepository;
import pl.bolewski.credit_management.testcontainers.TestcontainersSetup;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class BalanceServiceTest extends TestcontainersSetup {

    @Autowired
    private BalanceService balanceService;
    @Autowired
    private BalanceRepository balanceRepository;


    @BeforeEach
    void setUp() {
        balanceRepository.deleteAll();
    }

    @Test
    void addBalance() {
        Balance balance = new Balance();
        balance.setAccountId(1L);
        balance.setOkoBalance(BigDecimal.valueOf(1000));
        balance.setCreditBalance(BigDecimal.valueOf(500));

        Balance savedBalance = balanceService.addBalance(balance);

        Optional<Balance> fetchedBalance = balanceRepository.findByAccountId(1L);
        assertTrue(fetchedBalance.isPresent());
        assertEquals(0, BigDecimal.valueOf(1000).compareTo(fetchedBalance.get().getOkoBalance()));
        assertEquals(0, BigDecimal.valueOf(500).compareTo(fetchedBalance.get().getCreditBalance()));
    }

    @Test
    void getWholeBalance() {
        Balance balance = new Balance();
        balance.setAccountId(1L);
        balance.setOkoBalance(BigDecimal.valueOf(1000));
        balance.setCreditBalance(BigDecimal.valueOf(500));
        balanceRepository.save(balance);

        BalanceDTO balanceDTO = balanceService.getWholeBalance();
        assertEquals(0, BigDecimal.valueOf(1000).compareTo(balanceDTO.getOkoBalance()));
        assertEquals(0, BigDecimal.valueOf(500).compareTo(balanceDTO.getCreditBalance()));
    }
}