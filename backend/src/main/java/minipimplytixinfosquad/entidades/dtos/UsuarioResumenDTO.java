package minipimplytixinfosquad.entidades.dtos;

import lombok.Data;

@Data
public class UsuarioResumenDTO {
    private Long id;
    private String email;
    private String nombre;
    private String apellido1;
    private String apellido2;
    private String role;
}
