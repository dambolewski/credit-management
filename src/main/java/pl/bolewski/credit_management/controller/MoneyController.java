package pl.bolewski.credit_management.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import pl.bolewski.credit_management.dto.MoneyDTO;
import pl.bolewski.credit_management.model.Money;
import pl.bolewski.credit_management.response.ApiResponse;
import pl.bolewski.credit_management.service.MoneyService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class MoneyController {

    private final MoneyService moneyService;

    @PostMapping("/api/money/add")
    public ResponseEntity<ApiResponse> addMoney(@RequestBody MoneyDTO moneyDTO) {
        Money savedMoney = moneyService.addMoney(moneyDTO);
        ApiResponse apiResponse = ApiResponse.builder()
                .message("Cash added")
                .status(200)
                .build();
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @GetMapping("/api/money/getHistory")
    public List<Money> getMoneyHistory(){
        return moneyService.getMoney();
    }

}
