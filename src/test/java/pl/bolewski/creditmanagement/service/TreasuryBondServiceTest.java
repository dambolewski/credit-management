package pl.bolewski.creditmanagement.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.bolewski.creditmanagement.dto.TreasuryBondDTO;
import pl.bolewski.creditmanagement.model.AccountType;
import pl.bolewski.creditmanagement.model.Money;
import pl.bolewski.creditmanagement.model.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TreasuryBondServiceTest {

    @Mock
    private MoneyService moneyService;

    @InjectMocks
    private TreasuryBondService treasuryBondService;

    @Test
    void getTreasuryBondList_returnsListOfTreasuryBondDTOs() {
        // Given
        List<Money> mockMoneyList = List.of(
                createMockMoney(BigDecimal.valueOf(1000), "01", "2024"),
                createMockMoney(BigDecimal.valueOf(2000), "02", "2024"),
                createMockMoney(BigDecimal.valueOf(1500), "03", "2024")
        );
        when(moneyService.getMoneyByAccountType(AccountType.TREASURY_BOND))
                .thenReturn(mockMoneyList);

        // When
        List<TreasuryBondDTO> result = treasuryBondService.getTreasuryBondList();

        // Then
        assertEquals(3, result.size());

        assertEquals("01", result.get(0).getMonth());
        assertEquals("2024", result.get(0).getYear());
        assertEquals(0, BigDecimal.valueOf(1000).compareTo(result.get(0).getTreasuryBond()));

        assertEquals("02", result.get(1).getMonth());
        assertEquals("2024", result.get(1).getYear());
        assertEquals(0, BigDecimal.valueOf(2000).compareTo(result.get(1).getTreasuryBond()));

        assertEquals("03", result.get(2).getMonth());
        assertEquals(0, BigDecimal.valueOf(1500).compareTo(result.get(2).getTreasuryBond()));

        verify(moneyService).getMoneyByAccountType(AccountType.TREASURY_BOND);
    }

    @Test
    void getTreasuryBondList_returnsEmptyListWhenNoData() {
        // Given
        when(moneyService.getMoneyByAccountType(AccountType.TREASURY_BOND))
                .thenReturn(List.of());

        // When
        List<TreasuryBondDTO> result = treasuryBondService.getTreasuryBondList();

        // Then
        assertTrue(result.isEmpty());
        verify(moneyService).getMoneyByAccountType(AccountType.TREASURY_BOND);
    }

    @ParameterizedTest(name = "calculateDepositedTreasuryBond - {0} items, total={1}")
    @CsvSource({
            "1, 1000",
            "2, 2500",
            "3, 4500"
    })
    void calculateDepositedTreasuryBond(int itemCount, BigDecimal expectedTotal) {
        // Given
        List<Money> mockMoneyList = createMockMoneyList(itemCount);
        when(moneyService.getMoneyByAccountType(AccountType.TREASURY_BOND))
                .thenReturn(mockMoneyList);

        // When
        BigDecimal result = treasuryBondService.calculateDepositedTreasuryBond();

        // Then
        assertEquals(expectedTotal, result);
        verify(moneyService).getMoneyByAccountType(AccountType.TREASURY_BOND);
    }

    @Test
    void calculateDepositedTreasuryBond_returnsZeroWhenNoData() {
        // Given
        when(moneyService.getMoneyByAccountType(AccountType.TREASURY_BOND))
                .thenReturn(List.of());

        // When
        BigDecimal result = treasuryBondService.calculateDepositedTreasuryBond();

        // Then
        assertEquals(0, BigDecimal.ZERO.compareTo(result));
        verify(moneyService).getMoneyByAccountType(AccountType.TREASURY_BOND);
    }

    @Test
    void calculateDepositedTreasuryBond_handlesVariousAmounts() {
        // Given
        List<Money> mockMoneyList = List.of(
                createMockMoney(BigDecimal.valueOf(500.50), "01", "2024"),
                createMockMoney(BigDecimal.valueOf(1000.25), "02", "2024"),
                createMockMoney(BigDecimal.valueOf(250.75), "03", "2024")
        );
        when(moneyService.getMoneyByAccountType(AccountType.TREASURY_BOND))
                .thenReturn(mockMoneyList);

        // When
        BigDecimal result = treasuryBondService.calculateDepositedTreasuryBond();

        // Then
        assertEquals(0, BigDecimal.valueOf(1751.50).compareTo(result));
    }

    @Test
    void getTreasuryBondList_handlesSingleItem() {
        // Given
        List<Money> mockMoneyList = List.of(
                createMockMoney(BigDecimal.valueOf(5000), "12", "2023")
        );
        when(moneyService.getMoneyByAccountType(AccountType.TREASURY_BOND))
                .thenReturn(mockMoneyList);

        // When
        List<TreasuryBondDTO> result = treasuryBondService.getTreasuryBondList();

        // Then
        assertEquals(1, result.size());
        assertEquals("12", result.get(0).getMonth());
        assertEquals("2023", result.get(0).getYear());
        assertEquals(0, BigDecimal.valueOf(5000).compareTo(result.get(0).getTreasuryBond()));
    }

    private Money createMockMoney(BigDecimal amount, String month, String year) {
        return Money.builder()
                .cash(amount)
                .accountType(AccountType.TREASURY_BOND)
                .month(month)
                .year(year)
                .addedAt(LocalDateTime.now())
                .transactionType(TransactionType.DEPOSIT)
                .build();
    }

    private List<Money> createMockMoneyList(int count) {
        List<Money> moneyList = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            moneyList.add(createMockMoney(
                    BigDecimal.valueOf(1000 + i * 500L),
                    String.format("%02d", i + 1),
                    "2024"
            ));
        }
        return moneyList;
    }
}