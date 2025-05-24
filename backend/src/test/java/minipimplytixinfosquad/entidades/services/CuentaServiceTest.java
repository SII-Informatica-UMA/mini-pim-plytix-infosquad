package minipimplytixinfosquad.entidades.services;

import minipimplytixinfosquad.entidades.clients.ProductosClient;
import minipimplytixinfosquad.entidades.entities.Cuenta;
import minipimplytixinfosquad.entidades.entities.Plan;
import minipimplytixinfosquad.entidades.exceptions.CuentaConRecursosException;
import minipimplytixinfosquad.entidades.repositories.CuentaRepository;
import minipimplytixinfosquad.entidades.repositories.PlanRepository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CuentaServiceTest {

    @Mock
    private CuentaRepository cuentaRepository;

    @Mock
    private PlanRepository planRepository;

    @Mock
    private ProductosClient productosClient;

    @InjectMocks
    private CuentaService cuentaService;

    @Test
    @DisplayName("Debe listar todas las cuentas")
    void testListarCuentas() {
        
        List<Cuenta> mockCuentas = List.of(new Cuenta(), new Cuenta());
        when(cuentaRepository.findAll()).thenReturn(mockCuentas);

        List<Cuenta> resultado = cuentaService.listarCuentas();

        assertEquals(2, resultado.size());
    }

    @Test
    @DisplayName("Debe crear una nueva cuenta con propietario asignado")
    void testCrearCuenta() {
        
        Cuenta nueva = new Cuenta();
        when(cuentaRepository.save(any())).thenReturn(nueva);
        
        Cuenta cuentaGuardada = cuentaService.crearCuenta(new Cuenta(), 1L);

        assertNotNull(cuentaGuardada);
        verify(cuentaRepository).save(any());
    }

    @Test
    @DisplayName("Debe lanzar excepción si no se encuentra la cuenta al actualizar")
    void testActualizarCuentaNoExiste() {
        
        when(cuentaRepository.findById(anyLong())).thenReturn(Optional.empty());

      
        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                cuentaService.actualizarCuenta(1L, new Cuenta()));
        assertTrue(exception.getMessage().contains("Cuenta no encontrada"));
    }

    @Test
    @DisplayName("Debe eliminar cuenta sin recursos asociados")
    void testEliminarCuentaSinRecursos() {
       
        Long cuentaId = 10L;
        String jwt = "token";
        when(productosClient.cuentaTieneProductos(cuentaId, jwt)).thenReturn(false);
        when(productosClient.cuentaTieneCategorias(cuentaId, jwt)).thenReturn(false);
        when(productosClient.cuentaTieneRelaciones(cuentaId, jwt)).thenReturn(false);
        when(productosClient.cuentaTieneActivos(cuentaId, jwt)).thenReturn(false);

        
        cuentaService.eliminarCuentaSiNoTieneRecursos(cuentaId, jwt);

        
        verify(cuentaRepository).deleteById(cuentaId);
    }

    @Test
    @DisplayName("Debe lanzar excepción si la cuenta tiene recursos")
    void testEliminarCuentaConRecursos() {
        
        Long cuentaId = 10L;
        String jwt = "token";
        when(productosClient.cuentaTieneProductos(cuentaId, jwt)).thenReturn(true);

      
        assertThrows(CuentaConRecursosException.class, () ->
                cuentaService.eliminarCuentaSiNoTieneRecursos(cuentaId, jwt));
    }

    @Test
    @DisplayName("Debe actualizar correctamente el propietario de la cuenta")
    void testActualizarPropietario() {
        
        Cuenta cuenta = new Cuenta();
        cuenta.setUsuariosIds(new ArrayList<>());
        when(cuentaRepository.findById(1L)).thenReturn(Optional.of(cuenta));

        
        cuentaService.actualizarPropietario(1L, 77L);

        
        verify(cuentaRepository).save(cuenta);
        assertEquals(77L, cuenta.getPropietarioId());
        assertTrue(cuenta.getUsuariosIds().contains(77L));
    }

    @Test
    @DisplayName("Debe obtener correctamente el propietario de una cuenta existente")
    void testObtenerPropietario() {
        
        Cuenta cuenta = new Cuenta();
        cuenta.setPropietarioId(99L);
        when(cuentaRepository.findById(1L)).thenReturn(Optional.of(cuenta));

       
        Long propietario = cuentaService.obtenerPropietario(1L);

        
        assertEquals(99L, propietario);
    }

}
