package pl.bolewski.creditmanagement.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.bolewski.creditmanagement.model.Balance;
import pl.bolewski.creditmanagement.dto.BalanceDTO;
import pl.bolewski.creditmanagement.response.ApiResponse;
import pl.bolewski.creditmanagement.service.BalanceService;

import java.math.BigDecimal;

@RestController
@RequiredArgsConstructor
public class BalanceController {

    private final BalanceService balanceService;

    @PostMapping("/api/balance/add")
    public ResponseEntity<ApiResponse> addBalance(@Valid @RequestBody Balance balance) {
        balanceService.addBalance(balance);
        ApiResponse apiResponse = ApiResponse.builder()
                .message("Balance added")
                .status(201)
                .build();
        return new ResponseEntity<>(apiResponse, HttpStatus.CREATED);
    }

    @GetMapping("/api/balance/getBalance")
    public BalanceDTO getWholeBalance() {
        return balanceService.getBalanceDto();
    }

    @GetMapping("/api/balance/getCombinedBalance")
    public BigDecimal getCombinedBalance() {
        return balanceService.getCombinedBalance();
    }
}
