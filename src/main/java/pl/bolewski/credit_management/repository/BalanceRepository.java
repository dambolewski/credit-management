package pl.bolewski.credit_management.repository;

import org.springframework.data.repository.CrudRepository;
import pl.bolewski.credit_management.model.Balance;

import java.util.Optional;

public interface BalanceRepository extends CrudRepository <Balance, Integer> {

    Optional<Balance> findByAccountId(Long accountId);
}
