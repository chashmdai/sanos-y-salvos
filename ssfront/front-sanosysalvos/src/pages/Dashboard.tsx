import { useState } from 'react';
import { Navbar } from '../components/layout/Navbar';
import { Card } from '../components/ui/Card';
import { Button } from '../components/ui/Button';

export const Dashboard = () => {
    // CRUD Simulado en memoria para la demo
    const [usuarios, setUsuarios] = useState([
        { id: 1, nombre: 'Admin Sistema', email: 'admin@sanosysalvos.cl', rol: 'ROLE_ADMIN' },
        { id: 2, nombre: 'Juan Pérez', email: 'juan.perez@gmail.com', rol: 'ROLE_DUEÑO' }
    ]);

    const eliminarUsuario = (id: number) => {
        setUsuarios(usuarios.filter(u => u.id !== id));
    };

    return (
        <div className="min-h-screen bg-gray-50">
            <Navbar />
            <div className="container mx-auto p-8">
                <div className="flex justify-between items-center mb-6">
                    <h2 className="text-2xl font-bold text-gray-800">Gestión de Usuarios</h2>
                    <Button>+ Nuevo Usuario</Button>
                </div>

                <Card>
                    <table className="w-full text-left border-collapse">
                        <thead>
                            <tr className="border-b-2 border-gray-200">
                                <th className="p-3 font-semibold text-gray-600">ID</th>
                                <th className="p-3 font-semibold text-gray-600">Nombre</th>
                                <th className="p-3 font-semibold text-gray-600">Correo</th>
                                <th className="p-3 font-semibold text-gray-600">Rol</th>
                                <th className="p-3 font-semibold text-gray-600">Acciones</th>
                            </tr>
                        </thead>
                        <tbody>
                            {usuarios.map(u => (
                                <tr key={u.id} className="border-b border-gray-100 hover:bg-gray-50">
                                    <td className="p-3">{u.id}</td>
                                    <td className="p-3 font-medium">{u.nombre}</td>
                                    <td className="p-3 text-gray-500">{u.email}</td>
                                    <td className="p-3">
                                        <span className="bg-blue-100 text-sanos-blue text-xs px-2 py-1 rounded-full font-bold">
                                            {u.rol}
                                        </span>
                                    </td>
                                    <td className="p-3">
                                        <button className="text-sanos-orange hover:underline mr-3">Editar</button>
                                        <button onClick={() => eliminarUsuario(u.id)} className="text-red-500 hover:underline">Eliminar</button>
                                    </td>
                                </tr>
                            ))}
                        </tbody>
                    </table>
                </Card>
            </div>
        </div>
    );
};