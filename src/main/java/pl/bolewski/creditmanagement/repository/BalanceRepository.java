package pl.bolewski.creditmanagement.repository;

import org.springframework.data.repository.CrudRepository;
import pl.bolewski.creditmanagement.model.Balance;

import java.util.Optional;

public interface BalanceRepository extends CrudRepository <Balance, Integer> {

    Optional<Balance> findByAccountId(Long accountId);
}
