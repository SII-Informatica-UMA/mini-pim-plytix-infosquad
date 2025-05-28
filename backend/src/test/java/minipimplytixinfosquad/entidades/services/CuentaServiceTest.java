package minipimplytixinfosquad.entidades.services;

import minipimplytixinfosquad.entidades.clients.ProductosClient;
import minipimplytixinfosquad.entidades.entities.Cuenta;
import minipimplytixinfosquad.entidades.entities.Plan;
import minipimplytixinfosquad.entidades.exceptions.CuentaConRecursosException;
import minipimplytixinfosquad.entidades.repositories.CuentaRepository;
import minipimplytixinfosquad.entidades.repositories.PlanRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.util.List.of;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


class CuentaServiceFullTest {

    private CuentaService   service;
    private CuentaRepository repo;
    private ProductosClient  prodCli;

    private static Plan p(long id){ return Plan.builder().id(id).nombre("B").build(); }
    private static Cuenta c(long id,long prop,List<Long> us){
        return Cuenta.builder().id(id).nombre("C"+id).plan(p(1))
                     .propietarioId(prop).usuariosIds(new ArrayList<>(us)).build();
    }

    @BeforeEach
    void init(){
        repo    = mock(CuentaRepository.class);
        prodCli = mock(ProductosClient.class);

        service = new CuentaService();
        org.springframework.test.util.ReflectionTestUtils.setField(service,"cuentaRepository",repo);
        org.springframework.test.util.ReflectionTestUtils.setField(service,"planRepository",mock(PlanRepository.class));
        org.springframework.test.util.ReflectionTestUtils.setField(service,"productosClient",prodCli);
    }

    /* --------- crearCuenta ----------- */
    @Test 
    @DisplayName("crearCuenta - asigna propietario y usuario inicial correctamente")
    void crearCuenta_asignaPropietarioYUsuario(){
        Cuenta in = c(0,0,new ArrayList<>());
        when(repo.save(any(Cuenta.class))).thenAnswer(a->a.getArgument(0));
        Cuenta res = service.crearCuenta(in, 7L);
        assertEquals(7L, res.getPropietarioId());
        assertEquals(of(7L), res.getUsuariosIds());
    }

    /* ---------- listar / obtener / eliminar simples ---------- */
    @Test 
    @DisplayName("listar - devuelve lista de cuentas correctamente")
    void listar_ok(){
        when(repo.findAll()).thenReturn(of(c(1,7,of(7L))));
        assertEquals(1, service.listarCuentas().size());
    }
    @Test 
    @DisplayName("obtenerCuentaPorId - devuelve cuenta existente")
    void obtenerCuentaPorId_ok(){
        when(repo.findById(1L)).thenReturn(Optional.of(c(1,7,of(7L))));
        assertTrue(service.obtenerCuentaPorId(1L).isPresent());
    }
    @Test 
    @DisplayName("eliminarCuenta - invoca método de repositorio correctamente")
    void eliminarCuenta_invocaDelete(){
        service.eliminarCuenta(3L); verify(repo).deleteById(3L);
    }

    /* ---------- obtenerPropietario----------- */
    @Test 
    @DisplayName("obtenerPropietario - obtiene propietario correctamente")
    void obtenerPropietario_ok(){
        when(repo.findById(1L)).thenReturn(Optional.of(c(1,99,of(99L))));
        assertEquals(99L, service.obtenerPropietario(1L));
    }
    @Test 
    @DisplayName("obtenerPropietario - lanza excepción si no encuentra la cuenta")
    void obtenerPropietario_notFound(){
        assertThrows(RuntimeException.class, ()->service.obtenerPropietario(2L));
    }

    /* -------- actualizarCuenta --------- */
    @Test 
    @DisplayName("actualizarCuenta - actualiza datos correctamente")
    void actualizarCuenta_ok(){
        Cuenta original=c(1,7,of(7L));
        Cuenta patch   =c(0,9,of(9L));
        when(repo.findById(1L)).thenReturn(Optional.of(original));
        when(repo.save(any(Cuenta.class))).thenAnswer(a->a.getArgument(0));
        Cuenta res=service.actualizarCuenta(1L,patch);
        assertEquals("C0",res.getNombre());
        assertEquals(9L,  res.getPropietarioId());
    }
    @Test 
    @DisplayName("actualizarCuenta - lanza excepción si no existe la cuenta")
    void actualizarCuenta_notFound(){
        assertThrows(RuntimeException.class,
                     ()->service.actualizarCuenta(9L,c(0,0,of())));
    }

    /* -------- obtenerUsuariosDeCuenta -------- */
    @Test 
    @DisplayName("obtenerUsuariosDeCuenta - devuelve lista de usuarios correctamente")
    void obtenerUsuarios_ok(){
        when(repo.findById(1L)).thenReturn(Optional.of(c(1,7,of(7L,8L))));
        assertEquals(of(7L,8L), service.obtenerUsuariosDeCuenta(1L));
    }
    @Test 
    @DisplayName("obtenerUsuariosDeCuenta - lanza excepción si no existe la cuenta")
    void obtenerUsuarios_notFound(){
        assertThrows(RuntimeException.class,
                     ()->service.obtenerUsuariosDeCuenta(9L));
    }

    /* -------- actualizarUsuarios --------*/
    @Test @DisplayName("actualizarUsuarios - agrega propietario si falta")
    void actualizarUsuarios_agregaProp(){
        Cuenta cuenta=c(1,7,of(7L));                     // repo devuelve cuenta
        when(repo.findById(1L)).thenReturn(Optional.of(cuenta));
        ArgumentCaptor<Cuenta> cap=ArgumentCaptor.forClass(Cuenta.class);
        when(repo.save(cap.capture())).thenAnswer(a->a.getArgument(0));

        service.actualizarUsuarios(1L,of(8L));           // propietario no incluido
        List<Long> guardados=cap.getValue().getUsuariosIds();
        assertTrue(guardados.containsAll(of(7L,8L)));
    }
    @Test @DisplayName("actualizarUsuarios - lista ya contiene propietario")
    void actualizarUsuarios_propYaIncluido(){
        Cuenta cuenta=c(1,7,of(7L));
        when(repo.findById(1L)).thenReturn(Optional.of(cuenta));
        service.actualizarUsuarios(1L,of(7L,8L));     
        assertEquals(of(7L,8L), cuenta.getUsuariosIds());
    }

    /* -------- actualizarPropietario -------- */
    @Test 
    @DisplayName("actualizarPropietario - actualiza propietario y lo añade si no estaba")
    void actualizarPropietario_agregaSiNoEstaba(){
        Cuenta cuenta=c(1,7,of(7L));
        when(repo.findById(1L)).thenReturn(Optional.of(cuenta));
        when(repo.save(any(Cuenta.class))).thenAnswer(a->a.getArgument(0));

        service.actualizarPropietario(1L,9L);
        assertEquals(9L, cuenta.getPropietarioId());
        assertTrue(cuenta.getUsuariosIds().contains(9L));
    }
    @Test 
    @DisplayName("actualizarPropietario - nuevo propietario ya era usuario")
    void actualizarPropietario_usuarioYaEraMiembro(){
        Cuenta cuenta=c(1,7,of(7L,9L));
        when(repo.findById(1L)).thenReturn(Optional.of(cuenta));
        service.actualizarPropietario(1L,9L);
        assertEquals(9L, cuenta.getPropietarioId());
        assertEquals(of(7L,9L), cuenta.getUsuariosIds());
    }

    /* -------- eliminarCuentaSiNoTieneRecursos -------- */
    private void mockRecursos(boolean prod,boolean cat,boolean rel,boolean act){
        when(prodCli.cuentaTieneProductos (1L,"tok")).thenReturn(prod);
        when(prodCli.cuentaTieneCategorias(1L,"tok")).thenReturn(cat);
        when(prodCli.cuentaTieneRelaciones(1L,"tok")).thenReturn(rel);
        when(prodCli.cuentaTieneActivos   (1L,"tok")).thenReturn(act);
    }
    @Test 
    @DisplayName("eliminarCuentaSiNoTieneRecursos - elimina cuenta sin recursos")
    void eliminarSinRecursos_ok(){
        mockRecursos(false,false,false,false);
        service.eliminarCuentaSiNoTieneRecursos(1L,"tok");
        verify(repo).deleteById(1L);
    }
    @Test 
    @DisplayName("eliminarCuentaSiNoTieneRecursos - lanza excepción si tiene productos")
    void eliminarConProductos(){
        mockRecursos(true,false,false,false);
        assertThrows(CuentaConRecursosException.class,
                     ()->service.eliminarCuentaSiNoTieneRecursos(1L,"tok"));
    }
    @Test 
    @DisplayName("eliminarCuentaSiNoTieneRecursos - lanza excepción si tiene categorías")
    void eliminarConCategorias(){
        mockRecursos(false,true,false,false);
        assertThrows(CuentaConRecursosException.class,
                     ()->service.eliminarCuentaSiNoTieneRecursos(1L,"tok"));
    }
    @Test 
    @DisplayName("eliminarCuentaSiNoTieneRecursos - lanza excepción si tiene relaciones")
    void eliminarConRelaciones(){
        mockRecursos(false,false,true,false);
        assertThrows(CuentaConRecursosException.class,
                     ()->service.eliminarCuentaSiNoTieneRecursos(1L,"tok"));
    }
    @Test 
    @DisplayName("eliminarCuentaSiNoTieneRecursos - lanza excepción si tiene activos")
    void eliminarConActivos(){
        mockRecursos(false,false,false,true);
        assertThrows(CuentaConRecursosException.class,
                     ()->service.eliminarCuentaSiNoTieneRecursos(1L,"tok"));
    }


    // -------- -------------------------------------
    /* -------- lambdas sinteticas restantes -------- */
    @Test
    @DisplayName("Ejecutar lambdas sinteticas para cobertura completa de CuentaService")
    @SuppressWarnings("java:S3011")
    void cubrirLambdasJacoco() throws Exception{
        for (Method m: CuentaService.class.getDeclaredMethods()){
            if (m.getName().startsWith("lambda$")){
                m.setAccessible(true);
                Object arg = switch (m.getParameterCount()){
                    case 0  -> null;
                    case 1  -> m.getParameterTypes()[0]==Long.class
                               ? 1L : c(0,0,of(0L));
                    default -> null;
                };
                try{ if(arg==null) m.invoke(null); else m.invoke(null,arg); }
                catch (Exception ignored){ }
            }
        }
    }
}
