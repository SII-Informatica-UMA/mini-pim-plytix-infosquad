package minipimplytixinfosquad.entidades.controllers;

import minipimplytixinfosquad.entidades.dtos.PlanDTO;
import minipimplytixinfosquad.entidades.entities.Plan;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class PlanMapperTest {

    @Test
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
    void toDTO_planNullDevuelveNull() {
        assertNull(PlanMapper.toDTO(null));
    }

    @Test
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
    void toEntity_dtoNullDevuelveNull() {
        assertNull(PlanMapper.toEntity(null));
    }
}
