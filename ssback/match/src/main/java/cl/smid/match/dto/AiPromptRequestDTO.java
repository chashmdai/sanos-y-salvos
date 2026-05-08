package cl.smid.match.dto;

import java.util.List;

public record AiPromptRequestDTO(
    MascotaDTO mascotaPerdida,
    List<MascotaDTO> mascotasEncontradasCandidatas
) {}