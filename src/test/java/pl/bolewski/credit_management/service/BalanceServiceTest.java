package pl.bolewski.credit_management.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import pl.bolewski.credit_management.dto.BalanceDTO;
import pl.bolewski.credit_management.model.Balance;
import pl.bolewski.credit_management.repository.BalanceRepository;
import pl.bolewski.credit_management.testcontainers.TestcontainersSetup;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DirtiesContext
class BalanceServiceTest extends TestcontainersSetup {

    private final BalanceService balanceService;
    private final BalanceRepository balanceRepository;

    @Autowired
    BalanceServiceTest(BalanceService balanceService, BalanceRepository balanceRepository) {
        this.balanceService = balanceService;
        this.balanceRepository = balanceRepository;
    }


    @BeforeEach
    void setUp() {
        balanceRepository.deleteAll();
    }

    @Nested
    class addBalance {

        @Test
        void createsBalance() {
            Balance balance = new Balance();
            balance.setAccountId(1L);
            balance.setOkoBalance(BigDecimal.valueOf(1000));
            balance.setCreditBalance(BigDecimal.valueOf(500));

            balanceService.addBalance(balance);

            Optional<Balance> fetchedBalance = balanceRepository.findByAccountId(1L);
            assertTrue(fetchedBalance.isPresent());
            assertEquals(0, BigDecimal.valueOf(1000).compareTo(fetchedBalance.get().getOkoBalance()));
            assertEquals(0, BigDecimal.valueOf(500).compareTo(fetchedBalance.get().getCreditBalance()));
        }

        @Test
        void createsNewWhenAlreadyExist() {
            Balance existingBalance = new Balance();
            existingBalance.setAccountId(1L);
            existingBalance.setOkoBalance(BigDecimal.valueOf(500));
            existingBalance.setCreditBalance(BigDecimal.valueOf(250));
            balanceRepository.save(existingBalance);

            Balance newBalance = new Balance();
            newBalance.setAccountId(1L);
            newBalance.setOkoBalance(BigDecimal.valueOf(1000));
            newBalance.setCreditBalance(BigDecimal.valueOf(500));

            balanceService.addBalance(newBalance);

            Optional<Balance> fetchedBalance = balanceRepository.findByAccountId(1L);
            assertTrue(fetchedBalance.isPresent());
            assertEquals(0, BigDecimal.valueOf(1000).compareTo(fetchedBalance.get().getOkoBalance()));
            assertEquals(0, BigDecimal.valueOf(500).compareTo(fetchedBalance.get().getCreditBalance()));
        }

        @Test
        void returnsEmptyWhenNoBalance() {
            BalanceDTO balanceDTO = balanceService.getWholeBalance();
            assertEquals(BigDecimal.ZERO, balanceDTO.getOkoBalance());
            assertEquals(BigDecimal.ZERO, balanceDTO.getCreditBalance());
        }

    }

    @Nested
    class getWholeBalance {
        @Test
        void returnsWhenBalance() {
            Balance balance = new Balance();
            balance.setAccountId(1L);
            balance.setOkoBalance(BigDecimal.valueOf(1000));
            balance.setCreditBalance(BigDecimal.valueOf(500));
            balanceRepository.save(balance);

            BalanceDTO balanceDTO = balanceService.getWholeBalance();
            assertEquals(0, BigDecimal.valueOf(1000).compareTo(balanceDTO.getOkoBalance()));
            assertEquals(0, BigDecimal.valueOf(500).compareTo(balanceDTO.getCreditBalance()));
        }

        @Test
        void returnsEmptyWhenNoBalance() {
            BalanceDTO balanceDTO = balanceService.getWholeBalance();
            assertEquals(BigDecimal.ZERO, balanceDTO.getOkoBalance());
            assertEquals(BigDecimal.ZERO, balanceDTO.getCreditBalance());
        }
    }
}
