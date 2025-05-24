package minipimplytixinfosquad.entidades.controllers;

import minipimplytixinfosquad.entidades.dtos.CuentaDTO;
import minipimplytixinfosquad.entidades.dtos.CuentaNuevaDTO;
import minipimplytixinfosquad.entidades.dtos.PlanDTO;
import minipimplytixinfosquad.entidades.entities.Cuenta;
import minipimplytixinfosquad.entidades.entities.Plan;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CuentaMapperTest {

    @Test
    void toDTO_OK() {
        Plan plan = Plan.builder()
                .id(1L).nombre("Pro")
                .maxProductos(5).maxActivos(10).maxAlmacenamiento(500)
                .maxCategoriasProductos(2).maxCategoriasActivos(3).maxRelaciones(1)
                .precio(new BigDecimal("19.99"))
                .build();

        Cuenta cuenta = Cuenta.builder()
                .id(100L)
                .nombre("MiCuenta")
                .direccion("Calle Falsa 123")
                .nif("X1234567Y")
                .fechaAlta(new Date())
                .plan(plan)
                .build();

        CuentaDTO dto = CuentaMapper.toDTO(cuenta);

        assertEquals("MiCuenta", dto.getNombre());
        assertEquals("X1234567Y", dto.getNif());
        assertEquals("Pro", dto.getPlan().getNombre());
        assertEquals(1L, dto.getPlan().getId());
        assertEquals(BigDecimal.valueOf(19.99), dto.getPlan().getPrecio());
    }

    @Test
    void toDTO_fechaAltaNula() {
        Plan plan = Plan.builder()
            .id(1L).nombre("PlanX")
            .maxProductos(1).maxActivos(1).maxAlmacenamiento(1)
            .maxCategoriasProductos(1).maxCategoriasActivos(1)
            .maxRelaciones(1).precio(BigDecimal.ZERO)
            .build();

        Cuenta cuenta = Cuenta.builder()
            .id(1L)
            .nombre("CuentaX")
            .plan(plan)
            .build();

        CuentaDTO dto = CuentaMapper.toDTO(cuenta);
        assertNull(dto.getFechaAlta());
    }

    @Test
    void toEntity_OK() {
        CuentaNuevaDTO dto = new CuentaNuevaDTO();
        dto.setNombre("C");
        dto.setDireccion("Dir");
        dto.setNif("123A");
        dto.setFechaAlta(LocalDate.of(2024, 1, 1));
        dto.setPropietarioId(10L);
        dto.setUsuariosIds(List.of(10L, 11L));

        Plan plan = Plan.builder().id(1L).nombre("Pro").build();

        Cuenta cuenta = CuentaMapper.toEntity(dto, plan);

        assertEquals("C", cuenta.getNombre());
        assertEquals("Dir", cuenta.getDireccion());
        assertEquals("123A", cuenta.getNif());
        assertNotNull(cuenta.getFechaAlta());
        assertEquals(10L, cuenta.getPropietarioId());
        assertEquals(2, cuenta.getUsuariosIds().size());
    }

    @Test
    void toEntity_fechaAltaNullUsaActual() {
        CuentaNuevaDTO dto = new CuentaNuevaDTO();
        dto.setNombre("C");
        dto.setPlan(CuentaNuevaDTO.PlanIdDTO.builder().id(1L).build());

        Cuenta cuenta = CuentaMapper.toEntity(dto, Plan.builder().id(1L).build());

        assertNotNull(cuenta.getFechaAlta());
    }
}

