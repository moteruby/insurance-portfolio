import React, { useState, useEffect } from 'react';
import { mockApi, initMockData } from '../services/mockData';
import './Analytics.css';

const Analytics = () => {
  const [data, setData] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    initMockData();
    fetchAnalytics();
  }, []);

  const fetchAnalytics = async () => {
    try {
      const [policies, payments, claims, clients] = await Promise.all([
        mockApi.getPolicies(),
        mockApi.getPayments(),
        mockApi.getClaims(),
        mockApi.getClients()
      ]);

      const totalPremiums = payments.reduce((sum, p) => sum + (p.amount || 0), 0);
      const totalClaims = claims.reduce((sum, c) => sum + (c.claimAmount || 0), 0);
      const lossRatio = totalPremiums > 0 ? (totalClaims / totalPremiums * 100).toFixed(2) : 0;

      // Анализ по видам страхования
      const byType = {};
      policies.forEach(p => {
        if (!byType[p.insuranceType]) {
          byType[p.insuranceType] = { count: 0, premium: 0 };
        }
        byType[p.insuranceType].count++;
        byType[p.insuranceType].premium += p.premium || 0;
      });

      // Анализ по регионам
      const byRegion = {};
      clients.forEach(c => {
        byRegion[c.region] = (byRegion[c.region] || 0) + 1;
      });

      setData({
        totalPremiums,
        totalClaims,
        lossRatio,
        profitMargin: (100 - parseFloat(lossRatio)).toFixed(2),
        avgPolicyValue: (totalPremiums / policies.length).toFixed(2),
        byType,
        byRegion,
        activePolicies: policies.filter(p => p.status === 'ACTIVE').length,
        expiredPolicies: policies.filter(p => p.status === 'EXPIRED').length
      });
    } catch (error) {
      console.error('Error fetching analytics:', error);
    } finally {
      setLoading(false);
    }
  };

  if (loading) return <div className="loading">Загрузка аналитики...</div>;

  return (
    <div className="analytics">
      <h1>📊 Аналитическая панель</h1>

      <div className="analytics-grid">
        <div className="analytics-card primary">
          <div className="card-icon">💰</div>
          <div className="card-content">
            <h3>Общие премии</h3>
            <p className="value">{data?.totalPremiums?.toLocaleString()} ₽</p>
            <span className="label">За весь период</span>
          </div>
        </div>

        <div className="analytics-card danger">
          <div className="card-icon">🚨</div>
          <div className="card-content">
            <h3>Выплаты по страховым случаям</h3>
            <p className="value">{data?.totalClaims?.toLocaleString()} ₽</p>
            <span className="label">Общая сумма выплат</span>
          </div>
        </div>

        <div className="analytics-card warning">
          <div className="card-icon">📉</div>
          <div className="card-content">
            <h3>Коэффициент убыточности</h3>
            <p className="value">{data?.lossRatio}%</p>
            <span className="label">Loss Ratio</span>
          </div>
        </div>

        <div className="analytics-card success">
          <div className="card-icon">📈</div>
          <div className="card-content">
            <h3>Маржа прибыли</h3>
            <p className="value">{data?.profitMargin}%</p>
            <span className="label">Profit Margin</span>
          </div>
        </div>
      </div>

      <div className="analytics-section">
        <div className="section-card">
          <h2>📋 Анализ по видам страхования</h2>
          <table className="analytics-table">
            <thead>
              <tr>
                <th>Вид страхования</th>
                <th>Количество полисов</th>
                <th>Общая премия</th>
                <th>Средняя премия</th>
              </tr>
            </thead>
            <tbody>
              {Object.entries(data?.byType || {}).map(([type, info]) => (
                <tr key={type}>
                  <td><strong>{type}</strong></td>
                  <td>{info.count}</td>
                  <td>{info.premium.toLocaleString()} ₽</td>
                  <td>{(info.premium / info.count).toFixed(2)} ₽</td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>

        <div className="section-card">
          <h2>🗺️ Распределение клиентов по регионам</h2>
          <div className="region-chart">
            {Object.entries(data?.byRegion || {}).map(([region, count]) => (
              <div key={region} className="region-item">
                <div className="region-label">
                  <span>{region}</span>
                  <span className="region-count">{count} клиентов</span>
                </div>
                <div className="region-bar">
                  <div 
                    className="region-fill" 
                    style={{ width: `${(count / Object.values(data?.byRegion || {}).reduce((a, b) => a + b, 0)) * 100}%` }}
                  />
                </div>
              </div>
            ))}
          </div>
        </div>
      </div>

      <div className="analytics-footer">
        <div className="footer-stat">
          <span className="stat-label">Активные полисы:</span>
          <span className="stat-value">{data?.activePolicies}</span>
        </div>
        <div className="footer-stat">
          <span className="stat-label">Истекшие полисы:</span>
          <span className="stat-value">{data?.expiredPolicies}</span>
        </div>
        <div className="footer-stat">
          <span className="stat-label">Средняя стоимость полиса:</span>
          <span className="stat-value">{data?.avgPolicyValue} ₽</span>
        </div>
      </div>
    </div>
  );
};

export default Analytics;
