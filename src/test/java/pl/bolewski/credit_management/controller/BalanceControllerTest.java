package pl.bolewski.credit_management.controller;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;
import pl.bolewski.credit_management.dto.BalanceDTO;
import pl.bolewski.credit_management.model.Balance;
import pl.bolewski.credit_management.service.BalanceService;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BalanceController.class)
class BalanceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BalanceService balanceService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void addBalanceTest() throws Exception {
        Balance balance = Balance.builder()
                .accountId(1L)
                .okoBalance(BigDecimal.valueOf(1000))
                .creditBalance(BigDecimal.valueOf(500))
                .build();

        Mockito.doNothing().when(balanceService).addBalance(Mockito.any(Balance.class));

        mockMvc.perform(post("/api/balance/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(balance)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Balance added"))
                .andExpect(jsonPath("$.status").value(200));
    }

    @Test
    void getWholeBalanceTest() throws Exception {
        BalanceDTO balanceDTO = BalanceDTO.builder()
                .okoBalance(BigDecimal.valueOf(1000))
                .creditBalance(BigDecimal.valueOf(500))
                .build();

        Mockito.when(balanceService.getWholeBalance()).thenReturn(balanceDTO);

        mockMvc.perform(get("/api/balance/getBalance"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.okoBalance").value(1000))
                .andExpect(jsonPath("$.creditBalance").value(500));
    }
}