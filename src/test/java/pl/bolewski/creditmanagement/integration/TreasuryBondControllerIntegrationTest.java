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

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

class TreasuryBondControllerIntegrationTest extends BaseIntegrationTest {

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
    void shouldGetEmptyTreasuryBondsWhenNoDeposits() {
        given()
                .when()
                .get("/api/treasuryBond/getTreasuryBonds")
                .then()
                .statusCode(200)
                .body(".", hasSize(0));
    }

    @Test
    void shouldGetTreasuryBondsAfterDeposit() {
        MoneyDTO moneyDTO = new MoneyDTO(
                BigDecimal.valueOf(500),
                AccountType.TREASURY_BOND,
                "January"
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
                .get("/api/treasuryBond/getTreasuryBonds")
                .then()
                .statusCode(200)
                .body(".", hasSize(1))
                .body("[0].month", equalTo("January"))
                .body("[0].treasuryBond", notNullValue());
    }

    @Test
    void shouldCalculateDepositedTreasuryBondAmount() {
        MoneyDTO moneyDTO1 = new MoneyDTO(
                BigDecimal.valueOf(1000),
                AccountType.TREASURY_BOND,
                "February"
        );
        MoneyDTO moneyDTO2 = new MoneyDTO(
                BigDecimal.valueOf(1500),
                AccountType.TREASURY_BOND,
                "March"
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
                .get("/api/treasuryBond/getDepositedAmount")
                .then()
                .statusCode(200)
                .body(equalTo("2500.00"));
    }
}
