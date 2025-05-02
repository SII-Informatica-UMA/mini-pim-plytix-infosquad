package minipimplytixinfosquad.entidades.dtos;

import lombok.Data;

@Data
public class UsuarioBasicoDTO {
    private Long id;
    private String email;
    private String nombre;
    private String apellido1;
    private String apellido2;
}
