package cl.smid.match.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MascotaDTO {
    private Long idReporte;
    private String especie;
    private String raza;
    private String color;
    private String senalesParticulares;
    private String estado; // "PERDIDA" o "ENCONTRADA"
}