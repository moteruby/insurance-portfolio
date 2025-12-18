import React, { useState, useEffect } from 'react';
import { mockApi, initMockData } from '../services/mockData';

const Payments = () => {
  const [payments, setPayments] = useState([]);
  const [policies, setPolicies] = useState([]);
  const [loading, setLoading] = useState(true);
  const [showForm, setShowForm] = useState(false);
  const [formData, setFormData] = useState({
    policyId: '',
    amount: '',
    paymentDate: new Date().toISOString().split('T')[0],
    paymentType: 'PREMIUM',
    status: 'PAID',
    comment: ''
  });

  useEffect(() => {
    initMockData();
    fetchData();
  }, []);

  const fetchData = async () => {
    try {
      const [paymentsData, policiesData] = await Promise.all([
        mockApi.getPayments(),
        mockApi.getPolicies()
      ]);
      setPayments(paymentsData);
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
      await mockApi.createPayment(formData);
      setShowForm(false);
      setFormData({
        policyId: '',
        amount: '',
        paymentDate: new Date().toISOString().split('T')[0],
        paymentType: 'PREMIUM',
        status: 'PAID',
        comment: ''
      });
      fetchData();
    } catch (error) {
      console.error('Error creating payment:', error);
      alert('Ошибка при создании платежа');
    }
  };

  if (loading) return <div className="loading">Загрузка...</div>;

  return (
    <div>
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '20px' }}>
        <h1>Платежи</h1>
        <button className="btn btn-primary" onClick={() => setShowForm(!showForm)}>
          {showForm ? 'Отмена' : '+ Добавить платеж'}
        </button>
      </div>

      {showForm && (
        <div className="card">
          <h2>Новый платеж</h2>
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
              <label>Сумма *</label>
              <input
                type="number"
                className="form-control"
                value={formData.amount}
                onChange={(e) => setFormData({...formData, amount: e.target.value})}
                required
              />
            </div>
            <div className="form-group">
              <label>Дата платежа *</label>
              <input
                type="date"
                className="form-control"
                value={formData.paymentDate}
                onChange={(e) => setFormData({...formData, paymentDate: e.target.value})}
                required
              />
            </div>
            <div className="form-group">
              <label>Тип платежа *</label>
              <select
                className="form-control"
                value={formData.paymentType}
                onChange={(e) => setFormData({...formData, paymentType: e.target.value})}
                required
              >
                <option value="PREMIUM">Премия</option>
                <option value="INSTALLMENT">Взнос</option>
              </select>
            </div>
            <div className="form-group">
              <label>Статус *</label>
              <select
                className="form-control"
                value={formData.status}
                onChange={(e) => setFormData({...formData, status: e.target.value})}
                required
              >
                <option value="PAID">Оплачено</option>
                <option value="PENDING">Ожидается</option>
                <option value="OVERDUE">Просрочено</option>
              </select>
            </div>
            <div className="form-group">
              <label>Комментарий</label>
              <textarea
                className="form-control"
                value={formData.comment}
                onChange={(e) => setFormData({...formData, comment: e.target.value})}
                rows="3"
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
              <th>ID</th>
              <th>Полис</th>
              <th>Сумма</th>
              <th>Дата</th>
              <th>Тип</th>
              <th>Статус</th>
              <th>Комментарий</th>
            </tr>
          </thead>
          <tbody>
            {payments.map(payment => (
              <tr key={payment.id}>
                <td>{payment.id}</td>
                <td>{payment.policyNumber}</td>
                <td>{payment.amount?.toLocaleString()} ₽</td>
                <td>{payment.paymentDate}</td>
                <td>{payment.paymentType}</td>
                <td>
                  <span className={`badge badge-${
                    payment.status === 'PAID' ? 'success' : 
                    payment.status === 'PENDING' ? 'warning' : 'danger'
                  }`}>
                    {payment.status}
                  </span>
                </td>
                <td>{payment.comment}</td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
};

export default Payments;
