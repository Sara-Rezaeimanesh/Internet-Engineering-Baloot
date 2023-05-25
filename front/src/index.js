import React from 'react';
import ReactDOM from 'react-dom/client';
import './baloot.css';
import './login.css';
import './product.css';
import './profile.css';
import './register.css';

import App from './App';
import "bootstrap/dist/css/bootstrap.min.css";
import "bootstrap/dist/js/bootstrap.bundle.min";

const root = ReactDOM.createRoot(document.getElementById('root'));
root.render(
  <React.StrictMode>
    <App />
  </React.StrictMode>
);


