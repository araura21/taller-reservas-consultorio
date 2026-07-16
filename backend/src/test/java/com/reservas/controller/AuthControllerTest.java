package com.reservas.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.reservas.dto.LoginRequest;
import com.reservas.dto.LoginResponse;
import com.reservas.service.AuthService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthService authService;

    @Test
    void login_debeRetornar200CuandoCredencialesSonValidas() throws Exception {
        LoginRequest request = new LoginRequest("admin@reservas.com", "password");
        LoginResponse response = new LoginResponse(
                "admin-token-123",
                "Administrador",
                "admin@reservas.com",
                "Administrador"
        );

        when(authService.login(any(LoginRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("admin-token-123"))
                .andExpect(jsonPath("$.email").value("admin@reservas.com"));
    }

    @Test
    void login_debeRetornar400CuandoCredencialesSonInvalidas() throws Exception {
        LoginRequest request = new LoginRequest("admin@reservas.com", "incorrecta");

        when(authService.login(any(LoginRequest.class)))
                .thenThrow(new RuntimeException("Contraseña incorrecta"));

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Credenciales inválidas"));
    }

        @Test
        void validateToken_debeRetornarTrueCuandoTokenEsValido() throws Exception {
                when(authService.validarToken("token-valido")).thenReturn(true);

                mockMvc.perform(post("/api/auth/validate")
                                                .header("Authorization", "Bearer token-valido"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$").value(true));
        }
}
