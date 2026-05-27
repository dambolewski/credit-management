package pl.bolewski.creditmanagement.service;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.bolewski.creditmanagement.dto.MoneyDTO;
import pl.bolewski.creditmanagement.model.AccountType;
import pl.bolewski.creditmanagement.model.Money;
import pl.bolewski.creditmanagement.model.TransactionType;
import pl.bolewski.creditmanagement.repository.MoneyRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MoneyServiceTest {

    @InjectMocks
    MoneyService moneyService;
    @Mock
    MoneyRepository moneyRepository;
    @Mock
    CalculatorService calculatorService;

    @ParameterizedTest(name = "processTransaction - {0} {1} of {2} in month {3}")
    @CsvSource({
            "DEPOSIT, OKO, 1000, 07",
            "DEPOSIT, CREDIT, 1500, 08",
            "WITHDRAW, OKO, 500, 09",
            "WITHDRAW, CREDIT, 750, 10"
    })
    void processTransaction(TransactionType transactionType, AccountType accountType,
                            BigDecimal amount, String month) {
        // Given
        MoneyDTO moneyDTO = new MoneyDTO(amount, accountType, month);

        // When
        if (transactionType == TransactionType.DEPOSIT) {
            moneyService.depositMoney(moneyDTO);
        } else {
            moneyService.withdrawMoney(moneyDTO);
        }

        // Then
        ArgumentCaptor<Money> moneyCaptor = ArgumentCaptor.forClass(Money.class);
        verify(moneyRepository).save(moneyCaptor.capture());
        verify(calculatorService).updateBalance(amount, accountType, transactionType);

        Money savedMoney = moneyCaptor.getValue();
        assertEquals(amount, savedMoney.getCash());
        assertEquals(accountType, savedMoney.getAccountType());
        assertEquals(month, savedMoney.getMonth());
        assertEquals(String.valueOf(LocalDate.now().getYear()), savedMoney.getYear());
        assertEquals(transactionType, savedMoney.getTransactionType());
        assertNotNull(savedMoney.getAddedAt());
    }

    @ParameterizedTest(name = "processTransactionList - {0} {1} amounts=[{2},{3}] month={4}")
    @CsvSource({
            "DEPOSIT, OKO, 1000, 400, 07",
            "DEPOSIT, CREDIT, 1500, 800, 08",
            "WITHDRAW, OKO, 500, 750, 09"
    })
    void processTransactionList(TransactionType transactionType, AccountType accountType,
                                BigDecimal amount1, BigDecimal amount2, String month) {
        // Given
        List<MoneyDTO> moneyDTOList = List.of(
                new MoneyDTO(amount1, accountType, month),
                new MoneyDTO(amount2, accountType, month)
        );

        // When
        if (transactionType == TransactionType.DEPOSIT) {
            moneyService.depositMoneyList(moneyDTOList);
        } else {
            moneyService.withdrawMoneyList(moneyDTOList);
        }

        // Then
        ArgumentCaptor<Money> moneyCaptor = ArgumentCaptor.forClass(Money.class);
        verify(moneyRepository, times(2)).save(moneyCaptor.capture());

        List<Money> savedMoneyList = moneyCaptor.getAllValues();

        assertEquals(0, amount1.compareTo(savedMoneyList.get(0).getCash()));
        assertEquals(0, amount2.compareTo(savedMoneyList.get(1).getCash()));
    }

    @Test
    void getMoney() {
        // Given
        List<Money> mockMoneyList = List.of(
                createMockMoney(BigDecimal.valueOf(1000), AccountType.CREDIT, "07"),
                createMockMoney(BigDecimal.valueOf(500), AccountType.OKO, "08")
        );
        when(moneyRepository.findAllByOrderByAddedAtDesc()).thenReturn(mockMoneyList);

        // When
        List<Money> result = moneyService.getMoney();

        // Then
        assertEquals(2, result.size());
        assertEquals(mockMoneyList, result);
        verify(moneyRepository).findAllByOrderByAddedAtDesc();
    }

    @ParameterizedTest(name = "getMoneyByYearAndMonth - year={0}, month={1}, hasData={2}")
    @CsvSource({
            "2024, 07, true",
            "2024, 08, false"
    })
    void getMoneyByYearAndMonth(String year, String month, boolean hasData) {
        // Given
        List<Money> mockResult = hasData ?
                List.of(createMockMoney(BigDecimal.valueOf(1000), AccountType.CREDIT, month)) :
                List.of();

        when(moneyRepository.findByYearAndMonthAndAccountType(year, month, AccountType.CREDIT))
                .thenReturn(mockResult);

        // When
        List<Money> result = moneyService.getMoneyByYearAndMonth(year, month, AccountType.CREDIT);

        // Then
        assertEquals(hasData ? 1 : 0, result.size());
        verify(moneyRepository).findByYearAndMonthAndAccountType(year, month, AccountType.CREDIT);
    }

    @ParameterizedTest(name = "getMoneyByYear - year={0}, hasData={1}")
    @CsvSource({
            "2024, true",
            "2023, false"
    })
    void getMoneyByYear(String year, boolean hasData) {
        // Given
        List<Money> mockResult = hasData ?
                List.of(
                        createMockMoney(BigDecimal.valueOf(1000), AccountType.CREDIT, "07"),
                        createMockMoney(BigDecimal.valueOf(2000), AccountType.CREDIT, "08")
                ) :
                List.of();

        when(moneyRepository.findByYearAndAccountType(year, AccountType.CREDIT)).thenReturn(mockResult);

        // When
        List<Money> result = moneyService.getMoneyByYear(year, AccountType.CREDIT);

        // Then
        assertEquals(hasData ? 2 : 0, result.size());
        verify(moneyRepository).findByYearAndAccountType(year, AccountType.CREDIT);
    }

    private Money createMockMoney(BigDecimal amount, AccountType accountType, String month) {
        return Money.builder()
                .cash(amount)
                .accountType(accountType)
                .month(month)
                .year(String.valueOf(LocalDate.now().getYear()))
                .addedAt(LocalDateTime.now())
                .transactionType(TransactionType.DEPOSIT)
                .build();
    }
}
