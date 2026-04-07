package pl.bolewski.creditmanagement.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.bolewski.creditmanagement.dto.MoneyDTO;
import pl.bolewski.creditmanagement.model.AccountType;
import pl.bolewski.creditmanagement.model.Money;
import pl.bolewski.creditmanagement.model.TransactionType;
import pl.bolewski.creditmanagement.repository.MoneyRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MoneyService {

    private final MoneyRepository moneyRepository;
    private final CalculatorService calculatorService;

    @Transactional
    public void depositMoney(MoneyDTO moneyDTO){
        processTransaction(moneyDTO, TransactionType.DEPOSIT);
    }

    @Transactional
    public void depositMoneyList(List<MoneyDTO> moneyDTOList) {
        for (MoneyDTO moneyDTO : moneyDTOList) {
            processTransaction(moneyDTO, TransactionType.DEPOSIT);
        }
    }

    @Transactional
    public void withdrawMoney(MoneyDTO moneyDTO){
        processTransaction(moneyDTO, TransactionType.WITHDRAW);
    }

    @Transactional
    public void withdrawMoneyList(List<MoneyDTO> moneyDTOList){
        for (MoneyDTO moneyDTO : moneyDTOList) {
            processTransaction(moneyDTO, TransactionType.WITHDRAW);
        }
    }

    @Transactional(readOnly = true)
    public List<Money> getMoney(){
        return moneyRepository.findAllByOrderByAddedAtDesc();
    }

    @Transactional(readOnly = true)
    public Optional<List<Money>> getMoneyByYearAndMonth(String year, String month, AccountType accountType) {
        return moneyRepository.findByYearAndMonthAndAccountType(year, month, accountType);
    }

    @Transactional(readOnly = true)
    public Optional<List<Money>> getMoneyByYear(String year, AccountType accountType) {
        return moneyRepository.findByYearAndAccountType(year, accountType);
    }

    @Transactional(readOnly = true)
    public Optional<List<Money>> getMoneyByAccountType(AccountType accountType) {
        return moneyRepository.findByAccountType(accountType);
    }

    private void processTransaction(MoneyDTO moneyDTO, TransactionType transactionType) {
        Money money = Money.builder()
                .cash(moneyDTO.getCash())
                .accountType(moneyDTO.getAccountType())
                .month(moneyDTO.getMonth())
                .year(String.valueOf(LocalDate.now().getYear()))
                .addedAt(LocalDateTime.now())
                .transactionType(transactionType)
                .build();
        updateBalance(moneyDTO.getCash(), moneyDTO.getAccountType(), transactionType);
        moneyRepository.save(money);
    }

    private void updateBalance(BigDecimal cash, AccountType accountType, TransactionType transactionType) {
        calculatorService.updateBalance(cash, accountType, transactionType);
    }
}
