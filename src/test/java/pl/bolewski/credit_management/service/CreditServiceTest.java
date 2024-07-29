package pl.bolewski.credit_management.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pl.bolewski.credit_management.dto.MoneyDTO;
import pl.bolewski.credit_management.dto.MonthVerifierDTO;
import pl.bolewski.credit_management.model.Money;
import pl.bolewski.credit_management.repository.MoneyRepository;
import pl.bolewski.credit_management.testcontainers.TestcontainersSetup;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CreditServiceTest extends TestcontainersSetup {


    @Test
    void checkMonthlyPayouts() {
    }

    @Test
    void checkYearlyPayouts() {
    }
}