import React, { useState, useEffect } from 'react';
import { mockApi, initMockData } from '../services/mockData';

const Agents = () => {
  const [agents, setAgents] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    initMockData();
    fetchAgents();
  }, []);

  const fetchAgents = async () => {
    try {
      const data = await mockApi.getAgents();
      setAgents(data);
    } catch (error) {
      console.error('Error fetching agents:', error);
    } finally {
      setLoading(false);
    }
  };

  if (loading) return <div className="loading">Загрузка...</div>;

  return (
    <div>
      <h1>Агенты</h1>

      <div className="card">
        <table className="table">
          <thead>
            <tr>
              <th>ID</th>
              <th>ФИО</th>
              <th>Код агента</th>
              <th>Филиал</th>
              <th>Телефон</th>
              <th>Email</th>
              <th>Дата найма</th>
              <th>Статус</th>
            </tr>
          </thead>
          <tbody>
            {agents.map(agent => (
              <tr key={agent.id}>
                <td>{agent.id}</td>
                <td>{agent.fullName}</td>
                <td>{agent.agentCode}</td>
                <td>{agent.branch}</td>
                <td>{agent.phone}</td>
                <td>{agent.email}</td>
                <td>{agent.hireDate}</td>
                <td>
                  <span className={`badge badge-${agent.status === 'ACTIVE' ? 'success' : 'warning'}`}>
                    {agent.status}
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

export default Agents;
