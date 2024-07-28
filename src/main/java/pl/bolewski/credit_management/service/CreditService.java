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

    public final MoneyRepository moneyRepository;
    public final MoneyService moneyService;
    public final CalculatorService calculatorService;

    public MonthVerifierDTO checkMonthlyPayouts(String year, String month) {
        Optional<List<Money>> monthlyMoneyListOptional = moneyService.getMoneyByYearAndMonth(year, month);
        BigDecimal statusMoney = BigDecimal.ZERO;
        BigDecimal monthlyMoney = BigDecimal.ZERO;
        if (monthlyMoneyListOptional.isPresent()) {
            List<Money> monthlyMoneyList = monthlyMoneyListOptional.get();
            monthlyMoney = calculatorService.calculateMoneyInsideList(monthlyMoneyList);
            statusMoney = monthlyMoney.subtract(BigDecimal.valueOf(1835));
        }

        return MonthVerifierDTO.builder()
                .month(month)
                .needed(BigDecimal.valueOf(1835))
                .collected(monthlyMoney)
                .status(statusMoney)
                .build();
    }

    public YearlyVerifierDTO checkYearlyPayouts(String year) {
        Optional<List<Money>> yearlyMoneyListOptional = moneyService.getMoneyByYear(year);
        BigDecimal statusMoney = BigDecimal.ZERO;
        BigDecimal yearlyMoney = BigDecimal.ZERO;
        if (yearlyMoneyListOptional.isPresent()) {
            List<Money> yearlyMoneyList = yearlyMoneyListOptional.get();
            yearlyMoney = calculatorService.calculateMoneyInsideList(yearlyMoneyList);
            if (year.equals(String.valueOf(2024)))
                statusMoney = yearlyMoney.subtract(BigDecimal.valueOf(16515));
            else
                statusMoney = yearlyMoney.subtract(BigDecimal.valueOf(22020));
        }

        if (year.equals(String.valueOf(2024))) {
            return YearlyVerifierDTO.builder()
                    .year(year)
                    .needed(BigDecimal.valueOf(16515))
                    .collected(yearlyMoney)
                    .status(statusMoney)
                    .build();
        } else {
            return YearlyVerifierDTO.builder()
                    .year(year)
                    .needed(BigDecimal.valueOf(22020))
                    .collected(yearlyMoney)
                    .status(statusMoney)
                    .build();
        }
    }
}
