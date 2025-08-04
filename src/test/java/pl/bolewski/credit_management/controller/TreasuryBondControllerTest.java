package pl.bolewski.credit_management.controller;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import pl.bolewski.credit_management.service.TreasuryBondService;

import java.math.BigDecimal;
import java.util.Collections;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TreasuryBondController.class)
class TreasuryBondControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TreasuryBondService treasuryBondService;

    @Test
    void checkDepositedAmount() throws Exception {
        Mockito.when(treasuryBondService.calculateDepositedTreasuryBond()).thenReturn(BigDecimal.valueOf(10000));

        mockMvc.perform(get("/api/treasuryBond/checkDepositedAmount"))
                .andExpect(status().isOk())
                .andExpect(content().string("10000"));
    }

    @Test
    void getTreasuryBonds() throws Exception {
        Mockito.when(treasuryBondService.getTreasuryBondList()).thenReturn(Collections.emptyList());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/treasuryBond/getTreasuryBonds"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }
}