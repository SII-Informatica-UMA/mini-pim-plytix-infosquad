package minipimplytixinfosquad.entidades.clients;

import minipimplytixinfosquad.entidades.dtos.UsuarioResumenDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;
import org.springframework.web.util.UriUtils;

import lombok.RequiredArgsConstructor;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Component
@RequiredArgsConstructor
public class UsuarioClient {

    @Value("${usuarios.base-url}")
    private String baseUrl;

    private final RestTemplate restTemplate = new RestTemplate();
    private final DefaultUriBuilderFactory ubf = new DefaultUriBuilderFactory();  // factoría reaprovechable

    /* --- GET /usuario?id=... --------------------------------------------- */
    public UsuarioResumenDTO obtenerUsuarioPorId(Long id, String jwt) {

        URI uri = ubf.builder()
                     .scheme("https")
                     .host(baseUrl.replace("https://", ""))   // «mallba3.lcc.uma.es»
                     .path("/usuario")
                     .queryParam("id", id)
                     .build();

        RequestEntity<Void> rq = RequestEntity.get(uri)
                .header("Authorization", "Bearer "+jwt)
                .accept(MediaType.APPLICATION_JSON)
                .build();

        ResponseEntity<UsuarioResumenDTO[]> rs =
                restTemplate.exchange(rq, UsuarioResumenDTO[].class);

        if (rs.getStatusCode() == HttpStatus.OK &&
            rs.getBody()           != null     &&
            rs.getBody().length    >  0)
            return rs.getBody()[0];

        throw new RuntimeException("Usuario "+id+" no encontrado");
    }

    /* --- GET /usuario?email=... ------------------------------------------ */
    public UsuarioResumenDTO obtenerUsuarioPorEmail(String email, String jwt) {

         
        URI uri = ubf.builder()
                     .scheme("https")
                     .host(baseUrl.replace("https://", ""))
                     .path("/usuario")
                     .queryParam("email", email)
                     .build();
        

        /* 
        String encodedEmail = UriUtils.encode(email, StandardCharsets.UTF_8);
        URI uri = ubf.builder()
                    .scheme("https")
                    .host(baseUrl.replace("https://", ""))
                    .path("/usuario")
                    .queryParam("email", encodedEmail) // codificado manualmente
                    .build();
        */

        RequestEntity<Void> rq = RequestEntity.get(uri)
                .header("Authorization", "Bearer "+jwt)
                .accept(MediaType.APPLICATION_JSON)
                .build();

        ResponseEntity<UsuarioResumenDTO[]> rs =
                restTemplate.exchange(rq, UsuarioResumenDTO[].class);

        if (rs.getStatusCode() == HttpStatus.OK &&
            rs.getBody() != null && rs.getBody().length > 0)
            return rs.getBody()[0];

        throw new RuntimeException("Usuario con email "+email+" no encontrado");
    }

    /* --- Lista de IDs usando stream interno -------------------------------- */
    public List<UsuarioResumenDTO> obtenerUsuariosPorIds(List<Long> ids, String jwt) {
        return ids.stream()
                  .map(id -> obtenerUsuarioPorId(id, jwt))
                  .toList();
    }
}
