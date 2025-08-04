package pl.bolewski.credit_management.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import pl.bolewski.credit_management.dto.MonthVerifierDTO;
import pl.bolewski.credit_management.dto.TreasuryBondDTO;
import pl.bolewski.credit_management.dto.YearlyVerifierDTO;
import pl.bolewski.credit_management.service.CreditService;
import pl.bolewski.credit_management.service.TreasuryBondService;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class TreasuryBondController {

    public final TreasuryBondService treasuryBondService;

    @GetMapping("/api/treasuryBond/checkDepositedAmount")
    public BigDecimal checkDepositedAmount(){
        return treasuryBondService.calculateDepositedTreasuryBond();
    }

    @GetMapping("/api/treasuryBond/getTreasuryBonds")
    public List<TreasuryBondDTO> getTreasuryBonds(){
        return treasuryBondService.getTreasuryBondList();
    }
}
