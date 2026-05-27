package pl.bolewski.creditmanagement.controller;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;
import pl.bolewski.creditmanagement.dto.BalanceDTO;
import pl.bolewski.creditmanagement.model.Balance;
import pl.bolewski.creditmanagement.service.BalanceService;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("Balance added"))
                .andExpect(jsonPath("$.status").value(201));
    }

    @Test
    void getBalanceTest() throws Exception {
        BalanceDTO balanceDTO = BalanceDTO.builder()
                .okoBalance(BigDecimal.valueOf(1000))
                .creditBalance(BigDecimal.valueOf(500))
                .build();

        Mockito.when(balanceService.getBalanceDto()).thenReturn(balanceDTO);

        mockMvc.perform(get("/api/balance/getBalance"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.okoBalance").value(1000))
                .andExpect(jsonPath("$.creditBalance").value(500));
    }

    @Test
    void getCombinedBalanceTest() throws Exception {
        Mockito.when(balanceService.getCombinedBalance()).thenReturn(BigDecimal.valueOf(10000));

        mockMvc.perform(get("/api/balance/getCombinedBalance"))
                .andExpect(status().isOk())
                .andExpect(content().string("10000"));
    }
}