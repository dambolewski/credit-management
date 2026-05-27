package pl.bolewski.creditmanagement.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.bolewski.creditmanagement.dto.TreasuryBondDTO;
import pl.bolewski.creditmanagement.service.TreasuryBondService;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class TreasuryBondController {

    private final TreasuryBondService treasuryBondService;

    @GetMapping("/api/treasuryBond/getDepositedAmount")
    public BigDecimal getDepositedAmount(){
        return treasuryBondService.calculateDepositedTreasuryBond();
    }

    @GetMapping("/api/treasuryBond/getTreasuryBonds")
    public List<TreasuryBondDTO> getTreasuryBonds(){
        return treasuryBondService.getTreasuryBondList();
    }
}
