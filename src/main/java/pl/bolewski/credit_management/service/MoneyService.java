package pl.bolewski.credit_management.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.bolewski.credit_management.dto.MoneyDTO;
import pl.bolewski.credit_management.model.Money;
import pl.bolewski.credit_management.repository.MoneyRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MoneyService {

    private final MoneyRepository moneyRepository;
    private final CalculatorService calculatorService;


    public void addMoney(MoneyDTO moneyDTO){
        Money money = Money.builder()
                .cash(moneyDTO.getCash())
                .account(moneyDTO.getAccount())
                .month(moneyDTO.getMonth())
                .year(String.valueOf(LocalDate.now().getYear()))
                .addedDate(LocalDate.now())
                .transaction("deposit")
                .build();
        updateBalance(moneyDTO.getCash(), moneyDTO.getAccount(), money.getTransaction());
        moneyRepository.save(money);
    }

    public void withdrawMoney(MoneyDTO moneyDTO){
        Money money = Money.builder()
                .cash(moneyDTO.getCash())
                .account(moneyDTO.getAccount())
                .month(moneyDTO.getMonth())
                .year(String.valueOf(LocalDate.now().getYear()))
                .addedDate(LocalDate.now())
                .transaction("withdraw")
                .build();
        updateBalance(moneyDTO.getCash(), moneyDTO.getAccount(), money.getTransaction());
        moneyRepository.save(money);
    }

    public List<Money> getMoney(){
        return (List<Money>) moneyRepository.findAll();
    }

    private void updateBalance(BigDecimal cash, String account, String transaction) {
        calculatorService.updateBalance(cash, account, transaction);
    }

    public Optional<List<Money>> getMoneyByYearAndMonth(String year, String month) {
        return moneyRepository.findByYearAndMonthAndAccount(year, month, "credit");
    }

    public Optional<List<Money>> getMoneyByYear(String year) {
        return moneyRepository.findByYear(year);
    }
}
