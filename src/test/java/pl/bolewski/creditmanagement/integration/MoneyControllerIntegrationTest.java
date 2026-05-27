package pl.bolewski.creditmanagement.integration;

import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import pl.bolewski.creditmanagement.dto.MoneyDTO;
import pl.bolewski.creditmanagement.model.AccountType;
import pl.bolewski.creditmanagement.repository.BalanceRepository;
import pl.bolewski.creditmanagement.repository.MoneyRepository;

import java.math.BigDecimal;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

class MoneyControllerIntegrationTest extends BaseIntegrationTest {

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
    void shouldDepositMoney() {
        MoneyDTO moneyDTO = new MoneyDTO(
                BigDecimal.valueOf(1000),
                AccountType.OKO,
                "January"
        );

        given()
                .contentType(ContentType.JSON)
                .body(moneyDTO)
                .when()
                .post("/api/money/deposit")
                .then()
                .statusCode(201)
                .body("message", equalTo("DEPOSIT - Money processed successfully"))
                .body("status", equalTo(201));
    }

    @Test
    void shouldDepositMoneyList() {
        List<MoneyDTO> moneyDTOList = List.of(
                new MoneyDTO(BigDecimal.valueOf(500), AccountType.OKO, "February"),
                new MoneyDTO(BigDecimal.valueOf(750), AccountType.CREDIT, "March")
        );

        given()
                .contentType(ContentType.JSON)
                .body(moneyDTOList)
                .when()
                .post("/api/money/deposit-list")
                .then()
                .statusCode(201)
                .body("message", equalTo("DEPOSIT - Money list processed successfully"))
                .body("status", equalTo(201));
    }

    @Test
    void shouldWithdrawMoney() {
        MoneyDTO moneyDTO = new MoneyDTO(
                BigDecimal.valueOf(200),
                AccountType.CREDIT,
                "April"
        );

        given()
                .contentType(ContentType.JSON)
                .body(moneyDTO)
                .when()
                .post("/api/money/withdraw")
                .then()
                .statusCode(201)
                .body("message", equalTo("WITHDRAW - Money processed successfully"))
                .body("status", equalTo(201));
    }

    @Test
    void shouldGetMoneyHistory() {
        MoneyDTO moneyDTO = new MoneyDTO(
                BigDecimal.valueOf(1500),
                AccountType.OKO,
                "May"
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
                .get("/api/money/getHistory")
                .then()
                .statusCode(200)
                .body(".", hasSize(1));
    }
}
