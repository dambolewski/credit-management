package pl.bolewski.creditmanagement.integration;

import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import pl.bolewski.creditmanagement.dto.MoneyDTO;
import pl.bolewski.creditmanagement.model.AccountType;
import pl.bolewski.creditmanagement.model.Balance;
import pl.bolewski.creditmanagement.repository.BalanceRepository;
import pl.bolewski.creditmanagement.repository.MoneyRepository;

import java.math.BigDecimal;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

class CreditControllerIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private BalanceRepository balanceRepository;

    @Autowired
    private MoneyRepository moneyRepository;

    @BeforeEach
    void setUp() {
        super.setUpRestAssured();
        moneyRepository.deleteAll();
        balanceRepository.deleteAll();
        createInitialBalance();
    }

    @Test
    void shouldCheckMonthlyPayouts() {
        MoneyDTO moneyDTO = new MoneyDTO(
                BigDecimal.valueOf(2000),
                AccountType.CREDIT,
                "July"
        );

        given()
                .contentType(ContentType.JSON)
                .body(moneyDTO)
                .when()
                .post("/api/money/deposit")
                .then()
                .statusCode(201);

        given()
                .when()
                .get("/api/credit/checkMonthly/2025/July")
                .then()
                .statusCode(200)
                .body("month", equalTo("July"))
                .body("needed", notNullValue())
                .body("collected", notNullValue())
                .body("status", notNullValue());
    }

    @Test
    void shouldCheckYearlyPayouts() {
        MoneyDTO moneyDTO1 = new MoneyDTO(
                BigDecimal.valueOf(1000),
                AccountType.CREDIT,
                "January"
        );
        MoneyDTO moneyDTO2 = new MoneyDTO(
                BigDecimal.valueOf(1500),
                AccountType.CREDIT,
                "February"
        );

        given()
                .contentType(ContentType.JSON)
                .body(moneyDTO1)
                .when()
                .post("/api/money/deposit")
                .then()
                .statusCode(201);

        given()
                .contentType(ContentType.JSON)
                .body(moneyDTO2)
                .when()
                .post("/api/money/deposit")
                .then()
                .statusCode(201);

        given()
                .when()
                .get("/api/credit/checkYearly/2025")
                .then()
                .statusCode(200)
                .body("year", equalTo("2025"))
                .body("needed", notNullValue())
                .body("collected", notNullValue())
                .body("status", notNullValue());
    }

    @Test
    void shouldReturnZeroCollectedWhenNoDeposits() {
        given()
                .when()
                .get("/api/credit/checkMonthly/2025/June")
                .then()
                .statusCode(200)
                .body("month", equalTo("June"))
                .body("collected", equalTo(0));
    }

    private void createInitialBalance() {
        Balance balance = Balance.builder()
                .accountId(1L)
                .okoBalance(BigDecimal.ZERO)
                .creditBalance(BigDecimal.ZERO)
                .build();
        balanceRepository.save(balance);
    }
}
