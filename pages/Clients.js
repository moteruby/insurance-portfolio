import React, { useState, useEffect } from 'react';
import { mockApi, initMockData } from '../services/mockData';

const Clients = () => {
  const [clients, setClients] = useState([]);
  const [loading, setLoading] = useState(true);
  const [showForm, setShowForm] = useState(false);
  const [formData, setFormData] = useState({
    fullName: '',
    birthDate: '',
    passportData: '',
    address: '',
    region: '',
    phone: '',
    email: '',
    status: 'ACTIVE'
  });

  useEffect(() => {
    initMockData();
    fetchClients();
  }, []);

  const fetchClients = async () => {
    try {
      const data = await mockApi.getClients();
      setClients(data);
    } catch (error) {
      console.error('Error fetching clients:', error);
    } finally {
      setLoading(false);
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      await mockApi.createClient(formData);
      setShowForm(false);
      setFormData({
        fullName: '',
        birthDate: '',
        passportData: '',
        address: '',
        region: '',
        phone: '',
        email: '',
        status: 'ACTIVE'
      });
      fetchClients();
    } catch (error) {
      console.error('Error creating client:', error);
      alert('Ошибка при создании клиента');
    }
  };

  const handleDelete = async (id) => {
    if (window.confirm('Удалить клиента?')) {
      try {
        await mockApi.deleteClient(id);
        fetchClients();
      } catch (error) {
        console.error('Error deleting client:', error);
        alert('Ошибка при удалении клиента');
      }
    }
  };

  if (loading) return <div className="loading">Загрузка...</div>;

  return (
    <div>
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '20px' }}>
        <h1>Клиенты</h1>
        <button className="btn btn-primary" onClick={() => setShowForm(!showForm)}>
          {showForm ? 'Отмена' : '+ Добавить клиента'}
        </button>
      </div>

      {showForm && (
        <div className="card">
          <h2>Новый клиент</h2>
          <form onSubmit={handleSubmit}>
            <div className="form-group">
              <label>ФИО *</label>
              <input
                type="text"
                className="form-control"
                value={formData.fullName}
                onChange={(e) => setFormData({...formData, fullName: e.target.value})}
                required
              />
            </div>
            <div className="form-group">
              <label>Дата рождения *</label>
              <input
                type="date"
                className="form-control"
                value={formData.birthDate}
                onChange={(e) => setFormData({...formData, birthDate: e.target.value})}
                required
              />
            </div>
            <div className="form-group">
              <label>Паспортные данные</label>
              <input
                type="text"
                className="form-control"
                value={formData.passportData}
                onChange={(e) => setFormData({...formData, passportData: e.target.value})}
              />
            </div>
            <div className="form-group">
              <label>Адрес</label>
              <input
                type="text"
                className="form-control"
                value={formData.address}
                onChange={(e) => setFormData({...formData, address: e.target.value})}
              />
            </div>
            <div className="form-group">
              <label>Регион</label>
              <input
                type="text"
                className="form-control"
                value={formData.region}
                onChange={(e) => setFormData({...formData, region: e.target.value})}
              />
            </div>
            <div className="form-group">
              <label>Телефон *</label>
              <input
                type="tel"
                className="form-control"
                value={formData.phone}
                onChange={(e) => setFormData({...formData, phone: e.target.value})}
                required
              />
            </div>
            <div className="form-group">
              <label>Email</label>
              <input
                type="email"
                className="form-control"
                value={formData.email}
                onChange={(e) => setFormData({...formData, email: e.target.value})}
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
              <th>ФИО</th>
              <th>Телефон</th>
              <th>Email</th>
              <th>Регион</th>
              <th>Статус</th>
              <th>Действия</th>
            </tr>
          </thead>
          <tbody>
            {clients.map(client => (
              <tr key={client.id}>
                <td>{client.id}</td>
                <td>{client.fullName}</td>
                <td>{client.phone}</td>
                <td>{client.email}</td>
                <td>{client.region}</td>
                <td>
                  <span className={`badge badge-${client.status === 'ACTIVE' ? 'success' : 'warning'}`}>
                    {client.status}
                  </span>
                </td>
                <td>
                  <button className="btn btn-danger" onClick={() => handleDelete(client.id)}>
                    Удалить
                  </button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
};

export default Clients;
