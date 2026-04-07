package pl.bolewski.creditmanagement.repository;

import org.springframework.data.repository.CrudRepository;
import pl.bolewski.creditmanagement.model.AccountType;
import pl.bolewski.creditmanagement.model.Money;

import java.util.List;
import java.util.Optional;

public interface MoneyRepository extends CrudRepository<Money, Integer> {

    Optional<List<Money>> findByYearAndMonthAndAccountType(String year, String month, AccountType account);

    Optional<List<Money>> findByYearAndAccountType(String year, AccountType account);

    List<Money> findAllByOrderByAddedAtDesc();

    Optional<List<Money>> findByAccountType(AccountType accountType);
}
