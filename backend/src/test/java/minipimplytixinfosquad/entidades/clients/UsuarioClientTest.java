package minipimplytixinfosquad.entidades.clients;

import minipimplytixinfosquad.entidades.dtos.UsuarioResumenDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.client.ExpectedCount.once;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

class UsuarioClientTest {

    private UsuarioClient client;
    private MockRestServiceServer server;

    @BeforeEach
    void init() {
        client = new UsuarioClient();
        ReflectionTestUtils.setField(client, "baseUrl", "https://mallba3.lcc.uma.es");

        RestTemplate rt = (RestTemplate) ReflectionTestUtils.getField(client, "restTemplate");
        server = MockRestServiceServer.bindTo(rt).build();
    }

    /* -------- obtenerUsuarioPorId -------- */
    @Test
    @DisplayName("obtenerUsuarioPorId - devuelve DTO correctamente")
    void obtenerUsuarioPorId_ok() {
        server.expect(once(),
                requestTo("https://mallba3.lcc.uma.es/usuario?id=8"))
              .andExpect(method(HttpMethod.GET))
              .andRespond(withSuccess("""
                  [{"id":8,"email":"foo@bar.com","nombre":"F","apellido1":"A1","apellido2":"A2"}]
              """, MediaType.APPLICATION_JSON));

        UsuarioResumenDTO dto = client.obtenerUsuarioPorId(8L, "tok");
        assertEquals(8L, dto.getId());
        assertEquals("foo@bar.com", dto.getEmail());
    }

    @Test
    @DisplayName("obtenerUsuarioPorId - lanza excepción si no hay usuario")
    void obtenerUsuarioPorId_notFound_lanzaExcepcion() {
        server.expect(once(),
                requestTo("https://mallba3.lcc.uma.es/usuario?id=9"))
              .andRespond(withSuccess("[]", MediaType.APPLICATION_JSON));

        assertThrows(RuntimeException.class,
                     () -> client.obtenerUsuarioPorId(9L, "tok"));
    }

    /* -------- obtenerUsuarioPorEmail -------- */
    @Test
    @DisplayName("obtenerUsuarioPorEmail - devuelve DTO correctamente")
    void obtenerUsuarioPorEmail_ok() {
        server.expect(once(),
                request -> {
                    assertEquals("/usuario", request.getURI().getPath());
                    assertEquals("mallba3.lcc.uma.es", request.getURI().getHost());
                    
                    assertTrue(request.getURI().getQuery().contains("email=foo@bar.com"));
                })
              .andExpect(method(HttpMethod.GET))
              .andRespond(withSuccess("""
                  [{"id":8,"email":"foo@bar.com","nombre":"F","apellido1":"A1","apellido2":"A2"}]
              """, MediaType.APPLICATION_JSON));

        UsuarioResumenDTO dto = client.obtenerUsuarioPorEmail("foo@bar.com", "tok");
        assertEquals(8L, dto.getId());
        assertEquals("foo@bar.com", dto.getEmail());
    }

    @Test
    @DisplayName("obtenerUsuarioPorEmail - lanza excepción si no encuentra usuario")
    void obtenerUsuarioPorEmail_notFound_lanzaExcepcion() {
        server.expect(once(),
                request -> assertTrue(request.getURI().getQuery().contains("email=foo@bar.com")))
              .andRespond(withSuccess("[]", MediaType.APPLICATION_JSON));

        assertThrows(RuntimeException.class,
                     () -> client.obtenerUsuarioPorEmail("foo@bar.com", "tok"));
    }

    /* -------- obtenerUsuariosPorIds -------- */
    @Test
    @DisplayName("obtenerUsuariosPorIds - devuelve lista de usuarios correctamente")
    void obtenerUsuariosPorIds_ok() {
        server.expect(once(),
                requestTo("https://mallba3.lcc.uma.es/usuario?id=7"))
              .andExpect(method(HttpMethod.GET))
              .andRespond(withSuccess("""
                  [{"id":7,"email":"a@x.com","nombre":"A","apellido1":"A1","apellido2":"A2"}]
              """, MediaType.APPLICATION_JSON));

        server.expect(once(),
                requestTo("https://mallba3.lcc.uma.es/usuario?id=8"))
              .andExpect(method(HttpMethod.GET))
              .andRespond(withSuccess("""
                  [{"id":8,"email":"b@x.com","nombre":"B","apellido1":"B1","apellido2":"B2"}]
              """, MediaType.APPLICATION_JSON));

        List<UsuarioResumenDTO> lista =
                client.obtenerUsuariosPorIds(List.of(7L, 8L), "tok");

        assertEquals(2, lista.size());
        assertEquals("a@x.com", lista.get(0).getEmail());
        assertEquals("b@x.com", lista.get(1).getEmail());
    }
}
