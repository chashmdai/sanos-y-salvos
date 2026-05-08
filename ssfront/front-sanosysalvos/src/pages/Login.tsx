import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { AuthService } from '../services/api';
import { Button } from '../components/ui/Button';
import { Card } from '../components/ui/Card';

export const Login = () => {
    const [email, setEmail] = useState('admin@sanosysalvos.cl');
    const [password, setPassword] = useState('1234');
    const [error, setError] = useState('');
    const navigate = useNavigate();

    const handleLogin = async (e: React.FormEvent) => {
        e.preventDefault();
        setError('');
        try {
            const data = await AuthService.login(email, password);
            localStorage.setItem('jwt_token', data.token);
            localStorage.setItem('user_data', JSON.stringify(data));
            navigate('/dashboard');
        } catch (err: any) {
            setError('Credenciales inválidas o servicio inactivo.');
        }
    };

    return (
        <div className="min-h-screen flex items-center justify-center bg-gray-100">
            <Card className="w-full max-w-md">
                <div className="text-center mb-8">
                    <h2 className="text-3xl font-bold text-sanos-blue">Sanos y Salvos</h2>
                    <p className="text-gray-500 mt-2">Plataforma de Recuperación</p>
                </div>
                
                {error && <div className="bg-red-100 text-red-700 p-3 rounded mb-4 text-sm">{error}</div>}

                <form onSubmit={handleLogin} className="flex flex-col gap-4">
                    <div>
                        <label className="block text-sm font-medium text-gray-700">Correo Electrónico</label>
                        <input type="email" value={email} onChange={e => setEmail(e.target.value)} 
                               className="mt-1 block w-full p-2 border border-gray-300 rounded-md focus:ring-sanos-blue focus:border-sanos-blue" required />
                    </div>
                    <div>
                        <label className="block text-sm font-medium text-gray-700">Contraseña</label>
                        <input type="password" value={password} onChange={e => setPassword(e.target.value)} 
                               className="mt-1 block w-full p-2 border border-gray-300 rounded-md focus:ring-sanos-blue focus:border-sanos-blue" required />
                    </div>
                    <Button type="submit" className="mt-4">Ingresar al Sistema</Button>
                </form>
            </Card>
        </div>
    );
};