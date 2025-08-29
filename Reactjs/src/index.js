import React from 'react';
import ReactDOM from 'react-dom/client';
import './index.css';
import App from './App';
import store from './Slices/store'
import { createRoot } from 'react-dom/client';
import { Provider } from 'react-redux'

import { BrowserRouter as Router } from 'react-router-dom';

import reportWebVitals from './reportWebVitals';




const root = createRoot(document.getElementById('root'));
root.render(
    <Provider store={store}>
      <App />
    </Provider>
    
);

reportWebVitals();
