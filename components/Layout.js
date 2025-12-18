import React from 'react';
import { Outlet, Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import './Layout.css';

const Layout = () => {
  const { logout, role } = useAuth();
  const navigate = useNavigate();

  const handleLogout = () => {
    logout();
    navigate('/login');
  };

  return (
    <div className="layout">
      <nav className="navbar">
        <div className="navbar-brand">
          <h2>🏥 Страховой портфель</h2>
        </div>
        <div className="navbar-menu">
          <Link to="/" className="nav-link">Главная</Link>
          
          {/* Администратор - только управление */}
          {role === 'ADMIN' && (
            <>
              <Link to="/clients" className="nav-link">Клиенты</Link>
              <Link to="/payments" className="nav-link">Платежи</Link>
              <Link to="/agents" className="nav-link">Агенты</Link>
            </>
          )}
          
          {/* Агент - работа с клиентами и профиль */}
          {role === 'AGENT' && (
            <>
              <Link to="/clients" className="nav-link">Клиенты</Link>
              <Link to="/policies" className="nav-link">Полисы</Link>
              <Link to="/payments" className="nav-link">Платежи</Link>
              <Link to="/claims" className="nav-link">Страховые случаи</Link>
              <Link to="/profile" className="nav-link">Мой профиль</Link>
            </>
          )}
          
          {/* Аналитик - аналитика и отчеты */}
          {role === 'ANALYST' && (
            <>
              <Link to="/kpi" className="nav-link">KPI</Link>
              <Link to="/analytics" className="nav-link">Аналитика</Link>
              <Link to="/trends" className="nav-link">Тренды</Link>
              <Link to="/reports" className="nav-link">Отчеты</Link>
              <Link to="/agents" className="nav-link">Агенты</Link>
            </>
          )}
        </div>
        <div className="navbar-user">
          <span className="user-role">{role}</span>
          <button onClick={handleLogout} className="btn btn-secondary">Выход</button>
        </div>
      </nav>
      <main className="main-content">
        <Outlet />
      </main>
    </div>
  );
};

export default Layout;
