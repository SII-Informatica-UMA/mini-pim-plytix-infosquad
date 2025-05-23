package minipimplytixinfosquad.entidades.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NuevoPropietarioDTO {
    private Long id;
    private String email;
}
