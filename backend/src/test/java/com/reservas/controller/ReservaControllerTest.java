package com.reservas.controller;

import com.reservas.dto.ReservaResponse;
import com.reservas.service.ReservaService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ReservaController.class)
@AutoConfigureMockMvc(addFilters = false)
class ReservaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReservaService reservaService;

    @Test
    void obtenerReservas_debeFiltrarPorEstado() throws Exception {
        ReservaResponse reserva = new ReservaResponse();
        reserva.setIdReserva(1L);
        reserva.setNombreCliente("Juan Perez");
        reserva.setEstado("Pendiente");
        reserva.setFecha(LocalDate.of(2026, 7, 20));
        reserva.setHora(LocalTime.of(10, 0));

        when(reservaService.obtenerReservasPorEstado("Pendiente")).thenReturn(List.of(reserva));

        mockMvc.perform(get("/api/reservas").param("estado", "Pendiente"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].estado").value("Pendiente"))
                .andExpect(jsonPath("$[0].nombreCliente").value("Juan Perez"));
    }
}
