package edu.dosw.rideci.unit.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;


import edu.dosw.rideci.application.port.in.CreateCreditCardUseCase;
import edu.dosw.rideci.application.port.in.DeleteCreditCardUseCase;
import edu.dosw.rideci.application.port.in.GetCreditCardUseCase;
import edu.dosw.rideci.application.port.in.SetDefaultCreditCardUseCase;
import edu.dosw.rideci.domain.model.CreditCard;
import edu.dosw.rideci.infrastructure.controller.CreditCardController;

class CreditCardControllerTest {

    private MockMvc mockMvc;

    @Mock private CreateCreditCardUseCase createUseCase;
    @Mock private GetCreditCardUseCase getUseCase;
    @Mock private DeleteCreditCardUseCase deleteUseCase;
    @Mock private SetDefaultCreditCardUseCase setDefaultUseCase;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);

        CreditCardController controller = new CreditCardController(
                createUseCase,
                getUseCase,
                deleteUseCase,
                setDefaultUseCase
        );

        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    private CreditCard mockCard() {
        return CreditCard.builder()
                .id("CARD-1")
                .userId("USR-10")
                .cardHolder("Juan Pérez")
                .cardNumber("4111111111111111")
                .expiration("12/29")
                .cvv("123")
                .alias("Personal")
                .isDefault(false)
                .isActive(true)
                .build();
    }

    @Test
    void testCreate() throws Exception {

        String requestJson = """
            {
              "userId": "USR-10",
              "cardHolder": "Juan Pérez",
              "cardNumber": "4111111111111111",
              "expiration": "12/29",
              "cvv": "123",
              "alias": "Personal"
            }
        """;

        when(createUseCase.create(any())).thenReturn(mockCard());

        mockMvc.perform(post("/api/credit-cards")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value("CARD-1"));
    }

    @Test
    void testFindAll() throws Exception {
        when(getUseCase.findAll()).thenReturn(List.of(mockCard()));

        mockMvc.perform(get("/api/credit-cards"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("CARD-1"));
    }

    @Test
    void testFindByUser() throws Exception {
        when(getUseCase.findByUser("USR-10")).thenReturn(List.of(mockCard()));

        mockMvc.perform(get("/api/credit-cards/user/USR-10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].userId").value("USR-10"));
    }

    @Test
    void testFindById() throws Exception {
        when(getUseCase.findById("CARD-1")).thenReturn(mockCard());

        mockMvc.perform(get("/api/credit-cards/CARD-1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("CARD-1"));
    }

    @Test
    void testSetDefault() throws Exception {
        var updated = mockCard();
        updated.setDefault(true);

        when(setDefaultUseCase.setDefault("CARD-1")).thenReturn(updated);

        mockMvc.perform(patch("/api/credit-cards/CARD-1/default"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isDefault").value(true));
    }

    @Test
    void testDeleteSuccess() throws Exception {
        when(deleteUseCase.delete("CARD-1")).thenReturn(true);

        mockMvc.perform(delete("/api/credit-cards/CARD-1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testDeleteNotFound() throws Exception {
        when(deleteUseCase.delete("CARD-999")).thenReturn(false);

        mockMvc.perform(delete("/api/credit-cards/CARD-999"))
                .andExpect(status().isNotFound());
    }
}
