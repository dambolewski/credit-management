package pl.bolewski.credit_management.service;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import pl.bolewski.credit_management.dto.MoneyDTO;
import pl.bolewski.credit_management.model.Money;
import pl.bolewski.credit_management.repository.MoneyRepository;
import pl.bolewski.credit_management.testcontainers.TestcontainersSetup;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DirtiesContext
class MoneyServiceTest extends TestcontainersSetup {

    private final MoneyService moneyService;
    private final MoneyRepository moneyRepository;

    @Autowired
    MoneyServiceTest(MoneyService moneyService, MoneyRepository moneyRepository) {
        this.moneyService = moneyService;
        this.moneyRepository = moneyRepository;
    }

    @BeforeEach
    void setUp() {
        moneyRepository.deleteAll();
    }

    @Test
    void depositMoney() {
        MoneyDTO moneyDTO = new MoneyDTO(BigDecimal.valueOf(1000), "oko", "07");

        moneyService.depositMoney(moneyDTO);

        List<Money> moneyList = (List<Money>) moneyRepository.findAll();
        assertFalse(moneyList.isEmpty());
        assertEquals(0, BigDecimal.valueOf(1000).compareTo(moneyList.get(0).getCash()));
    }

    @Test
    void depositMoneyList() {
        MoneyDTO moneyDTO = new MoneyDTO(BigDecimal.valueOf(1000), "oko", "07");
        MoneyDTO moneyDTO2 = new MoneyDTO(BigDecimal.valueOf(2000), "oko", "07");

        moneyService.depositMoneyList(List.of(moneyDTO,moneyDTO2));

        List<Money> moneyList = (List<Money>) moneyRepository.findAll();
        assertFalse(moneyList.isEmpty());
        assertEquals(0, BigDecimal.valueOf(1000).compareTo(moneyList.get(0).getCash()));
        assertEquals(0, BigDecimal.valueOf(2000).compareTo(moneyList.get(1).getCash()));
    }

    @Test
    void withdrawMoney() {
        MoneyDTO moneyDTO = new MoneyDTO(BigDecimal.valueOf(500), "oko", "07");

        moneyService.withdrawMoney(moneyDTO);

        List<Money> moneyList = (List<Money>) moneyRepository.findAll();
        assertFalse(moneyList.isEmpty());
        assertEquals(0, BigDecimal.valueOf(500).compareTo(moneyList.get(0).getCash()));
    }

    @Test
    void withdrawMoneyList() {
        MoneyDTO moneyDTO = new MoneyDTO(BigDecimal.valueOf(1000), "oko", "07");
        MoneyDTO moneyDTO2 = new MoneyDTO(BigDecimal.valueOf(2000), "oko", "07");

        moneyService.withdrawMoneyList(List.of(moneyDTO,moneyDTO2));

        List<Money> moneyList = (List<Money>) moneyRepository.findAll();
        assertFalse(moneyList.isEmpty());
        assertEquals(0, BigDecimal.valueOf(1000).compareTo(moneyList.get(0).getCash()));
        assertEquals(0, BigDecimal.valueOf(2000).compareTo(moneyList.get(1).getCash()));
    }

    @Test
    void getMoney() {
        MoneyDTO moneyDTO = new MoneyDTO(BigDecimal.valueOf(1500), "credit", "07");
        moneyService.depositMoney(moneyDTO);

        List<Money> moneyList = moneyService.getMoney();

        assertFalse(moneyList.isEmpty());
        assertEquals(1, moneyList.size());
    }

    @Test
    void getMoneyByYearAndMonth() {
        MoneyDTO moneyDTO1 = new MoneyDTO(BigDecimal.valueOf(1000), "credit", "07");
        MoneyDTO moneyDTO2 = new MoneyDTO(BigDecimal.valueOf(2000), "credit", "07");
        moneyService.depositMoney(moneyDTO1);
        moneyService.depositMoney(moneyDTO2);

        Optional<List<Money>> result = moneyService.getMoneyByYearAndMonth(String.valueOf(LocalDate.now().getYear()), "07");

        assertTrue(result.isPresent());
        assertEquals(2, result.get().size());
    }

    @Test
    void getMoneyByYear() {
        MoneyDTO moneyDTO1 = new MoneyDTO(BigDecimal.valueOf(3000), "credit", "07");
        MoneyDTO moneyDTO2 = new MoneyDTO(BigDecimal.valueOf(4000), "credit", "08");
        moneyService.depositMoney(moneyDTO1);
        moneyService.depositMoney(moneyDTO2);

        Optional<List<Money>> result = moneyService.getMoneyByYear(String.valueOf(LocalDate.now().getYear()));

        assertTrue(result.isPresent());
        assertEquals(2, result.get().size());
    }
}
