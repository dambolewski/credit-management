package pl.bolewski.creditmanagement.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import pl.bolewski.creditmanagement.dto.MoneyDTO;
import pl.bolewski.creditmanagement.model.Money;
import pl.bolewski.creditmanagement.response.ApiResponse;
import pl.bolewski.creditmanagement.service.MoneyService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class MoneyController {

    private final MoneyService moneyService;

    @PostMapping("/api/money/deposit")
    public ResponseEntity<ApiResponse> depositMoney(@RequestBody MoneyDTO moneyDTO) {
        moneyService.depositMoney(moneyDTO);
        ApiResponse apiResponse = ApiResponse.builder()
                .message("DEPOSIT - Money processed successfully")
                .status(200)
                .build();
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }


    @PostMapping("/api/money/deposit-list")
    public ResponseEntity<ApiResponse> depositMoneyList(@RequestBody List<MoneyDTO> moneyDTOList) {
        moneyService.depositMoneyList(moneyDTOList);
        ApiResponse apiResponse = ApiResponse.builder()
                .message("DEPOSIT - Money list processed successfully")
                .status(200)
                .build();
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @PostMapping("/api/money/withdraw")
    public ResponseEntity<ApiResponse> withdrawMoney(@RequestBody MoneyDTO moneyDTO) {
        moneyService.withdrawMoney(moneyDTO);
        ApiResponse apiResponse = ApiResponse.builder()
                .message("WITHDRAW - Money processed successfully")
                .status(200)
                .build();
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @PostMapping("/api/money/withdraw-list")
    public ResponseEntity<ApiResponse> withdrawMoneyList(@RequestBody List<MoneyDTO> moneyDTOList) {
        moneyService.withdrawMoneyList(moneyDTOList);
        ApiResponse apiResponse = ApiResponse.builder()
                .message("WITHDRAW - Money list processed successfully")
                .status(200)
                .build();
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @GetMapping("/api/money/getHistory")
    public List<Money> getMoneyHistory(){
        return moneyService.getMoney();
    }

}
