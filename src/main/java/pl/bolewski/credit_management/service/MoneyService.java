package pl.bolewski.credit_management.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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

    @Transactional
    public void depositMoney(MoneyDTO moneyDTO){
        processTransaction(moneyDTO, "deposit");
    }

    @Transactional
    public void depositMoneyList(List<MoneyDTO> moneyDTOList) {
        for (MoneyDTO moneyDTO : moneyDTOList) {
            processTransaction(moneyDTO, "deposit");
        }
    }

    @Transactional
    public void withdrawMoney(MoneyDTO moneyDTO){
        processTransaction(moneyDTO, "withdraw");
    }

    @Transactional
    public void withdrawMoneyList(List<MoneyDTO> moneyDTOList){
        for (MoneyDTO moneyDTO : moneyDTOList) {
            processTransaction(moneyDTO, "withdraw");
        }
    }

    @Transactional(readOnly = true)
    public List<Money> getMoney(){
        return moneyRepository.findAllByOrderByYearAscMonthDesc();
    }

    @Transactional(readOnly = true)
    public Optional<List<Money>> getMoneyByYearAndMonth(String year, String month) {
        return moneyRepository.findByYearAndMonthAndAccount(year, month, "credit");
    }

    @Transactional(readOnly = true)
    public Optional<List<Money>> getMoneyByYear(String year) {
        return moneyRepository.findByYearAndAccount(year, "credit");
    }

    private void processTransaction(MoneyDTO moneyDTO, String transaction) {
        Money money = Money.builder()
                .cash(moneyDTO.getCash())
                .account(moneyDTO.getAccount())
                .month(moneyDTO.getMonth())
                .year(String.valueOf(LocalDate.now().getYear()))
                .addedDate(LocalDate.now())
                .transaction(transaction)
                .build();
        updateBalance(moneyDTO.getCash(), moneyDTO.getAccount(), transaction);
        moneyRepository.save(money);
    }

    private void updateBalance(BigDecimal cash, String account, String transaction) {
        calculatorService.updateBalance(cash, account, transaction);
    }
}
