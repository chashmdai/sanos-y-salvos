const BASE_URL = 'http://localhost:8080'; // Tu BFF Gateway

// CORRECCIÓN: Le decimos explícitamente a TypeScript que esto es un diccionario de strings
const getAuthHeaders = (): Record<string, string> => {
    const token = localStorage.getItem('jwt_token');
    return token ? { 'Authorization': `Bearer ${token}` } : {};
};

export const AuthService = {
    login: async (email: string, password: string) => {
        const response = await fetch(`${BASE_URL}/auth/login`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ email, password })
        });
        
        if (!response.ok) throw new Error('Credenciales inválidas');
        return response.json();
    }
};

export const MatchService = {
    analizarMatch: async (idReportePerdido: number) => {
        const response = await fetch(`${BASE_URL}/match/analizar/${idReportePerdido}`, {
            method: 'GET',
            headers: { 
                'Content-Type': 'application/json',
                ...getAuthHeaders() 
            }
        });

        if (!response.ok) throw new Error('Error al procesar el Match con IA');
        // Si no hay coincidencias (204 No Content), manejamos el caso
        if (response.status === 204) return { coincidencias: [] }; 
        return response.json();
    }
};