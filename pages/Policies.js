import React, { useState, useEffect } from 'react';
import { mockApi, initMockData } from '../services/mockData';

const Policies = () => {
  const [policies, setPolicies] = useState([]);
  const [clients, setClients] = useState([]);
  const [agents, setAgents] = useState([]);
  const [loading, setLoading] = useState(true);
  const [showForm, setShowForm] = useState(false);
  const [formData, setFormData] = useState({
    policyNumber: '',
    clientId: '',
    agentId: '',
    insuranceType: 'PROPERTY',
    insuredAmount: '',
    premium: '',
    startDate: '',
    endDate: '',
    status: 'ACTIVE'
  });

  useEffect(() => {
    initMockData();
    fetchData();
  }, []);

  const fetchData = async () => {
    try {
      const [policiesData, clientsData, agentsData] = await Promise.all([
        mockApi.getPolicies(),
        mockApi.getClients(),
        mockApi.getAgents()
      ]);
      setPolicies(policiesData);
      setClients(clientsData);
      setAgents(agentsData);
    } catch (error) {
      console.error('Error fetching data:', error);
    } finally {
      setLoading(false);
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      await mockApi.createPolicy(formData);
      setShowForm(false);
      setFormData({
        policyNumber: '',
        clientId: '',
        agentId: '',
        insuranceType: 'PROPERTY',
        insuredAmount: '',
        premium: '',
        startDate: '',
        endDate: '',
        status: 'ACTIVE'
      });
      fetchData();
    } catch (error) {
      console.error('Error creating policy:', error);
      alert('Ошибка при создании полиса');
    }
  };

  if (loading) return <div className="loading">Загрузка...</div>;

  return (
    <div>
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '20px' }}>
        <h1>Полисы</h1>
        <button className="btn btn-primary" onClick={() => setShowForm(!showForm)}>
          {showForm ? 'Отмена' : '+ Добавить полис'}
        </button>
      </div>

      {showForm && (
        <div className="card">
          <h2>Новый полис</h2>
          <form onSubmit={handleSubmit}>
            <div className="form-group">
              <label>Номер полиса *</label>
              <input
                type="text"
                className="form-control"
                value={formData.policyNumber}
                onChange={(e) => setFormData({...formData, policyNumber: e.target.value})}
                required
              />
            </div>
            <div className="form-group">
              <label>Клиент *</label>
              <select
                className="form-control"
                value={formData.clientId}
                onChange={(e) => setFormData({...formData, clientId: e.target.value})}
                required
              >
                <option value="">Выберите клиента</option>
                {clients.map(client => (
                  <option key={client.id} value={client.id}>{client.fullName}</option>
                ))}
              </select>
            </div>
            <div className="form-group">
              <label>Агент *</label>
              <select
                className="form-control"
                value={formData.agentId}
                onChange={(e) => setFormData({...formData, agentId: e.target.value})}
                required
              >
                <option value="">Выберите агента</option>
                {agents.map(agent => (
                  <option key={agent.id} value={agent.id}>{agent.fullName}</option>
                ))}
              </select>
            </div>
            <div className="form-group">
              <label>Вид страхования *</label>
              <select
                className="form-control"
                value={formData.insuranceType}
                onChange={(e) => setFormData({...formData, insuranceType: e.target.value})}
                required
              >
                <option value="PERSONAL">Личное</option>
                <option value="PROPERTY">Имущественное</option>
                <option value="LIABILITY">Ответственность</option>
                <option value="COMBINED">Комбинированное</option>
              </select>
            </div>
            <div className="form-group">
              <label>Страховая сумма *</label>
              <input
                type="number"
                className="form-control"
                value={formData.insuredAmount}
                onChange={(e) => setFormData({...formData, insuredAmount: e.target.value})}
                required
              />
            </div>
            <div className="form-group">
              <label>Премия *</label>
              <input
                type="number"
                className="form-control"
                value={formData.premium}
                onChange={(e) => setFormData({...formData, premium: e.target.value})}
                required
              />
            </div>
            <div className="form-group">
              <label>Дата начала *</label>
              <input
                type="date"
                className="form-control"
                value={formData.startDate}
                onChange={(e) => setFormData({...formData, startDate: e.target.value})}
                required
              />
            </div>
            <div className="form-group">
              <label>Дата окончания *</label>
              <input
                type="date"
                className="form-control"
                value={formData.endDate}
                onChange={(e) => setFormData({...formData, endDate: e.target.value})}
                required
              />
            </div>
            <button type="submit" className="btn btn-success">Создать</button>
          </form>
        </div>
      )}

      <div className="card">
        <table className="table">
          <thead>
            <tr>
              <th>Номер</th>
              <th>Клиент</th>
              <th>Вид страхования</th>
              <th>Страховая сумма</th>
              <th>Премия</th>
              <th>Период</th>
              <th>Статус</th>
            </tr>
          </thead>
          <tbody>
            {policies.map(policy => (
              <tr key={policy.id}>
                <td>{policy.policyNumber}</td>
                <td>{policy.clientName}</td>
                <td>{policy.insuranceType}</td>
                <td>{policy.insuredAmount?.toLocaleString()} ₽</td>
                <td>{policy.premium?.toLocaleString()} ₽</td>
                <td>{policy.startDate} - {policy.endDate}</td>
                <td>
                  <span className={`badge badge-${policy.status === 'ACTIVE' ? 'success' : 'warning'}`}>
                    {policy.status}
                  </span>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
};

export default Policies;
