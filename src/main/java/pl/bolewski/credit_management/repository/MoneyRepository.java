package pl.bolewski.credit_management.repository;

import org.springframework.data.repository.CrudRepository;
import pl.bolewski.credit_management.model.Money;

public interface MoneyRepository extends CrudRepository<Money, Integer> {
}
