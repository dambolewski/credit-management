package pl.bolewski.credit_management.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import pl.bolewski.credit_management.dto.MonthVerifierDTO;
import pl.bolewski.credit_management.dto.YearlyVerifierDTO;
import pl.bolewski.credit_management.service.CreditService;

@RestController
@RequiredArgsConstructor
public class CreditController {

    public final CreditService creditService;

    @GetMapping("/api/credit/checkMonthly/{year}/{month}")
    public MonthVerifierDTO getMonthlyStatus(@PathVariable String year, @PathVariable String month){
        return creditService.checkMonthlyPayouts(year, month);
    }

    @GetMapping("/api/credit/checkYearly/{year}")
    public YearlyVerifierDTO getYearlyStatus(@PathVariable String year){
        return creditService.checkYearlyPayouts(year);
    }
}
