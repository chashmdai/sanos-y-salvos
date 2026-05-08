import { Link, useNavigate } from 'react-router-dom';

export const Navbar = () => {
    const navigate = useNavigate();
    
    const handleLogout = () => {
        localStorage.removeItem('jwt_token');
        localStorage.removeItem('user_data');
        navigate('/');
    };

    return (
        <nav className="bg-sanos-blue text-white p-4 shadow-lg">
            <div className="container mx-auto flex justify-between items-center">
                <h1 className="text-2xl font-bold tracking-wide">Sanos y Salvos <span className="text-sanos-orange">BETA</span></h1>
                <div className="flex gap-6 items-center font-medium">
                    <Link to="/dashboard" className="hover:text-sanos-orange transition-colors">Usuarios (CRUD)</Link>
                    <Link to="/match" className="hover:text-sanos-orange transition-colors">Motor IA</Link>
                    <button onClick={handleLogout} className="text-red-300 hover:text-red-100 ml-4 border border-red-400 px-3 py-1 rounded">
                        Cerrar Sesión
                    </button>
                </div>
            </div>
        </nav>
    );
};