package com.reservas.controller;

import com.reservas.dto.ServicioResponse;
import com.reservas.service.ServicioService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ServicioController.class)
@AutoConfigureMockMvc(addFilters = false)
class ServicioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ServicioService servicioService;

    @Test
    void obtenerServicios_debeRetornarListaDeServiciosActivos() throws Exception {
        ServicioResponse servicio = new ServicioResponse(
                1L,
                "Consulta General",
                25,
                "Consulta medica general",
                30,
                true,
                LocalDateTime.now()
        );

        when(servicioService.obtenerServiciosActivos()).thenReturn(List.of(servicio));

        mockMvc.perform(get("/api/servicios"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombreServicio").value("Consulta General"))
                .andExpect(jsonPath("$[0].precio").value(25));
    }
}
