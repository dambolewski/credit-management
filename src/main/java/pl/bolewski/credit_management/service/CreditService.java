package pl.bolewski.credit_management.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.bolewski.credit_management.dto.MonthVerifierDTO;
import pl.bolewski.credit_management.dto.YearlyVerifierDTO;
import pl.bolewski.credit_management.model.Money;
import pl.bolewski.credit_management.repository.MoneyRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CreditService {

    private static final BigDecimal MONTHLY_TARGET = BigDecimal.valueOf(1835);
    private static final BigDecimal YEARLY_TARGET_2024 = BigDecimal.valueOf(16515);
    private static final BigDecimal YEARLY_TARGET_DEFAULT = BigDecimal.valueOf(22020);


    public final MoneyRepository moneyRepository;
    public final MoneyService moneyService;
    public final CalculatorService calculatorService;

    public MonthVerifierDTO checkMonthlyPayouts(String year, String month) {
        Optional<List<Money>> moneyListOptional = moneyService.getMoneyByYearAndMonth(year, month);
        List<Money> moneyList = moneyListOptional.orElse(List.of());
        return createMonthVerifierDTO(month, moneyList);
    }

    public YearlyVerifierDTO checkYearlyPayouts(String year) {
        Optional<List<Money>> moneyListOptional = moneyService.getMoneyByYear(year);
        List<Money> moneyList = moneyListOptional.orElse(List.of());
        return createYearlyVerifierDTO(year, moneyList);
    }

    private MonthVerifierDTO createMonthVerifierDTO(String month, List<Money> moneyList) {
        BigDecimal collected = calculatorService.calculateMoneyInsideList(moneyList);
        BigDecimal status = collected.subtract(MONTHLY_TARGET);

        return MonthVerifierDTO.builder()
                .month(month)
                .needed(MONTHLY_TARGET)
                .collected(collected)
                .status(status)
                .build();
    }

    private YearlyVerifierDTO createYearlyVerifierDTO(String year, List<Money> moneyList) {
        BigDecimal yearlyTarget = year.equals("2024") ? YEARLY_TARGET_2024 : YEARLY_TARGET_DEFAULT;
        BigDecimal collected = calculatorService.calculateMoneyInsideList(moneyList);
        BigDecimal status = collected.subtract(yearlyTarget);

        return YearlyVerifierDTO.builder()
                .year(year)
                .needed(yearlyTarget)
                .collected(collected)
                .status(status)
                .build();
    }
}
