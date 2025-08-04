package pl.bolewski.credit_management.service;

import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.bolewski.credit_management.dto.MonthVerifierDTO;
import pl.bolewski.credit_management.dto.YearlyVerifierDTO;
import pl.bolewski.credit_management.model.AccountType;
import pl.bolewski.credit_management.model.Money;
import pl.bolewski.credit_management.model.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreditServiceTest {

    @InjectMocks
    CreditService creditService;
    @Mock
    MoneyService moneyService;
    @Mock
    CalculatorService calculatorService;

    @ParameterizedTest(name = "checkMonthlyPayouts - month={0}, collected={1}, expectedStatus={2}")
    @CsvSource({
            "01, 1850, 15",
            "02, 1835, 0",
            "03, 1500, -335",
            "04, 2000, 165",
            "05, 0, -1835"
    })
    void checkMonthlyPayouts(String month, BigDecimal collected, BigDecimal expectedStatus) {
        // Given
        List<Money> moneyList = collected.equals(BigDecimal.ZERO) ?
                List.of() :
                List.of(createTestMoney(collected.intValue(), AccountType.CREDIT, month, "2024", TransactionType.DEPOSIT));

        Optional<List<Money>> moneyListOptional = collected.equals(BigDecimal.ZERO) ?
                Optional.empty() :
                Optional.of(moneyList);

        when(moneyService.getMoneyByYearAndMonth("2024", month, AccountType.CREDIT)).thenReturn(moneyListOptional);
        when(calculatorService.calculateMoneyInsideList(anyList())).thenReturn(collected);

        // When
        MonthVerifierDTO result = creditService.checkMonthlyPayouts("2024", month);

        // Then
        assertEquals(month, result.getMonth());
        assertEquals(0, BigDecimal.valueOf(1835).compareTo(result.getNeeded()));
        assertEquals(collected, result.getCollected());
        assertEquals(expectedStatus, result.getStatus());

        verify(moneyService).getMoneyByYearAndMonth("2024", month, AccountType.CREDIT);
        verify(calculatorService).calculateMoneyInsideList(anyList());
    }

    @ParameterizedTest(name = "checkYearlyPayouts - year={0}, collected={1}, expectedTarget={2}")
    @CsvSource({
            "2024, 2850, 16515",
            "2025, 2000, 22020",
            "2023, 1500, 22020"
    })
    void checkYearlyPayouts(String year, BigDecimal collected, BigDecimal expectedTarget) {
        // Given
        List<Money> moneyList = List.of(createTestMoney(collected.intValue(), AccountType.CREDIT, "01", year, TransactionType.DEPOSIT));

        when(moneyService.getMoneyByYear(year, AccountType.CREDIT)).thenReturn(Optional.of(moneyList));
        when(calculatorService.calculateMoneyInsideList(moneyList)).thenReturn(collected);

        // When
        YearlyVerifierDTO result = creditService.checkYearlyPayouts(year);

        // Then
        assertEquals(year, result.getYear());
        assertEquals(expectedTarget, result.getNeeded());
        assertEquals(collected, result.getCollected());
        assertEquals(collected.subtract(expectedTarget), result.getStatus());
    }

    private Money createTestMoney(int value, AccountType account, String month, String year, TransactionType transaction) {
        return Money.builder()
                .cash(BigDecimal.valueOf(value))
                .accountType(account)
                .month(month)
                .year(year)
                .addedAt(LocalDateTime.now())
                .transactionType(transaction)
                .build();
    }
}
