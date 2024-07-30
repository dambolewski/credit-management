package pl.bolewski.credit_management.service;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import pl.bolewski.credit_management.model.Balance;
import pl.bolewski.credit_management.model.Money;
import pl.bolewski.credit_management.repository.BalanceRepository;
import pl.bolewski.credit_management.testcontainers.TestcontainersSetup;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DirtiesContext
class CalculatorServiceTest extends TestcontainersSetup {

    private final CalculatorService calculatorService;
    private final BalanceRepository balanceRepository;

    @Autowired
    CalculatorServiceTest(CalculatorService calculatorService, BalanceRepository balanceRepository) {
        this.calculatorService = calculatorService;
        this.balanceRepository = balanceRepository;
    }

    @BeforeEach
    void setUp() {
        balanceRepository.deleteAll();
    }


    @Nested
    class updateBalanceTests {

        @Test
        void updateBalanceOkoAccount(){
            BigDecimal depositAmount = BigDecimal.valueOf(1000);
            BigDecimal withdrawAmount = BigDecimal.valueOf(500);

            calculatorService.updateBalance(depositAmount, "oko", "deposit");

            Balance balance = balanceRepository.findByAccountId(1L).orElseThrow();
            assertEquals(0, BigDecimal.valueOf(1000).compareTo(balance.getOkoBalance()));

            calculatorService.updateBalance(withdrawAmount, "oko", "withdraw");

            balance = balanceRepository.findByAccountId(1L).orElseThrow();
            assertEquals(0, BigDecimal.valueOf(500).compareTo(balance.getOkoBalance()));
        }

        @Test
        void updateBalanceCreditAccount(){
            BigDecimal depositAmount = BigDecimal.valueOf(1000);
            BigDecimal withdrawAmount = BigDecimal.valueOf(500);

            calculatorService.updateBalance(depositAmount, "credit", "deposit");

            Balance balance = balanceRepository.findByAccountId(1L).orElseThrow();
            assertEquals(0, BigDecimal.valueOf(1000).compareTo(balance.getCreditBalance()));

            calculatorService.updateBalance(withdrawAmount, "credit", "withdraw");

            balance = balanceRepository.findByAccountId(1L).orElseThrow();
            assertEquals(0, BigDecimal.valueOf(500).compareTo(balance.getCreditBalance()));
        }

    }

    @Test
    void calculateMoneyInsideList() {

        Money deposit1 = Money.builder().cash(BigDecimal.valueOf(1000)).transaction("deposit").build();
        Money deposit2 = Money.builder().cash(BigDecimal.valueOf(500)).transaction("deposit").build();
        Money withdraw = Money.builder().cash(BigDecimal.valueOf(300)).transaction("withdraw").build();

        List<Money> moneyList = List.of(deposit1, deposit2, withdraw);

        BigDecimal result = calculatorService.calculateMoneyInsideList(moneyList);

        assertEquals(0, BigDecimal.valueOf(1200).compareTo(result));

    }
}
