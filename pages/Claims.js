import React, { useState, useEffect } from 'react';
import { mockApi, initMockData } from '../services/mockData';
import { useAuth } from '../context/AuthContext';

const Claims = () => {
  const [claims, setClaims] = useState([]);
  const [policies, setPolicies] = useState([]);
  const [loading, setLoading] = useState(true);
  const [showForm, setShowForm] = useState(false);
  const { role } = useAuth();
  const [formData, setFormData] = useState({
    policyId: '',
    claimNumber: '',
    incidentDate: '',
    reportDate: new Date().toISOString().split('T')[0],
    description: '',
    claimAmount: '',
    status: 'REGISTERED'
  });

  useEffect(() => {
    initMockData();
    fetchData();
  }, []);

  const fetchData = async () => {
    try {
      const [claimsData, policiesData] = await Promise.all([
        mockApi.getClaims(),
        mockApi.getPolicies()
      ]);
      setClaims(claimsData);
      setPolicies(policiesData);
    } catch (error) {
      console.error('Error fetching data:', error);
    } finally {
      setLoading(false);
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      await mockApi.createClaim(formData);
      setShowForm(false);
      setFormData({
        policyId: '',
        claimNumber: '',
        incidentDate: '',
        reportDate: new Date().toISOString().split('T')[0],
        description: '',
        claimAmount: '',
        status: 'REGISTERED'
      });
      fetchData();
    } catch (error) {
      console.error('Error creating claim:', error);
      alert('Ошибка при создании страхового случая');
    }
  };

  const handleApprove = async (id) => {
    try {
      await mockApi.approveClaim(id);
      fetchData();
    } catch (error) {
      console.error('Error approving claim:', error);
      alert('Ошибка при одобрении');
    }
  };

  const handleReject = async (id) => {
    const reason = prompt('Укажите причину отклонения:');
    if (reason) {
      try {
        await mockApi.rejectClaim(id, reason);
        fetchData();
      } catch (error) {
        console.error('Error rejecting claim:', error);
        alert('Ошибка при отклонении');
      }
    }
  };

  if (loading) return <div className="loading">Загрузка...</div>;

  return (
    <div>
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '20px' }}>
        <h1>Страховые случаи</h1>
        <button className="btn btn-primary" onClick={() => setShowForm(!showForm)}>
          {showForm ? 'Отмена' : '+ Добавить страховой случай'}
        </button>
      </div>

      {showForm && (
        <div className="card">
          <h2>Новый страховой случай</h2>
          <form onSubmit={handleSubmit}>
            <div className="form-group">
              <label>Полис *</label>
              <select
                className="form-control"
                value={formData.policyId}
                onChange={(e) => setFormData({...formData, policyId: e.target.value})}
                required
              >
                <option value="">Выберите полис</option>
                {policies.map(policy => (
                  <option key={policy.id} value={policy.id}>
                    {policy.policyNumber} - {policy.clientName}
                  </option>
                ))}
              </select>
            </div>
            <div className="form-group">
              <label>Номер страхового случая *</label>
              <input
                type="text"
                className="form-control"
                value={formData.claimNumber}
                onChange={(e) => setFormData({...formData, claimNumber: e.target.value})}
                required
              />
            </div>
            <div className="form-group">
              <label>Дата происшествия *</label>
              <input
                type="date"
                className="form-control"
                value={formData.incidentDate}
                onChange={(e) => setFormData({...formData, incidentDate: e.target.value})}
                required
              />
            </div>
            <div className="form-group">
              <label>Дата регистрации *</label>
              <input
                type="date"
                className="form-control"
                value={formData.reportDate}
                onChange={(e) => setFormData({...formData, reportDate: e.target.value})}
                required
              />
            </div>
            <div className="form-group">
              <label>Описание *</label>
              <textarea
                className="form-control"
                value={formData.description}
                onChange={(e) => setFormData({...formData, description: e.target.value})}
                rows="4"
                required
              />
            </div>
            <div className="form-group">
              <label>Сумма выплаты *</label>
              <input
                type="number"
                className="form-control"
                value={formData.claimAmount}
                onChange={(e) => setFormData({...formData, claimAmount: e.target.value})}
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
              <th>Полис</th>
              <th>Дата происшествия</th>
              <th>Сумма выплаты</th>
              <th>Статус</th>
              {role === 'ADMIN' && <th>Действия</th>}
            </tr>
          </thead>
          <tbody>
            {claims.map(claim => (
              <tr key={claim.id}>
                <td>{claim.claimNumber}</td>
                <td>{claim.policyNumber}</td>
                <td>{claim.incidentDate}</td>
                <td>{claim.claimAmount?.toLocaleString()} ₽</td>
                <td>
                  <span className={`badge badge-${
                    claim.status === 'APPROVED' ? 'success' : 
                    claim.status === 'REJECTED' ? 'danger' : 
                    claim.status === 'PAID' ? 'info' : 'warning'
                  }`}>
                    {claim.status}
                  </span>
                </td>
                {role === 'ADMIN' && (
                  <td>
                    {claim.status === 'UNDER_REVIEW' && (
                      <>
                        <button 
                          className="btn btn-success" 
                          onClick={() => handleApprove(claim.id)}
                          style={{ marginRight: '5px' }}
                        >
                          Одобрить
                        </button>
                        <button 
                          className="btn btn-danger" 
                          onClick={() => handleReject(claim.id)}
                        >
                          Отклонить
                        </button>
                      </>
                    )}
                  </td>
                )}
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
};

export default Claims;
