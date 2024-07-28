package pl.bolewski.credit_management.repository;

import org.springframework.data.repository.CrudRepository;
import pl.bolewski.credit_management.model.Money;

import java.util.List;
import java.util.Optional;

public interface MoneyRepository extends CrudRepository<Money, Integer> {

    Optional<List<Money>> findByYearAndMonthAndAccount(String year, String month, String account);

    Optional<List<Money>> findByYear(String year);
}
