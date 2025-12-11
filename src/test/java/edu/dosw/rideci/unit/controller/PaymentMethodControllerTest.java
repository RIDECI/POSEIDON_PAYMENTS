package edu.dosw.rideci.unit.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import edu.dosw.rideci.application.port.in.*;
import edu.dosw.rideci.domain.model.PaymentMethod;
import edu.dosw.rideci.domain.model.enums.PaymentMethodType;
import edu.dosw.rideci.infrastructure.controller.PaymentMethodController;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

@Import(PaymentMethodController.class)
class PaymentMethodControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CreatePaymentMethodUseCase createUseCase;

    @Mock
    private DeletePaymentMethodUseCase deleteUseCase;

    @Mock
    private GetPaymentMethodsUseCase getUseCase;

    @Mock
    private SetDefaultPaymentMethodUseCase setDefaultUseCase;

    @InjectMocks
    private PaymentMethodController controller;


    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void testGetById() throws Exception {

        PaymentMethod pm = PaymentMethod.builder()
                .id("pm1")
                .userId("u1")
                .alias("MyCard")
                .isDefault(true)
                .isActive(true)
                .type(PaymentMethodType.CREDIT_CARD_PAYU)
                .build();

        when(getUseCase.getById("pm1")).thenReturn(pm);

        mockMvc.perform(get("/api/payment-methods/pm1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("pm1"))
                .andExpect(jsonPath("$.alias").value("MyCard"))
                .andExpect(jsonPath("$.isDefault").value(true))
                .andExpect(jsonPath("$.type").value("CREDIT_CARD_PAYU"));
    }


    @Test
    void testSetDefault() throws Exception {

        PaymentMethod pm = PaymentMethod.builder()
                .id("pm99")
                .userId("u1")
                .alias("NEQUI")
                .isDefault(true)
                .isActive(true)
                .type(PaymentMethodType.NEQUI)
                .build();

        when(setDefaultUseCase.setDefault("pm99")).thenReturn(pm);

        mockMvc.perform(patch("/api/payment-methods/pm99/default"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("pm99"))
                .andExpect(jsonPath("$.isDefault").value(true))
                .andExpect(jsonPath("$.type").value("NEQUI"));
    }


    @Test
    void testGetByUser() throws Exception {

        PaymentMethod pm = PaymentMethod.builder()
                .id("pm10")
                .userId("uABC")
                .alias("BREB")
                .isDefault(false)
                .isActive(true)
                .type(PaymentMethodType.BRE_B_key)
                .build();

        when(getUseCase.getByUserId("uABC")).thenReturn(List.of(pm));

        mockMvc.perform(get("/api/payment-methods/user/uABC"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("pm10"))
                .andExpect(jsonPath("$[0].type").value("BRE_B_key"));
    }

    @Test
    void testFindAll() throws Exception {

        PaymentMethod pm = PaymentMethod.builder()
                .id("global")
                .userId("uX")
                .alias("Cash")
                .isDefault(false)
                .isActive(true)
                .type(PaymentMethodType.CASH)
                .build();

        when(getUseCase.findAll()).thenReturn(List.of(pm));

        mockMvc.perform(get("/api/payment-methods"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].type").value("CASH"));
    }

    @Test
    void testDeleteOk() throws Exception {

        when(deleteUseCase.delete("pm1")).thenReturn(true);

        mockMvc.perform(delete("/api/payment-methods/pm1"))
                .andExpect(status().isNoContent());
    }
    
    @Test
    void testDeleteNotFound() throws Exception {

        when(deleteUseCase.delete("pm404")).thenReturn(false);

        mockMvc.perform(delete("/api/payment-methods/pm404"))
                .andExpect(status().isNotFound());
    }
}
