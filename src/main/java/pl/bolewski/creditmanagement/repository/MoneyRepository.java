package pl.bolewski.creditmanagement.repository;

import org.springframework.data.repository.CrudRepository;
import pl.bolewski.creditmanagement.model.AccountType;
import pl.bolewski.creditmanagement.model.Money;

import java.util.List;
public interface MoneyRepository extends CrudRepository<Money, Integer> {

    List<Money> findByYearAndMonthAndAccountType(String year, String month, AccountType account);

    List<Money> findByYearAndAccountType(String year, AccountType account);

    List<Money> findAllByOrderByAddedAtDesc();

    List<Money> findByAccountType(AccountType accountType);
}
