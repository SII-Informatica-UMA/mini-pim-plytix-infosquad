package minipimplytixinfosquad.entidades.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import minipimplytixinfosquad.entidades.dtos.PlanDTO;
import minipimplytixinfosquad.entidades.entities.Plan;
import minipimplytixinfosquad.entidades.security.JwtRequestFilter;
import minipimplytixinfosquad.entidades.security.JwtUtil;
import minipimplytixinfosquad.entidades.services.PlanService;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.*;
import static org.springframework.boot.autoconfigure.security.servlet.SecurityFilterAutoConfiguration.*;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityFilterAutoConfiguration;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(value = PlanController.class,
            excludeAutoConfiguration = {SecurityAutoConfiguration.class, SecurityFilterAutoConfiguration.class})
@AutoConfigureMockMvc(addFilters = false)
class PlanControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;

    @MockBean private PlanService planService;

    @MockBean
    private JwtRequestFilter jwtRequestFilter;

    @MockBean
    private JwtUtil jwtUtil;

    /* ---------- GET ---------- */
    @Test
    @DisplayName("GET /plan devuelve lista DTO")
    void testListarPlanes() throws Exception {

        Plan plan = new Plan(); plan.setId(1L); plan.setNombre("Pro");
        PlanDTO dto = PlanDTO.builder().id(1L).nombre("Pro").build();

        when(planService.listarPlanes()).thenReturn(List.of(plan));

        try (MockedStatic<PlanMapper> mocked = Mockito.mockStatic(PlanMapper.class)) {
            mocked.when(() -> PlanMapper.toDTO(plan)).thenReturn(dto);

            mockMvc.perform(get("/plan"))
                   .andExpect(status().isOk())
                   .andExpect(jsonPath("$", hasSize(1)))
                   .andExpect(jsonPath("$[0].nombre").value("Pro"));
        }
    }

    /* ---------- POST ---------- */
    @Test
    @DisplayName("POST /plan crea plan")
    void testCrearPlan() throws Exception {

        Plan entrada = new Plan(); entrada.setNombre("Premium");
        Plan creado  = new Plan(); creado.setId(5L); creado.setNombre("Premium");
        PlanDTO dto  = PlanDTO.builder().id(5L).nombre("Premium").build();

        when(planService.crearPlan(any(Plan.class))).thenReturn(creado);

        try (MockedStatic<PlanMapper> mocked = Mockito.mockStatic(PlanMapper.class)) {
            mocked.when(() -> PlanMapper.toDTO(creado)).thenReturn(dto);

            mockMvc.perform(post("/plan")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(entrada)))
                   .andExpect(status().isOk())
                   .andExpect(jsonPath("$.id").value(5L))
                   .andExpect(jsonPath("$.nombre").value("Premium"));
        }
    }

    /* ---------- PUT ---------- */
    @Test
    @DisplayName("PUT /plan/{id} actualiza plan")
    void testActualizarPlan() throws Exception {

        Plan entrada = new Plan(); entrada.setNombre("Nuevo");
        Plan actualizado = new Plan(); actualizado.setId(7L); actualizado.setNombre("Nuevo");
        PlanDTO dto = PlanDTO.builder().id(7L).nombre("Nuevo").build();

        when(planService.actualizarPlan(7L, entrada)).thenReturn(actualizado);

        try (MockedStatic<PlanMapper> mocked = Mockito.mockStatic(PlanMapper.class)) {
            mocked.when(() -> PlanMapper.toDTO(actualizado)).thenReturn(dto);

            mockMvc.perform(put("/plan/7")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(entrada)))
                   .andExpect(status().isOk())
                   .andExpect(jsonPath("$.id").value(7L))
                   .andExpect(jsonPath("$.nombre").value("Nuevo"));
        }
    }

    /* ---------- DELETE ---------- */
    @Test
    @DisplayName("DELETE /plan/{id} → 204")
    void testEliminarPlan() throws Exception {
        mockMvc.perform(delete("/plan/3"))
               .andExpect(status().isNoContent());

        Mockito.verify(planService).eliminarPlan(3L);
    }

    //--------------------------------------
}
