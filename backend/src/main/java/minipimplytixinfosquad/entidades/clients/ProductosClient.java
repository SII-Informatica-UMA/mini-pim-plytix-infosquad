package minipimplytixinfosquad.entidades.clients;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class ProductosClient {

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper mapper = new ObjectMapper();

    @Value("${productos.base-url}")          
    private String productosBaseUrl;        

    public boolean cuentaTieneProductos(Long idCuenta, String jwt) {
        return hayRecursos(productosBaseUrl + "/producto?idCuenta=" + idCuenta, jwt);
    }

    public boolean cuentaTieneCategorias(Long idCuenta, String jwt) {
        return hayRecursos(productosBaseUrl + "/categoria-producto?idCuenta=" + idCuenta, jwt);
    }

    public boolean cuentaTieneRelaciones(Long idCuenta, String jwt) {
        return hayRecursos(productosBaseUrl + "/relacion?idCuenta=" + idCuenta, jwt);
    }

    public boolean cuentaTieneActivos(Long idCuenta, String jwt) {
        return hayRecursos(productosBaseUrl + "/activo?idCuenta=" + idCuenta, jwt); // si existe este endpoint
    }

    /* ---------- método común ---------- */
    private boolean hayRecursos(String url, String jwt) {
        HttpHeaders h = new HttpHeaders();
        h.set("Authorization", "Bearer " + jwt);
        h.setAccept(List.of(MediaType.APPLICATION_JSON));
        ResponseEntity<String> res = restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(h), String.class);

        try {
            JsonNode root = mapper.readTree(res.getBody());
            return root.isArray() && root.size() > 0;
        } catch (Exception e) {
            return false;
        }
    }
}
