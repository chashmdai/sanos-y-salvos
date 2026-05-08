import React from 'react';

interface ButtonProps extends React.ButtonHTMLAttributes<HTMLButtonElement> {}

export const Button: React.FC<ButtonProps> = ({ children, className = '', ...props }) => {
    return (
        <button 
            className={`px-4 py-2 bg-sanos-blue text-white font-semibold rounded-lg hover:bg-blue-900 transition-colors disabled:bg-gray-400 ${className}`}
            {...props}
        >
            {children}
        </button>
    );
};