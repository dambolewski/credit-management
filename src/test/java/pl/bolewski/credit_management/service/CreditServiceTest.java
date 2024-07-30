package pl.bolewski.credit_management.service;

import org.junit.AfterClass;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import pl.bolewski.credit_management.dto.MonthVerifierDTO;
import pl.bolewski.credit_management.dto.YearlyVerifierDTO;
import pl.bolewski.credit_management.model.Money;
import pl.bolewski.credit_management.repository.MoneyRepository;
import pl.bolewski.credit_management.testcontainers.TestcontainersSetup;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DirtiesContext
class CreditServiceTest extends TestcontainersSetup {

    private final MoneyRepository moneyRepository;
    private final CreditService creditService;

    @Autowired
    CreditServiceTest(MoneyRepository moneyRepository, CreditService creditService) {
        this.moneyRepository = moneyRepository;
        this.creditService = creditService;
    }


    @BeforeEach
    void setUp() {
        moneyRepository.deleteAll();

        Money money1 = createTestMoney(1000,"credit","01", "2024","deposit");
        Money money2 = createTestMoney(850,"credit","01", "2024","deposit");
        Money money3 = createTestMoney(1000,"credit","02", "2024","deposit");

        moneyRepository.save(money1);
        moneyRepository.save(money2);
        moneyRepository.save(money3);
    }

    @Test
    void checkMonthlyPayouts() {
        MonthVerifierDTO result = creditService.checkMonthlyPayouts("2024", "01");

        assertEquals(0, BigDecimal.valueOf(1850).compareTo(result.getCollected()));
        assertEquals(0, BigDecimal.valueOf(15).compareTo(result.getStatus()));
    }

    @Test
    void checkYearlyPayouts() {
        // Act
        YearlyVerifierDTO result = creditService.checkYearlyPayouts("2024");

        // Assert
        assertEquals(0, result.getCollected().compareTo(BigDecimal.valueOf(2850)));

        BigDecimal expectedStatus = BigDecimal.valueOf(2850).subtract(BigDecimal.valueOf(16515));
        assertEquals(0, result.getStatus().compareTo(expectedStatus));
    }

    private Money createTestMoney(int value, String account, String month, String year, String transaction) {
        return Money.builder()
                .cash(BigDecimal.valueOf(value))
                .account(account)
                .month(month)
                .year(year)
                .addedDate(LocalDate.now())
                .transaction(transaction)
                .build();
    }
}
