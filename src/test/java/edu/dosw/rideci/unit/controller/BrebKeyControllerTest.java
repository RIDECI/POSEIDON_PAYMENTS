package edu.dosw.rideci.unit.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import edu.dosw.rideci.application.port.in.*;
import edu.dosw.rideci.domain.model.BrebKey;
import edu.dosw.rideci.domain.model.enums.BrebKeyType;
import edu.dosw.rideci.infrastructure.controller.BrebKeyController;

@WebMvcTest(
    controllers = BrebKeyController.class,
    excludeAutoConfiguration = {
        org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class,
        org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration.class
})
@AutoConfigureMockMvc(addFilters = false)
class BrebKeyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean   // <-- REEMPLAZA @MockBean
    private CreateBrebKeyUseCase createUseCase;

    @MockitoBean
    private GetBrebKeyUseCase getUseCase;

    @MockitoBean
    private DeleteBrebKeyUseCase deleteUseCase;

    @MockitoBean
    private SetDefaultBrebKeyUseCase setDefaultUseCase;

    private BrebKey sample() {
        return BrebKey.builder()
                .id("123")
                .userId("U1")
                .value("test@example.com")
                .type(BrebKeyType.EMAIL)
                .isDefault(true)
                .build();
    }

    @Test
    void testFindById() throws Exception {
        when(getUseCase.findById("123")).thenReturn(sample());

        mockMvc.perform(get("/api/breb-keys/123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("123"))
                .andExpect(jsonPath("$.isDefault").value(true));
    }

    @Test
    void testFindAll() throws Exception {
        when(getUseCase.findAll()).thenReturn(List.of(sample()));

        mockMvc.perform(get("/api/breb-keys"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("123"));
    }

    @Test
    void testSetDefault() throws Exception {
        when(setDefaultUseCase.setDefault("123")).thenReturn(sample());

        mockMvc.perform(patch("/api/breb-keys/123/default"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("123"))
                .andExpect(jsonPath("$.isDefault").value(true));
    }

    @Test
    void testDelete() throws Exception {
        when(deleteUseCase.delete("123")).thenReturn(true);

        mockMvc.perform(delete("/api/breb-keys/123"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testDeleteNotFound() throws Exception {
        when(deleteUseCase.delete("999")).thenReturn(false);

        mockMvc.perform(delete("/api/breb-keys/999"))
                .andExpect(status().isNotFound());
    }
}
