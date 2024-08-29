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
    void addMoneyTest() throws Exception {
        Mockito.doNothing().when(moneyService).addMoney(Mockito.any(MoneyDTO.class));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/money/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"cash\": 100, \"account\": \"account1\", \"month\": \"January\" }"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Cash added"))
                .andExpect(jsonPath("$.status").value(200));
    }

    @Test
    void withdrawMoneyTest() throws Exception {
        Mockito.doNothing().when(moneyService).withdrawMoney(Mockito.any(MoneyDTO.class));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/money/withdraw")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"cash\": 100, \"account\": \"account1\", \"month\": \"January\" }"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Cash withdrew"))
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