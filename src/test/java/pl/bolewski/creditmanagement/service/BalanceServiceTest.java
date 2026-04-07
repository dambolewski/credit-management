package pl.bolewski.creditmanagement.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.bolewski.creditmanagement.dto.BalanceDTO;
import pl.bolewski.creditmanagement.model.Balance;
import pl.bolewski.creditmanagement.repository.BalanceRepository;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BalanceServiceTest {

    @InjectMocks
    BalanceService balanceService;
    @Mock
    BalanceRepository balanceRepository;


    @Test
    void addBalance_createsNewBalance() {
        //Given
        Balance balance = Balance.builder()
                .accountId(1L)
                .okoBalance(BigDecimal.valueOf(1000))
                .creditBalance(BigDecimal.valueOf(500))
                .build();
        when(balanceRepository.findByAccountId(1L)).thenReturn(Optional.empty());

        //When
        balanceService.addBalance(balance);

        //Then
        verify(balanceRepository).findByAccountId(1L);
        verify(balanceRepository).save(balance);
    }

    @Test
    void addBalance_updatesExistingBalance() {
        //Given
        Balance existingBalance = Balance.builder()
                .id(1L).accountId(1L)
                .okoBalance(BigDecimal.valueOf(100))
                .creditBalance(BigDecimal.valueOf(200))
                .build();

        Balance newBalance = Balance.builder()
                .accountId(1L)
                .okoBalance(BigDecimal.valueOf(300))
                .creditBalance(BigDecimal.valueOf(400))
                .build();

        when(balanceRepository.findByAccountId(1L)).thenReturn(Optional.of(existingBalance));

        //When
        balanceService.addBalance(newBalance);

        //Then
        verify(balanceRepository).save(existingBalance);
        assertEquals(0, BigDecimal.valueOf(300).compareTo(existingBalance.getOkoBalance()));
        assertEquals(0, BigDecimal.valueOf(400).compareTo(existingBalance.getCreditBalance()));
    }

    @ParameterizedTest(name = "getBalanceDto - oko={0}, credit={1}, exists={2}")
    @CsvSource({
            "1000, 500, true",
            "0, 0, false"
    })
    void getBalanceDto(BigDecimal okoBalance, BigDecimal creditBalance, boolean balanceExists) {
        // Given
        Optional<Balance> repositoryReturn = balanceExists ?
                Optional.of(Balance.builder()
                        .accountId(1L)
                        .okoBalance(okoBalance)
                        .creditBalance(creditBalance)
                        .build()) :
                Optional.empty();

        when(balanceRepository.findByAccountId(1L)).thenReturn(repositoryReturn);

        // When
        BalanceDTO balanceDTO = balanceService.getBalanceDto();

        // Then
        assertEquals(okoBalance, balanceDTO.getOkoBalance());
        assertEquals(creditBalance, balanceDTO.getCreditBalance());
    }

    @ParameterizedTest(name = "getBalance - {0}")
    @CsvSource({
            "true, 1000, 500",
            "false, 0, 0"
    })
    void getBalance(boolean balanceExists, BigDecimal expectedOkoBalance, BigDecimal expectedCreditBalance) {
        // Given
        Optional<Balance> repositoryReturn = balanceExists ?
                Optional.of(Balance.builder()
                        .accountId(1L)
                        .okoBalance(expectedOkoBalance)
                        .creditBalance(expectedCreditBalance)
                        .build()) :
                Optional.empty();

        when(balanceRepository.findByAccountId(1L)).thenReturn(repositoryReturn);

        // When
        Balance result = balanceService.getBalance();

        // Then
        assertEquals(1L, result.getAccountId());
        assertEquals(expectedOkoBalance, result.getOkoBalance());
        assertEquals(expectedCreditBalance, result.getCreditBalance());
        verify(balanceRepository).findByAccountId(1L);
    }

    @ParameterizedTest(name = "getCombinedBalance - oko={0}, credit={1}, expected={2}")
    @CsvSource({
            "500, 500, 1000",
            "0, 0, 0"
    })
    void getCombinedBalance(BigDecimal okoBalance, BigDecimal creditBalance, BigDecimal expectedResult) {
        // Given
        Optional<Balance> repositoryReturn = okoBalance.equals(BigDecimal.ZERO) && creditBalance.equals(BigDecimal.ZERO) ?
                Optional.empty() :
                Optional.of(Balance.builder()
                        .okoBalance(okoBalance)
                        .creditBalance(creditBalance)
                        .build());

        when(balanceRepository.findByAccountId(1L)).thenReturn(repositoryReturn);

        // When
        BigDecimal result = balanceService.getCombinedBalance();

        // Then
        assertEquals(expectedResult, result);
    }
}
