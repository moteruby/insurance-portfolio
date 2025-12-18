import React, { useState } from 'react';
import { useAuth } from '../context/AuthContext';
import './Profile.css';

const Profile = () => {
  const { user } = useAuth();
  const [isEditing, setIsEditing] = useState(false);
  const [formData, setFormData] = useState({
    fullName: user?.fullName || '',
    email: 'agent@insurance.com',
    phone: '+7 (999) 123-45-67',
    branch: 'Московский филиал',
    agentCode: 'AGT-001',
    hireDate: '2020-01-15',
    address: 'г. Москва, ул. Ленина, д. 10',
    specialization: 'Имущественное страхование'
  });

  const [stats] = useState({
    totalPolicies: 45,
    activePolicies: 38,
    totalPremiums: 675000,
    avgPolicyValue: 15000,
    clientSatisfaction: 92,
    monthlyTarget: 50,
    currentMonth: 12
  });

  const handleSubmit = (e) => {
    e.preventDefault();
    // Сохранение в localStorage
    const updatedUser = { ...user, ...formData };
    localStorage.setItem('user', JSON.stringify(updatedUser));
    setIsEditing(false);
    alert('Профиль успешно обновлен!');
  };

  const handleChange = (e) => {
    setFormData({
      ...formData,
      [e.target.name]: e.target.value
    });
  };

  return (
    <div className="profile">
      <h1>👤 Мой профиль</h1>

      <div className="profile-layout">
        <div className="profile-main">
          <div className="profile-card">
            <div className="profile-header">
              <div className="profile-avatar">
                <span className="avatar-icon">👨‍💼</span>
              </div>
              <div className="profile-info">
                <h2>{formData.fullName}</h2>
                <p className="profile-role">{user?.role === 'AGENT' ? 'Страховой агент' : user?.role}</p>
                <p className="profile-code">Код агента: {formData.agentCode}</p>
              </div>
              <button 
                className="btn btn-primary" 
                onClick={() => setIsEditing(!isEditing)}
              >
                {isEditing ? 'Отмена' : '✏️ Редактировать'}
              </button>
            </div>

            {isEditing ? (
              <form onSubmit={handleSubmit} className="profile-form">
                <div className="form-row">
                  <div className="form-group">
                    <label>ФИО</label>
                    <input
                      type="text"
                      name="fullName"
                      className="form-control"
                      value={formData.fullName}
                      onChange={handleChange}
                      required
                    />
                  </div>
                  <div className="form-group">
                    <label>Email</label>
                    <input
                      type="email"
                      name="email"
                      className="form-control"
                      value={formData.email}
                      onChange={handleChange}
                      required
                    />
                  </div>
                </div>

                <div className="form-row">
                  <div className="form-group">
                    <label>Телефон</label>
                    <input
                      type="tel"
                      name="phone"
                      className="form-control"
                      value={formData.phone}
                      onChange={handleChange}
                      required
                    />
                  </div>
                  <div className="form-group">
                    <label>Филиал</label>
                    <input
                      type="text"
                      name="branch"
                      className="form-control"
                      value={formData.branch}
                      onChange={handleChange}
                      required
                    />
                  </div>
                </div>

                <div className="form-group">
                  <label>Адрес</label>
                  <input
                    type="text"
                    name="address"
                    className="form-control"
                    value={formData.address}
                    onChange={handleChange}
                  />
                </div>

                <div className="form-group">
                  <label>Специализация</label>
                  <select
                    name="specialization"
                    className="form-control"
                    value={formData.specialization}
                    onChange={handleChange}
                  >
                    <option value="Имущественное страхование">Имущественное страхование</option>
                    <option value="Личное страхование">Личное страхование</option>
                    <option value="Страхование ответственности">Страхование ответственности</option>
                    <option value="Комбинированное страхование">Комбинированное страхование</option>
                  </select>
                </div>

                <button type="submit" className="btn btn-success">
                  💾 Сохранить изменения
                </button>
              </form>
            ) : (
              <div className="profile-details">
                <div className="detail-row">
                  <span className="detail-label">📧 Email:</span>
                  <span className="detail-value">{formData.email}</span>
                </div>
                <div className="detail-row">
                  <span className="detail-label">📱 Телефон:</span>
                  <span className="detail-value">{formData.phone}</span>
                </div>
                <div className="detail-row">
                  <span className="detail-label">🏢 Филиал:</span>
                  <span className="detail-value">{formData.branch}</span>
                </div>
                <div className="detail-row">
                  <span className="detail-label">📅 Дата найма:</span>
                  <span className="detail-value">{formData.hireDate}</span>
                </div>
                <div className="detail-row">
                  <span className="detail-label">📍 Адрес:</span>
                  <span className="detail-value">{formData.address}</span>
                </div>
                <div className="detail-row">
                  <span className="detail-label">🎯 Специализация:</span>
                  <span className="detail-value">{formData.specialization}</span>
                </div>
              </div>
            )}
          </div>
        </div>

        <div className="profile-sidebar">
          <div className="stats-card">
            <h3>📊 Моя статистика</h3>
            <div className="stat-item">
              <span className="stat-label">Всего полисов</span>
              <span className="stat-value">{stats.totalPolicies}</span>
            </div>
            <div className="stat-item">
              <span className="stat-label">Активные полисы</span>
              <span className="stat-value success">{stats.activePolicies}</span>
            </div>
            <div className="stat-item">
              <span className="stat-label">Общие премии</span>
              <span className="stat-value primary">{stats.totalPremiums.toLocaleString()} ₽</span>
            </div>
            <div className="stat-item">
              <span className="stat-label">Средний чек</span>
              <span className="stat-value">{stats.avgPolicyValue.toLocaleString()} ₽</span>
            </div>
          </div>

          <div className="performance-card">
            <h3>🎯 Выполнение плана</h3>
            <div className="progress-section">
              <div className="progress-header">
                <span>Месячный план</span>
                <span>{stats.currentMonth} / {stats.monthlyTarget}</span>
              </div>
              <div className="progress-bar-container">
                <div 
                  className="progress-bar-fill" 
                  style={{ width: `${(stats.currentMonth / stats.monthlyTarget) * 100}%` }}
                />
              </div>
              <span className="progress-label">
                {((stats.currentMonth / stats.monthlyTarget) * 100).toFixed(0)}% выполнено
              </span>
            </div>

            <div className="satisfaction-section">
              <h4>Удовлетворенность клиентов</h4>
              <div className="satisfaction-score">
                <span className="score-value">{stats.clientSatisfaction}%</span>
                <span className="score-label">Отличный результат!</span>
              </div>
            </div>
          </div>

          <div className="achievements-card">
            <h3>🏆 Достижения</h3>
            <div className="achievement-item">
              <span className="achievement-icon">🥇</span>
              <span className="achievement-text">Лучший агент месяца</span>
            </div>
            <div className="achievement-item">
              <span className="achievement-icon">⭐</span>
              <span className="achievement-text">50+ довольных клиентов</span>
            </div>
            <div className="achievement-item">
              <span className="achievement-icon">💎</span>
              <span className="achievement-text">Премиум-агент 2024</span>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default Profile;
