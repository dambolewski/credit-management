package pl.bolewski.creditmanagement.service;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.bolewski.creditmanagement.model.AccountType;
import pl.bolewski.creditmanagement.model.Balance;
import pl.bolewski.creditmanagement.model.Money;
import pl.bolewski.creditmanagement.model.TransactionType;
import pl.bolewski.creditmanagement.repository.BalanceRepository;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CalculatorServiceTest {

    @InjectMocks
    CalculatorService calculatorService;
    @Mock
    BalanceService balanceService;
    @Mock
    BalanceRepository balanceRepository;

    @ParameterizedTest(name = "{1} {2} of {0} - Initial(OKO={3}, CREDIT={4}) -> Expected(OKO={5}, CREDIT={6})")
    @CsvSource({
            "1000, OKO, DEPOSIT, 0, 0, 1000, 0",
            "500, OKO, WITHDRAW, 1000, 0, 500, 0",
            "1000, CREDIT, DEPOSIT, 0, 0, 0, 1000",
            "500, CREDIT, WITHDRAW, 0, 1000, 0, 500"
    })
    void updateBalance(BigDecimal amount, AccountType accountType, TransactionType transactionType,
                       BigDecimal initialOkoBalance, BigDecimal initialCreditBalance,
                       BigDecimal expectedOkoBalance, BigDecimal expectedCreditBalance) {
        // Given
        Balance balance = Balance.builder()
                .accountId(1L)
                .okoBalance(initialOkoBalance)
                .creditBalance(initialCreditBalance)
                .build();
        when(balanceService.getBalance()).thenReturn(balance);

        // When
        calculatorService.updateBalance(amount, accountType, transactionType);

        // Then
        ArgumentCaptor<Balance> balanceCaptor = ArgumentCaptor.forClass(Balance.class);
        verify(balanceRepository).save(balanceCaptor.capture());

        Balance capturedBalance = balanceCaptor.getValue();
        assertEquals(expectedOkoBalance, capturedBalance.getOkoBalance());
        assertEquals(expectedCreditBalance, capturedBalance.getCreditBalance());
    }

    @Test
    void calculateMoneyInsideList() {
        // Given
        Money deposit1 = Money.builder()
                .cash(BigDecimal.valueOf(1000))
                .transactionType(TransactionType.DEPOSIT)
                .build();
        Money deposit2 = Money.builder()
                .cash(BigDecimal.valueOf(500))
                .transactionType(TransactionType.DEPOSIT)
                .build();
        Money withdraw = Money.builder()
                .cash(BigDecimal.valueOf(300))
                .transactionType(TransactionType.WITHDRAW)
                .build();

        List<Money> moneyList = List.of(deposit1, deposit2, withdraw);

        // When
        BigDecimal result = calculatorService.calculateMoneyInsideList(moneyList);

        // Then
        assertEquals(0, BigDecimal.valueOf(1200).compareTo(result));
    }
}
