export interface JwtResponse {
    token: string;
    type: string;
    email: string;
    fullName: string;
    roles: string[];
}

export interface CoincidenciaDTO {
    idReporteEncontrado: number;
    porcentajeSimilitud: number;
    razonamiento: string;
}

export interface AiMatchResponseDTO {
    coincidencias: CoincidenciaDTO[];
}