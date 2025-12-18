import React from 'react';
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import { AuthProvider, useAuth } from './context/AuthContext';
import Login from './pages/Login';
import Dashboard from './pages/Dashboard';
import Clients from './pages/Clients';
import Policies from './pages/Policies';
import Payments from './pages/Payments';
import Claims from './pages/Claims';
import Agents from './pages/Agents';
import Reports from './pages/Reports';
import KPI from './pages/KPI';
import Analytics from './pages/Analytics';
import Trends from './pages/Trends';
import Profile from './pages/Profile';
import Layout from './components/Layout';
import './App.css';

const PrivateRoute = ({ children }) => {
  const { token } = useAuth();
  return token ? children : <Navigate to="/login" />;
};

function App() {
  return (
    <AuthProvider>
      <Router>
        <Routes>
          <Route path="/login" element={<Login />} />
          <Route path="/" element={<PrivateRoute><Layout /></PrivateRoute>}>
            <Route index element={<Dashboard />} />
            <Route path="clients" element={<Clients />} />
            <Route path="policies" element={<Policies />} />
            <Route path="payments" element={<Payments />} />
            <Route path="claims" element={<Claims />} />
            <Route path="agents" element={<Agents />} />
            <Route path="reports" element={<Reports />} />
            <Route path="kpi" element={<KPI />} />
            <Route path="analytics" element={<Analytics />} />
            <Route path="trends" element={<Trends />} />
            <Route path="profile" element={<Profile />} />
          </Route>
        </Routes>
      </Router>
    </AuthProvider>
  );
}

export default App;
