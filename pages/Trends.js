import React, { useState, useEffect } from 'react';
import { mockApi, initMockData } from '../services/mockData';
import './Trends.css';

const Trends = () => {
  const [trends, setTrends] = useState(null);
  const [loading, setLoading] = useState(true);
  const [period, setPeriod] = useState('month');

  useEffect(() => {
    initMockData();
    fetchTrends();
  }, [period]);

  const fetchTrends = async () => {
    try {
      const [policies, payments, claims] = await Promise.all([
        mockApi.getPolicies(),
        mockApi.getPayments(),
        mockApi.getClaims()
      ]);

      // Генерация трендовых данных (mock)
      const monthlyData = [
        { month: 'Январь', policies: 12, premiums: 180000, claims: 45000 },
        { month: 'Февраль', policies: 15, premiums: 225000, claims: 60000 },
        { month: 'Март', policies: 18, premiums: 270000, claims: 55000 },
        { month: 'Апрель', policies: 14, premiums: 210000, claims: 70000 },
        { month: 'Май', policies: 20, premiums: 300000, claims: 50000 },
        { month: 'Июнь', policies: 22, premiums: 330000, claims: 80000 }
      ];

      const growthRate = '+15.3%';
      const claimTrend = '-8.5%';
      const customerGrowth = '+12.7%';

      setTrends({
        monthlyData,
        growthRate,
        claimTrend,
        customerGrowth,
        totalPolicies: policies.length,
        avgMonthlyGrowth: '3.2'
      });
    } catch (error) {
      console.error('Error fetching trends:', error);
    } finally {
      setLoading(false);
    }
  };

  if (loading) return <div className="loading">Загрузка трендов...</div>;

  return (
    <div className="trends">
      <div className="trends-header">
        <h1>📈 Тренды и прогнозы</h1>
        <div className="period-selector">
          <button 
            className={period === 'week' ? 'active' : ''} 
            onClick={() => setPeriod('week')}
          >
            Неделя
          </button>
          <button 
            className={period === 'month' ? 'active' : ''} 
            onClick={() => setPeriod('month')}
          >
            Месяц
          </button>
          <button 
            className={period === 'year' ? 'active' : ''} 
            onClick={() => setPeriod('year')}
          >
            Год
          </button>
        </div>
      </div>

      <div className="trends-summary">
        <div className="summary-card positive">
          <div className="summary-icon">📊</div>
          <div className="summary-content">
            <h3>Рост продаж</h3>
            <p className="summary-value">{trends?.growthRate}</p>
            <span className="summary-label">По сравнению с прошлым периодом</span>
          </div>
          <div className="trend-indicator up">↑</div>
        </div>

        <div className="summary-card positive">
          <div className="summary-icon">🎯</div>
          <div className="summary-content">
            <h3>Снижение убыточности</h3>
            <p className="summary-value">{trends?.claimTrend}</p>
            <span className="summary-label">Улучшение показателя</span>
          </div>
          <div className="trend-indicator down">↓</div>
        </div>

        <div className="summary-card positive">
          <div className="summary-icon">👥</div>
          <div className="summary-content">
            <h3>Рост клиентской базы</h3>
            <p className="summary-value">{trends?.customerGrowth}</p>
            <span className="summary-label">Новые клиенты</span>
          </div>
          <div className="trend-indicator up">↑</div>
        </div>
      </div>

      <div className="trends-chart-section">
        <div className="chart-card">
          <h2>📊 Динамика продаж полисов</h2>
          <div className="chart-container">
            {trends?.monthlyData.map((item, index) => (
              <div key={index} className="chart-bar-group">
                <div className="chart-bars">
                  <div 
                    className="chart-bar policies" 
                    style={{ height: `${(item.policies / 25) * 100}%` }}
                    title={`${item.policies} полисов`}
                  />
                </div>
                <span className="chart-label">{item.month.substring(0, 3)}</span>
              </div>
            ))}
          </div>
          <div className="chart-legend">
            <div className="legend-item">
              <span className="legend-color policies"></span>
              <span>Количество полисов</span>
            </div>
          </div>
        </div>

        <div className="chart-card">
          <h2>💰 Динамика премий и выплат</h2>
          <div className="chart-container">
            {trends?.monthlyData.map((item, index) => (
              <div key={index} className="chart-bar-group">
                <div className="chart-bars">
                  <div 
                    className="chart-bar premiums" 
                    style={{ height: `${(item.premiums / 350000) * 100}%` }}
                    title={`${item.premiums.toLocaleString()} ₽`}
                  />
                  <div 
                    className="chart-bar claims" 
                    style={{ height: `${(item.claims / 350000) * 100}%` }}
                    title={`${item.claims.toLocaleString()} ₽`}
                  />
                </div>
                <span className="chart-label">{item.month.substring(0, 3)}</span>
              </div>
            ))}
          </div>
          <div className="chart-legend">
            <div className="legend-item">
              <span className="legend-color premiums"></span>
              <span>Премии</span>
            </div>
            <div className="legend-item">
              <span className="legend-color claims"></span>
              <span>Выплаты</span>
            </div>
          </div>
        </div>
      </div>

      <div className="forecast-section">
        <div className="forecast-card">
          <h2>🔮 Прогноз на следующий период</h2>
          <div className="forecast-grid">
            <div className="forecast-item">
              <span className="forecast-icon">📈</span>
              <div className="forecast-content">
                <h4>Ожидаемый рост продаж</h4>
                <p className="forecast-value">+18%</p>
                <span className="forecast-confidence">Уверенность: 85%</span>
              </div>
            </div>
            <div className="forecast-item">
              <span className="forecast-icon">💵</span>
              <div className="forecast-content">
                <h4>Прогноз премий</h4>
                <p className="forecast-value">~350,000 ₽</p>
                <span className="forecast-confidence">Уверенность: 78%</span>
              </div>
            </div>
            <div className="forecast-item">
              <span className="forecast-icon">👥</span>
              <div className="forecast-content">
                <h4>Новые клиенты</h4>
                <p className="forecast-value">~25 клиентов</p>
                <span className="forecast-confidence">Уверенность: 72%</span>
              </div>
            </div>
          </div>
        </div>
      </div>

      <div className="insights-section">
        <h2>💡 Ключевые инсайты</h2>
        <div className="insights-grid">
          <div className="insight-card positive">
            <span className="insight-icon">✅</span>
            <p>Продажи полисов показывают стабильный рост на протяжении последних 6 месяцев</p>
          </div>
          <div className="insight-card positive">
            <span className="insight-icon">✅</span>
            <p>Коэффициент убыточности снижается, что указывает на улучшение качества андеррайтинга</p>
          </div>
          <div className="insight-card warning">
            <span className="insight-icon">⚠️</span>
            <p>В апреле наблюдался всплеск выплат - рекомендуется провести анализ причин</p>
          </div>
          <div className="insight-card positive">
            <span className="insight-icon">✅</span>
            <p>Клиентская база растет быстрее рынка - эффективная маркетинговая стратегия</p>
          </div>
        </div>
      </div>
    </div>
  );
};

export default Trends;
