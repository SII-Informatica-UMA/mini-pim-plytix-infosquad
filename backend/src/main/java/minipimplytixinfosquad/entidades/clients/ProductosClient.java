package minipimplytixinfosquad.entidades.clients;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;

@Component
@RequiredArgsConstructor
public class ProductosClient {

    @Value("${productos.base-url}")
    private String baseUrl;

    private final RestTemplate restTemplate = new RestTemplate();
    private final DefaultUriBuilderFactory ubf = new DefaultUriBuilderFactory();

    /* 4 end-points casi idénticos → factor común */
    public boolean cuentaTieneProductos (Long id, String jwt){ return hayRecursos("/producto",           id, jwt); }
    public boolean cuentaTieneCategorias(Long id, String jwt){ return hayRecursos("/categoria-producto", id, jwt); }
    public boolean cuentaTieneRelaciones(Long id, String jwt){ return hayRecursos("/relacion",           id, jwt); }
    public boolean cuentaTieneActivos   (Long id, String jwt){ return hayRecursos("/activo",             id, jwt); }

    /* --------------------------------------------------------------------- */
    private boolean hayRecursos(String path, Long idCuenta, String jwt) {

        URI uri = ubf.builder()
                     .scheme("https")
                     .host(baseUrl.replace("https://",""))
                     .path(path)
                     .queryParam("idCuenta", idCuenta)
                     .build();

        RequestEntity<Void> rq = RequestEntity.get(uri)
                .header("Authorization", "Bearer "+jwt)
                .accept(MediaType.APPLICATION_JSON)
                .build();

        ResponseEntity<JsonNode> rs =
                restTemplate.exchange(rq, JsonNode.class);

        return rs.getStatusCode()==HttpStatus.OK &&
               rs.getBody()!=null &&
               rs.getBody().isArray() &&
               rs.getBody().size()>0;
    }
}
