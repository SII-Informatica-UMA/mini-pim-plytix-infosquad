package minipimplytixinfosquad.entidades.controllers;

import minipimplytixinfosquad.entidades.dtos.PlanDTO;
import minipimplytixinfosquad.entidades.entities.Plan;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class PlanMapperTest {

    @Test
    @DisplayName("Conversion de Plan a PlanDTO correctamente")
    void toDTO_OK() {
        Plan plan = Plan.builder()
                .id(1L).nombre("Básico")
                .maxProductos(5).maxActivos(10)
                .maxAlmacenamiento(500)
                .maxCategoriasProductos(2)
                .maxCategoriasActivos(3)
                .maxRelaciones(1)
                .precio(new BigDecimal("12.50"))
                .build();

        PlanDTO dto = PlanMapper.toDTO(plan);

        assertEquals("Básico", dto.getNombre());
        assertEquals(1L, dto.getId());
        assertEquals(BigDecimal.valueOf(12.5), dto.getPrecio());
    }

    @Test
    @DisplayName("Conversion a DTO con Plan null devuelve null")
    void toDTO_planNullDevuelveNull() {
        assertNull(PlanMapper.toDTO(null));
    }

    @Test
    @DisplayName("Conversion a DTO con precio null en Plan devuelve precio cero")
    void toDTO_precioNullDaCero() {
        Plan plan = Plan.builder()
            .id(1L).nombre("PlanX")
            .maxProductos(1).maxActivos(1).maxAlmacenamiento(1)
            .maxCategoriasProductos(1).maxCategoriasActivos(1)
            .maxRelaciones(1)
            .build(); // precio es null

        PlanDTO dto = PlanMapper.toDTO(plan);
        assertEquals(BigDecimal.ZERO, dto.getPrecio());
    }

    @Test
    @DisplayName("Conversion de PlanDTO a Plan correctamente")
    void toEntity_OK() {
        PlanDTO dto = PlanDTO.builder()
            .id(2L).nombre("Pro")
            .maxProductos(10).maxActivos(20)
            .maxAlmacenamiento(1000)
            .maxCategoriasProductos(4)
            .maxCategoriasActivos(5)
            .maxRelaciones(2)
            .precio(new BigDecimal("15.00"))
            .build();

        Plan plan = PlanMapper.toEntity(dto);

        assertEquals("Pro", plan.getNombre());
        assertEquals(0, new BigDecimal(plan.getPrecio().toString()).compareTo(BigDecimal.valueOf(15.00)));
        assertEquals(2L, plan.getId());
    }

    @Test
    @DisplayName("Conversion a Plan con DTO null devuelve null")
    void toEntity_dtoNullDevuelveNull() {
        assertNull(PlanMapper.toEntity(null));
    }
}
