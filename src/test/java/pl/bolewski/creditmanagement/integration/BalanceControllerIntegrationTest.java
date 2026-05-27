package pl.bolewski.creditmanagement.integration;

import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import pl.bolewski.creditmanagement.model.Balance;
import pl.bolewski.creditmanagement.repository.BalanceRepository;
import pl.bolewski.creditmanagement.repository.MoneyRepository;

import java.math.BigDecimal;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

class BalanceControllerIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private BalanceRepository balanceRepository;

    @Autowired
    private MoneyRepository moneyRepository;

    @BeforeEach
    void setUp() {
        super.setUpRestAssured();
        moneyRepository.deleteAll();
        balanceRepository.deleteAll();
    }

    @Test
    void shouldAddBalance() {
        Balance balance = Balance.builder()
                .accountId(1L)
                .okoBalance(BigDecimal.valueOf(1000))
                .creditBalance(BigDecimal.valueOf(500))
                .build();

        given()
                .contentType(ContentType.JSON)
                .body(balance)
                .when()
                .post("/api/balance/add")
                .then()
                .statusCode(201)
                .body("message", equalTo("Balance added"))
                .body("status", equalTo(201));
    }

    @Test
    void shouldGetEmptyBalanceWhenNoData() {
        given()
                .when()
                .get("/api/balance/getBalance")
                .then()
                .statusCode(200)
                .body("okoBalance", equalTo(0))
                .body("creditBalance", equalTo(0));
    }

    @Test
    void shouldGetBalanceAfterAdding() {
        Balance balance = Balance.builder()
                .accountId(1L)
                .okoBalance(BigDecimal.valueOf(2000))
                .creditBalance(BigDecimal.valueOf(1500))
                .build();

        given()
                .contentType(ContentType.JSON)
                .body(balance)
                .when()
                .post("/api/balance/add")
                .then()
                .statusCode(201);

        given()
                .when()
                .get("/api/balance/getBalance")
                .then()
                .statusCode(200)
                .body("okoBalance", equalTo(2000f))
                .body("creditBalance", equalTo(1500f));
    }

    @Test
    void shouldGetCombinedBalance() {
        Balance balance = Balance.builder()
                .accountId(1L)
                .okoBalance(BigDecimal.valueOf(3000))
                .creditBalance(BigDecimal.valueOf(2500))
                .build();

        given()
                .contentType(ContentType.JSON)
                .body(balance)
                .when()
                .post("/api/balance/add")
                .then()
                .statusCode(201);

        given()
                .when()
                .get("/api/balance/getCombinedBalance")
                .then()
                .statusCode(200)
                .body(equalTo("5500.00"));
    }
}
