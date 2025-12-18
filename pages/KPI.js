import React, { useState, useEffect } from 'react';
import { mockApi, initMockData } from '../services/mockData';
import './KPI.css';

const KPI = () => {
  const [kpis, setKpis] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    initMockData();
    fetchKPIs();
  }, []);

  const fetchKPIs = async () => {
    try {
      const [policies, payments, claims, agents] = await Promise.all([
        mockApi.getPolicies(),
        mockApi.getPayments(),
        mockApi.getClaims(),
        mockApi.getAgents()
      ]);

      const totalPremiums = payments.reduce((sum, p) => sum + (p.amount || 0), 0);
      const totalClaims = claims.reduce((sum, c) => sum + (c.claimAmount || 0), 0);
      
      // KPI расчеты
      const policyRetention = ((policies.filter(p => p.status === 'ACTIVE').length / policies.length) * 100).toFixed(1);
      const claimApprovalRate = ((claims.filter(c => c.status === 'APPROVED').length / claims.length) * 100).toFixed(1);
      const avgProcessingTime = '3.5'; // дней (mock)
      const customerSatisfaction = '87'; // % (mock)
      const premiumGrowth = '+12.5'; // % (mock)
      const agentProductivity = (policies.length / agents.length).toFixed(1);

      setKpis({
        policyRetention,
        claimApprovalRate,
        avgProcessingTime,
        customerSatisfaction,
        premiumGrowth,
        agentProductivity,
        totalPolicies: policies.length,
        activePolicies: policies.filter(p => p.status === 'ACTIVE').length,
        totalPremiums,
        totalClaims,
        netProfit: totalPremiums - totalClaims
      });
    } catch (error) {
      console.error('Error fetching KPIs:', error);
    } finally {
      setLoading(false);
    }
  };

  if (loading) return <div className="loading">Загрузка KPI...</div>;

  return (
    <div className="kpi-dashboard">
      <h1>🎯 Ключевые показатели эффективности (KPI)</h1>

      <div className="kpi-grid">
        <div className="kpi-card excellent">
          <div className="kpi-header">
            <span className="kpi-icon">📊</span>
            <span className="kpi-trend up">↑</span>
          </div>
          <h3>Удержание клиентов</h3>
          <p className="kpi-value">{kpis?.policyRetention}%</p>
          <div className="kpi-progress">
            <div className="progress-bar" style={{ width: `${kpis?.policyRetention}%` }}></div>
          </div>
          <span className="kpi-label">Policy Retention Rate</span>
        </div>

        <div className="kpi-card good">
          <div className="kpi-header">
            <span className="kpi-icon">✅</span>
            <span className="kpi-trend up">↑</span>
          </div>
          <h3>Одобрение выплат</h3>
          <p className="kpi-value">{kpis?.claimApprovalRate}%</p>
          <div className="kpi-progress">
            <div className="progress-bar" style={{ width: `${kpis?.claimApprovalRate}%` }}></div>
          </div>
          <span className="kpi-label">Claim Approval Rate</span>
        </div>

        <div className="kpi-card excellent">
          <div className="kpi-header">
            <span className="kpi-icon">⏱️</span>
            <span className="kpi-trend down">↓</span>
          </div>
          <h3>Время обработки</h3>
          <p className="kpi-value">{kpis?.avgProcessingTime} дней</p>
          <div className="kpi-progress">
            <div className="progress-bar" style={{ width: '75%' }}></div>
          </div>
          <span className="kpi-label">Avg Processing Time</span>
        </div>

        <div className="kpi-card good">
          <div className="kpi-header">
            <span className="kpi-icon">😊</span>
            <span className="kpi-trend up">↑</span>
          </div>
          <h3>Удовлетворенность</h3>
          <p className="kpi-value">{kpis?.customerSatisfaction}%</p>
          <div className="kpi-progress">
            <div className="progress-bar" style={{ width: `${kpis?.customerSatisfaction}%` }}></div>
          </div>
          <span className="kpi-label">Customer Satisfaction</span>
        </div>

        <div className="kpi-card excellent">
          <div className="kpi-header">
            <span className="kpi-icon">💹</span>
            <span className="kpi-trend up">↑</span>
          </div>
          <h3>Рост премий</h3>
          <p className="kpi-value">{kpis?.premiumGrowth}%</p>
          <div className="kpi-progress">
            <div className="progress-bar" style={{ width: '65%' }}></div>
          </div>
          <span className="kpi-label">Premium Growth Rate</span>
        </div>

        <div className="kpi-card good">
          <div className="kpi-header">
            <span className="kpi-icon">👨‍💼</span>
            <span className="kpi-trend neutral">→</span>
          </div>
          <h3>Продуктивность агентов</h3>
          <p className="kpi-value">{kpis?.agentProductivity}</p>
          <div className="kpi-progress">
            <div className="progress-bar" style={{ width: '70%' }}></div>
          </div>
          <span className="kpi-label">Policies per Agent</span>
        </div>
      </div>

      <div className="kpi-details">
        <div className="detail-card">
          <h2>📈 Финансовые показатели</h2>
          <div className="detail-grid">
            <div className="detail-item">
              <span className="detail-label">Общие премии</span>
              <span className="detail-value success">{kpis?.totalPremiums?.toLocaleString()} ₽</span>
            </div>
            <div className="detail-item">
              <span className="detail-label">Выплаты по страховым случаям</span>
              <span className="detail-value danger">{kpis?.totalClaims?.toLocaleString()} ₽</span>
            </div>
            <div className="detail-item">
              <span className="detail-label">Чистая прибыль</span>
              <span className="detail-value primary">{kpis?.netProfit?.toLocaleString()} ₽</span>
            </div>
          </div>
        </div>

        <div className="detail-card">
          <h2>📋 Операционные показатели</h2>
          <div className="detail-grid">
            <div className="detail-item">
              <span className="detail-label">Всего полисов</span>
              <span className="detail-value">{kpis?.totalPolicies}</span>
            </div>
            <div className="detail-item">
              <span className="detail-label">Активные полисы</span>
              <span className="detail-value success">{kpis?.activePolicies}</span>
            </div>
            <div className="detail-item">
              <span className="detail-label">Коэффициент активности</span>
              <span className="detail-value">{((kpis?.activePolicies / kpis?.totalPolicies) * 100).toFixed(1)}%</span>
            </div>
          </div>
        </div>
      </div>

      <div className="kpi-legend">
        <h3>Легенда показателей:</h3>
        <div className="legend-items">
          <div className="legend-item">
            <span className="legend-icon excellent">●</span>
            <span>Отличный результат (≥80%)</span>
          </div>
          <div className="legend-item">
            <span className="legend-icon good">●</span>
            <span>Хороший результат (60-79%)</span>
          </div>
          <div className="legend-item">
            <span className="legend-icon warning">●</span>
            <span>Требует внимания (&lt;60%)</span>
          </div>
        </div>
      </div>
    </div>
  );
};

export default KPI;
