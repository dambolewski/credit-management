package pl.bolewski.credit_management.controller;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import pl.bolewski.credit_management.dto.MonthVerifierDTO;
import pl.bolewski.credit_management.dto.YearlyVerifierDTO;
import pl.bolewski.credit_management.service.CreditService;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CreditController.class)
class CreditControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CreditService creditService;

    @Test
    void getMonthlyStatusTest() throws Exception {
        MonthVerifierDTO mockResponse = new MonthVerifierDTO();
        mockResponse.setMonth("08");
        mockResponse.setNeeded(BigDecimal.valueOf(1835));
        mockResponse.setCollected(BigDecimal.ZERO);
        mockResponse.setStatus(BigDecimal.valueOf(-1835));

        Mockito.when(creditService.checkMonthlyPayouts("2023", "08")).thenReturn(mockResponse);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/credit/checkMonthly/2023/08"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.month").value("08"))
                .andExpect(jsonPath("$.needed").value(1835))
                .andExpect(jsonPath("$.collected").value(0))
                .andExpect(jsonPath("$.status").value(-1835));
    }

    @Test
    void getYearlyStatusTest() throws Exception {
        YearlyVerifierDTO mockResponse = new YearlyVerifierDTO();
        mockResponse.setYear("2024");
        mockResponse.setNeeded(BigDecimal.valueOf(16515));
        mockResponse.setCollected(BigDecimal.ZERO);
        mockResponse.setStatus(BigDecimal.valueOf(-16515));

        Mockito.when(creditService.checkYearlyPayouts("2024")).thenReturn(mockResponse);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/credit/checkYearly/2024"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.year").value("2024"))
                .andExpect(jsonPath("$.needed").value(16515))
                .andExpect(jsonPath("$.collected").value(0))
                .andExpect(jsonPath("$.status").value(-16515));
    }
}