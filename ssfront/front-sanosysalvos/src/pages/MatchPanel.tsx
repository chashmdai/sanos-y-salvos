import { useState } from 'react';
import { Navbar } from '../components/layout/Navbar';
import { Card } from '../components/ui/Card';
import { Button } from '../components/ui/Button';
import { MatchService } from '../services/api';
import type { AiMatchResponseDTO } from '../types';

export const MatchPanel = () => {
    const [idReporte, setIdReporte] = useState('1');
    const [resultado, setResultado] = useState<AiMatchResponseDTO | null>(null);
    const [loading, setLoading] = useState(false);

    const ejecutarIA = async () => {
        setLoading(true);
        setResultado(null);

        try {
            const data = await MatchService.analizarMatch(Number(idReporte));

            if (!data) {
                setResultado({ coincidencias: [] });
            } else {
                setResultado(data);
            }
        } catch (error) {
            alert('Error al conectar con el motor de IA o servicio inactivo.');
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="min-h-screen bg-gray-50">
            <Navbar />

            <div className="container mx-auto p-8 flex flex-col items-center">
                <Card className="w-full max-w-2xl mb-8 border-l-4 border-l-sanos-blue">
                    <h2 className="text-xl font-bold mb-4">Orquestador IA: GPT-5.4</h2>

                    <p className="text-gray-600 mb-4">
                        Ingresa el ID del reporte de la mascota perdida. El sistema cruzará la información con la base de datos de hallazgos mediante NLP.
                    </p>

                    <div className="flex gap-4">
                        <input
                            type="number"
                            value={idReporte}
                            onChange={e => setIdReporte(e.target.value)}
                            className="p-2 border border-gray-300 rounded w-32"
                            placeholder="ID Reporte"
                        />

                        <Button onClick={ejecutarIA} disabled={loading}>
                            {loading ? 'Procesando Match...' : '🚀 Analizar Coincidencias'}
                        </Button>
                    </div>
                </Card>

                {resultado && resultado.coincidencias && (
                    <div className="w-full max-w-2xl flex flex-col gap-4">
                        <h3 className="text-lg font-bold text-gray-700">
                            Resultados del Emparejamiento:
                        </h3>

                        {resultado.coincidencias.length === 0 ? (
                            <Card className="bg-yellow-50">
                                <p className="text-yellow-700 font-medium">
                                    No se encontraron coincidencias viables.
                                </p>
                            </Card>
                        ) : (
                            resultado.coincidencias.map((match, index) => (
                                <Card
                                    key={index}
                                    className="border-l-4 border-l-sanos-orange"
                                >
                                    <div className="flex justify-between items-start mb-2">
                                        <span className="font-bold text-lg">
                                            Candidato Encontrado ID: #{match.idReporteEncontrado}
                                        </span>

                                        <span className="bg-green-100 text-green-800 font-black px-3 py-1 rounded-full text-sm">
                                            {match.porcentajeSimilitud}% Similitud
                                        </span>
                                    </div>

                                    <p className="text-gray-700 italic">
                                        "{match.razonamiento}"
                                    </p>

                                    <Button className="mt-4 w-full bg-green-600 hover:bg-green-700">
                                        Confirmar Match y Notificar
                                    </Button>
                                </Card>
                            ))
                        )}
                    </div>
                )}
            </div>
        </div>
    );
};