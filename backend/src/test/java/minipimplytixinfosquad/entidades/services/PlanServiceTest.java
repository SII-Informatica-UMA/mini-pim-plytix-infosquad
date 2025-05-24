package minipimplytixinfosquad.entidades.services;

import minipimplytixinfosquad.entidades.entities.Plan;
import minipimplytixinfosquad.entidades.repositories.PlanRepository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PlanServiceTest {

    @Mock
    private PlanRepository planRepository;

    @InjectMocks
    private PlanService planService;

    @Test
    @DisplayName("Debe listar todos los planes")
    void testListarPlanes() {

        when(planRepository.findAll()).thenReturn(List.of(new Plan(), new Plan()));

        List<Plan> resultado = planService.listarPlanes();

        assertEquals(2, resultado.size());
        verify(planRepository).findAll();
    }

    @Test
    @DisplayName("Debe crear un plan correctamente")
    void testCrearPlan() {

        Plan plan = new Plan();
        when(planRepository.save(plan)).thenReturn(plan);

        Plan resultado = planService.crearPlan(plan);

        assertEquals(plan, resultado);
        verify(planRepository).save(plan);
    }

    @Test
    @DisplayName("Debe actualizar un plan existente correctamente")
    void testActualizarPlan() {

        Plan planExistente = new Plan();
        planExistente.setNombre("Básico");
        Plan planActualizado = new Plan();
        planActualizado.setNombre("Avanzado");
        planActualizado.setMaxProductos(10);
        planActualizado.setMaxActivos(5);
        planActualizado.setMaxAlmacenamiento(100);
        planActualizado.setMaxCategoriasProductos(3);
        planActualizado.setMaxCategoriasActivos(2);
        planActualizado.setMaxRelaciones(1);
        planActualizado.setPrecio(9.99);

        when(planRepository.findById(1L)).thenReturn(Optional.of(planExistente));
        when(planRepository.save(any())).thenReturn(planExistente);

        Plan resultado = planService.actualizarPlan(1L, planActualizado);

        assertEquals("Avanzado", resultado.getNombre());
        assertEquals(10, resultado.getMaxProductos());
        assertEquals(5, resultado.getMaxActivos());
        assertEquals(100, resultado.getMaxAlmacenamiento());
        assertEquals(9.99, resultado.getPrecio());
        verify(planRepository).save(planExistente);
    }

    @Test
    @DisplayName("Debe lanzar excepción al actualizar un plan inexistente")
    void testActualizarPlanNoExiste() {

        when(planRepository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                planService.actualizarPlan(99L, new Plan()));
        assertTrue(ex.getMessage().contains("Plan no encontrado"));
    }

    @Test
    @DisplayName("Debe eliminar un plan correctamente")
    void testEliminarPlan() {
       
        Long id = 5L;

       
        planService.eliminarPlan(id);

        
        verify(planRepository).deleteById(id);
    }

    @Test
    @DisplayName("Debe obtener un plan por ID correctamente")
    void testObtenerPlanPorId() {
       
        Plan plan = new Plan();
        when(planRepository.findById(3L)).thenReturn(Optional.of(plan));
  
        Optional<Plan> resultado = planService.obtenerPlanPorId(3L);

        assertTrue(resultado.isPresent());
        assertEquals(plan, resultado.get());
    }
}
