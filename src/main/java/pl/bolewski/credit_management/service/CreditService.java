package pl.bolewski.credit_management.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.bolewski.credit_management.dto.MonthVerifierDTO;
import pl.bolewski.credit_management.model.Money;
import pl.bolewski.credit_management.repository.MoneyRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CreditService {

    public final MoneyRepository moneyRepository;
    public final MoneyService moneyService;

    public MonthVerifierDTO checkMonthlyPayouts(String month){
        Optional<List<Money>> monthlyMoneyListOptional = moneyService.getMoneyByMonth(month);
        long statusMoney = 0L;
        if(monthlyMoneyListOptional.isPresent()){
            List<Money> monthlyMoneyList = monthlyMoneyListOptional.get();
            long monthlyAddedCash = 0L;
            long monthlyWithdrewCash = 0L;
            for (Money money : monthlyMoneyList) {
                if(money.getTransaction().equals("deposit"))
                    monthlyAddedCash += money.getCash();
                else
                    monthlyWithdrewCash += money.getCash();
            }
            long monthlyMoney = monthlyAddedCash - monthlyWithdrewCash;
            statusMoney = (monthlyMoney) - 1835;
        }
        return MonthVerifierDTO.builder()
                .month(month)
                .status(statusMoney)
                .build();
    }
}
