package minipimplytixinfosquad.entidades.clients;

import minipimplytixinfosquad.entidades.dtos.UsuarioResumenDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriUtils;

import java.nio.charset.StandardCharsets;
import java.util.List;

@Component
public class UsuarioClient {

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${usuarios.base-url}")
    private String usuariosBaseUrl;

    public UsuarioResumenDTO obtenerUsuarioPorId(Long id, String jwt) {
        String url = usuariosBaseUrl + "/usuario?id=" + id;

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + jwt);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));

        HttpEntity<?> entity = new HttpEntity<>(headers);

        ResponseEntity<UsuarioResumenDTO[]> response = restTemplate.exchange(
            url, HttpMethod.GET, entity, UsuarioResumenDTO[].class);

        if (response.getStatusCode() == HttpStatus.OK &&
            response.getBody() != null &&
            response.getBody().length > 0) {
            return response.getBody()[0];
        } else {
            throw new RuntimeException("No se pudo obtener información del usuario con ID " + id);
        }
    }

    public UsuarioResumenDTO obtenerUsuarioPorEmail(String email, String jwt) {
        String encodedEmail = UriUtils.encode(email, StandardCharsets.UTF_8);
        String url = usuariosBaseUrl + "/usuario?email=" + encodedEmail;

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + jwt);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));

        HttpEntity<?> entity = new HttpEntity<>(headers);

        ResponseEntity<UsuarioResumenDTO[]> response = restTemplate.exchange(
            url, HttpMethod.GET, entity, UsuarioResumenDTO[].class);

        if (response.getStatusCode() == HttpStatus.OK &&
            response.getBody() != null &&
            response.getBody().length > 0) {
            return response.getBody()[0];
        } else {
            throw new RuntimeException("No se pudo obtener información del usuario con email: " + email);
        }
    }

    public List<UsuarioResumenDTO> obtenerUsuariosPorIds(List<Long> ids, String jwt) {
        return ids.stream()
            .map(id -> {
                try {
                    return obtenerUsuarioPorId(id, jwt);
                } catch (Exception e) {
                    System.err.println("No se pudo obtener el usuario con ID: " + id);
                    return null;
                }
            })
            .filter(u -> u != null)
            .toList();
    }
}
