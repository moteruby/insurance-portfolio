import React, { useState, useEffect } from 'react';
import { mockApi, initMockData } from '../services/mockData';
import './Dashboard.css';

const Dashboard = () => {
  const [stats, setStats] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    initMockData();
    fetchStats();
  }, []);

  const fetchStats = async () => {
    try {
      const [clients, policies, payments, claims] = await Promise.all([
        mockApi.getClients(),
        mockApi.getPolicies(),
        mockApi.getPayments(),
        mockApi.getClaims()
      ]);

      setStats({
        clients: clients.length,
        policies: policies.length,
        payments: payments.length,
        claims: claims.length,
        activePolicies: policies.filter(p => p.status === 'ACTIVE').length,
        pendingClaims: claims.filter(c => c.status === 'UNDER_REVIEW').length
      });
    } catch (error) {
      console.error('Error fetching stats:', error);
    } finally {
      setLoading(false);
    }
  };

  if (loading) return <div className="loading">Загрузка...</div>;

  return (
    <div className="dashboard">
      <h1>Панель управления</h1>
      
      <div className="stats-grid">
        <div className="stat-card">
          <div className="stat-icon">👥</div>
          <div className="stat-info">
            <h3>Клиенты</h3>
            <p className="stat-value">{stats?.clients || 0}</p>
          </div>
        </div>

        <div className="stat-card">
          <div className="stat-icon">📄</div>
          <div className="stat-info">
            <h3>Полисы</h3>
            <p className="stat-value">{stats?.policies || 0}</p>
            <p className="stat-detail">Активных: {stats?.activePolicies || 0}</p>
          </div>
        </div>

        <div className="stat-card">
          <div className="stat-icon">💰</div>
          <div className="stat-info">
            <h3>Платежи</h3>
            <p className="stat-value">{stats?.payments || 0}</p>
          </div>
        </div>

        <div className="stat-card">
          <div className="stat-icon">🚨</div>
          <div className="stat-info">
            <h3>Страховые случаи</h3>
            <p className="stat-value">{stats?.claims || 0}</p>
            <p className="stat-detail">На рассмотрении: {stats?.pendingClaims || 0}</p>
          </div>
        </div>
      </div>

      <div className="card">
        <h2>Добро пожаловать в систему управления страховым портфелем!</h2>
        <p>Используйте меню навигации для работы с различными разделами системы.</p>
      </div>
    </div>
  );
};

export default Dashboard;
