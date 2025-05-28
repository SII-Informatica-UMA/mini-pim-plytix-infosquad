package minipimplytixinfosquad.entidades.clients;

import com.fasterxml.jackson.databind.JsonNode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.client.ExpectedCount.once;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

class ProductosClientTest {

    private ProductosClient client;
    private MockRestServiceServer server;

    @BeforeEach
    void init() {
        client = new ProductosClient();
        ReflectionTestUtils.setField(client, "baseUrl",
                                     "https://mallba3.lcc.uma.es");

        RestTemplate rt = (RestTemplate) ReflectionTestUtils
                          .getField(client, "restTemplate");

        server = MockRestServiceServer.bindTo(rt).build();
    }

    @Test
    @DisplayName("cuentaTieneProductos - devuelve true cuando la respuesta contiene elementos")
    void cuentaTieneProductos_trueCuandoHayArrayNoVacio() {
        server.expect(once(),
                requestTo("https://mallba3.lcc.uma.es/producto?idCuenta=1"))
              .andExpect(method(HttpMethod.GET))
              .andRespond(withSuccess("[{}]", MediaType.APPLICATION_JSON));

        assertTrue(client.cuentaTieneProductos(1L, "tok"));
    }

    @Test
    @DisplayName("cuentaTieneProductos - devuelve false cuando la respuesta está vacía")
    void cuentaTieneProductos_falseCuandoArrayVacio() {
        server.expect(once(),
                requestTo("https://mallba3.lcc.uma.es/producto?idCuenta=2"))
              .andRespond(withSuccess("[]", MediaType.APPLICATION_JSON));

        assertFalse(client.cuentaTieneProductos(2L, "tok"));
    }

    @Test
    @DisplayName("cuentaTieneCategorias - devuelve true cuando la respuesta contiene elementos")
    void cuentaTieneCategorias_trueCuandoHayArrayNoVacio() {
        server.expect(once(),
                requestTo("https://mallba3.lcc.uma.es/categoria-producto?idCuenta=3"))
              .andExpect(method(HttpMethod.GET))
              .andRespond(withSuccess("[{}]", MediaType.APPLICATION_JSON));

        assertTrue(client.cuentaTieneCategorias(3L, "tok"));
    }

    @Test
    @DisplayName("cuentaTieneCategorias - devuelve false cuando la respuesta está vacía")
    void cuentaTieneCategorias_falseCuandoArrayVacio() {
        server.expect(once(),
                requestTo("https://mallba3.lcc.uma.es/categoria-producto?idCuenta=3"))
              .andExpect(method(HttpMethod.GET))
              .andRespond(withSuccess("[]", MediaType.APPLICATION_JSON));

        assertFalse(client.cuentaTieneCategorias(3L, "tok"));
    }

    @Test
    @DisplayName("cuentaTieneRelaciones - devuelve true cuando la respuesta contiene elementos")
    void cuentaTieneRelaciones_trueCuandoHayArrayNoVacio() {
        server.expect(once(),
                requestTo("https://mallba3.lcc.uma.es/relacion?idCuenta=4"))
              .andExpect(method(HttpMethod.GET))
              .andRespond(withSuccess("[{}]", MediaType.APPLICATION_JSON));

        assertTrue(client.cuentaTieneRelaciones(4L, "tok"));
    }

    @Test
    @DisplayName("cuentaTieneRelaciones - devuelve false cuando la respuesta está vacía")
    void cuentaTieneRelaciones_falseCuandoArrayVacio() {
        server.expect(once(),
                requestTo("https://mallba3.lcc.uma.es/relacion?idCuenta=4"))
              .andExpect(method(HttpMethod.GET))
              .andRespond(withSuccess("[]", MediaType.APPLICATION_JSON));

        assertFalse(client.cuentaTieneRelaciones(4L, "tok"));
    }

    @Test
    @DisplayName("cuentaTieneActivos - devuelve true cuando la respuesta contiene elementos")
    void cuentaTieneActivos_trueCuandoHayArrayNoVacio() {
        server.expect(once(),
                requestTo("https://mallba3.lcc.uma.es/activo?idCuenta=5"))
              .andExpect(method(HttpMethod.GET))
              .andRespond(withSuccess("[{}]", MediaType.APPLICATION_JSON));

        assertTrue(client.cuentaTieneActivos(5L, "tok"));
    }

    @Test
    @DisplayName("cuentaTieneActivos - devuelve false cuando la respuesta está vacía")
    void cuentaTieneActivos_falseCuandoArrayVacio() {
        server.expect(once(),
                requestTo("https://mallba3.lcc.uma.es/activo?idCuenta=5"))
              .andExpect(method(HttpMethod.GET))
              .andRespond(withSuccess("[]", MediaType.APPLICATION_JSON));

        assertFalse(client.cuentaTieneActivos(5L, "tok"));
    }
}

