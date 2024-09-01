package pl.bolewski.credit_management.controller;


import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import pl.bolewski.credit_management.dto.MoneyDTO;
import pl.bolewski.credit_management.service.MoneyService;

import java.util.Collections;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MoneyController.class)
class MoneyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MoneyService moneyService;

    @Test
    void depositMoneyTest() throws Exception {
        Mockito.doNothing().when(moneyService).depositMoney(Mockito.any(MoneyDTO.class));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/money/deposit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"cash\": 100, \"account\": \"account1\", \"month\": \"January\" }"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("DEPOSIT - Money processed successfully"))
                .andExpect(jsonPath("$.status").value(200));
    }

    @Test
    void depositMoneyListTest() throws Exception {
        Mockito.doNothing().when(moneyService).depositMoneyList(Collections.singletonList(Mockito.any(MoneyDTO.class)));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/money/deposit-list")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("[{ \"cash\": 100, \"account\": \"account1\", \"month\": \"January\" },{ \"cash\": 100, \"account\": \"account1\", \"month\": \"January\" }]"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("DEPOSIT - Money list processed successfully"))
                .andExpect(jsonPath("$.status").value(200));
    }

    @Test
    void withdrawMoneyTest() throws Exception {
        Mockito.doNothing().when(moneyService).withdrawMoney(Mockito.any(MoneyDTO.class));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/money/withdraw")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"cash\": 100, \"account\": \"account1\", \"month\": \"January\" }"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("WITHDRAW - Money processed successfully"))
                .andExpect(jsonPath("$.status").value(200));
    }

    @Test
    void withdrawMoneyListTest() throws Exception {
        Mockito.doNothing().when(moneyService).withdrawMoneyList(Collections.singletonList(Mockito.any(MoneyDTO.class)));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/money/withdraw-list")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("[{ \"cash\": 100, \"account\": \"account1\", \"month\": \"January\" },{ \"cash\": 100, \"account\": \"account1\", \"month\": \"January\" }]"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("WITHDRAW - Money list processed successfully"))
                .andExpect(jsonPath("$.status").value(200));
    }

    @Test
    void getMoneyHistoryTest() throws Exception {
        Mockito.when(moneyService.getMoney()).thenReturn(Collections.emptyList());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/money/getHistory"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }
}