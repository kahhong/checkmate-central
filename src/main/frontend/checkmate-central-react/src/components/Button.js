import React from 'react';
import '../assets/vendor/bootstrap/css/bootstrap.min.css'

const Button = ({ children, onClick, value, disabled }) => {
    return (
        <button type="button" className="btn btn-primary" onClick={onClick} value={value} disabled={disabled}>
            {children}
        </button>
    );
};

export default Button;