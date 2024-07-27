package pl.bolewski.credit_management.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.bolewski.credit_management.model.Balance;
import pl.bolewski.credit_management.dto.BalanceDTO;
import pl.bolewski.credit_management.response.ApiResponse;
import pl.bolewski.credit_management.service.BalanceService;

@RestController
@RequiredArgsConstructor
public class BalanceController {

    private final BalanceService balanceService;

    @PostMapping("/api/balance/add")
    public ResponseEntity<ApiResponse> addBalance(@RequestBody Balance balance) {
        Balance savedBalance = balanceService.addBalance(balance);
        ApiResponse apiResponse = ApiResponse.builder()
                .message("Balance added")
                .status(200)
                .build();
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @GetMapping("/api/balance/getBalance")
    public BalanceDTO getWholeBalance() {
        return balanceService.getWholeBalance();
    }
}
