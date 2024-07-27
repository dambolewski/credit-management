package pl.bolewski.credit_management.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.bolewski.credit_management.dto.MoneyDTO;
import pl.bolewski.credit_management.model.Money;
import pl.bolewski.credit_management.repository.BalanceRepository;
import pl.bolewski.credit_management.repository.MoneyRepository;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MoneyService {

    private final MoneyRepository moneyRepository;
    private final CalculatorService calculatorService;


    public Money addMoney(MoneyDTO moneyDTO){
        Money money = Money.builder()
                .cash(moneyDTO.getCash())
                .account(moneyDTO.getAccount())
                .month(moneyDTO.getMonth())
                .addedDate(LocalDate.now())
                .build();
        updateBalance(moneyDTO.getCash(), moneyDTO.getAccount());
        return moneyRepository.save(money);
    }

    public List<Money> getMoney(){
        return (List<Money>) moneyRepository.findAll();
    }

    private void updateBalance(Long cash, String account) {
        calculatorService.updateBalance(cash, account);
    }
}
