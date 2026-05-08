import React from 'react';

export const Card: React.FC<{children: React.ReactNode, className?: string}> = ({ children, className = '' }) => {
    return (
        <div className={`bg-white shadow-md rounded-xl p-6 border border-gray-100 ${className}`}>
            {children}
        </div>
    );
};