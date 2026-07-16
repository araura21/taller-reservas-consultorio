package com.reservas.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.reservas.dto.ReservaRequest;
import com.reservas.dto.ReservaResponse;
import com.reservas.service.ReservaService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ReservaController.class)
@AutoConfigureMockMvc(addFilters = false)
class ReservaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

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

    @Test
    void crearReserva_debeRetornar201CuandoDatosValidos() throws Exception {
        ReservaRequest request = new ReservaRequest();
        request.setNombreCliente("Maria Garcia");
        request.setFecha(LocalDate.of(2026, 7, 25));
        request.setHora(LocalTime.of(14, 30));
        request.setIdServicio(1L);

        ReservaResponse response = new ReservaResponse();
        response.setIdReserva(2L);
        response.setNombreCliente("Maria Garcia");
        response.setEstado("Confirmada");
        response.setFecha(LocalDate.of(2026, 7, 25));
        response.setHora(LocalTime.of(14, 30));

        when(reservaService.crearReserva(any(ReservaRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/reservas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.idReserva").value(2L))
                .andExpect(jsonPath("$.nombreCliente").value("Maria Garcia"))
                .andExpect(jsonPath("$.estado").value("Confirmada"));
    }

    @Test
    void obtenerReservas_debeRetornarListaVaciaParaEstadoSinReservas() throws Exception {
        when(reservaService.obtenerReservasPorEstado("Cancelada")).thenReturn(List.of());

        mockMvc.perform(get("/api/reservas").param("estado", "Cancelada"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }
}
