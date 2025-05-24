package minipimplytixinfosquad.entidades.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioResumenDTO {
    private Long id;
    private String email;
    private String nombre;
    private String apellido1;
    private String apellido2;
    private String role;
}
