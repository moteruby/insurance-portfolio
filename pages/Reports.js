import React, { useState, useEffect } from 'react';
import { mockApi, initMockData } from '../services/mockData';
import './Reports.css';

const Reports = () => {
  const [reportType, setReportType] = useState('summary');
  const [startDate, setStartDate] = useState('2024-01-01');
  const [endDate, setEndDate] = useState('2024-12-31');
  const [reportData, setReportData] = useState(null);
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    initMockData();
  }, []);

  const reportTypes = [
    { value: 'summary', label: 'FR-01: Сводный отчет' },
    { value: 'by-insurance-type', label: 'FR-02: По видам страхования' },
    { value: 'by-agents', label: 'FR-03: По агентам и филиалам' },
    { value: 'dynamics', label: 'FR-04: Динамический отчет' },
    { value: 'comparison', label: 'FR-05: Сравнительный отчет' },
    { value: 'financial-result', label: 'FR-06: Финансовый результат' },
    { value: 'debts', label: 'FR-07: Задолженности' },
    { value: 'expiring-policies', label: 'FR-08: Истекающие полисы' }
  ];

  const fetchReport = async () => {
    setLoading(true);
    try {
      // Имитация генерации отчета
      await new Promise(resolve => setTimeout(resolve, 1000));
      
      const policies = await mockApi.getPolicies();
      const payments = await mockApi.getPayments();
      const claims = await mockApi.getClaims();
      
      const mockReport = {
        reportType,
        period: { startDate, endDate },
        summary: {
          totalPolicies: policies.length,
          totalPremiums: payments.reduce((sum, p) => sum + (p.amount || 0), 0),
          totalClaims: claims.reduce((sum, c) => sum + (c.claimAmount || 0), 0),
          activePolicies: policies.filter(p => p.status === 'ACTIVE').length
        },
        details: policies.slice(0, 5).map(p => ({
          policyNumber: p.policyNumber,
          client: p.clientName,
          premium: p.premium,
          status: p.status
        }))
      };
      
      setReportData(mockReport);
    } catch (error) {
      console.error('Error fetching report:', error);
      alert('Ошибка при получении отчета');
    } finally {
      setLoading(false);
    }
  };

  const exportReport = async (format) => {
    alert(`Экспорт в ${format.toUpperCase()} будет доступен при подключении к backend`);
  };

  return (
    <div className="reports">
      <h1>Отчеты</h1>

      <div className="card">
        <h2>Параметры отчета</h2>
        <div className="report-form">
          <div className="form-group">
            <label>Тип отчета</label>
            <select
              className="form-control"
              value={reportType}
              onChange={(e) => setReportType(e.target.value)}
            >
              {reportTypes.map(type => (
                <option key={type.value} value={type.value}>
                  {type.label}
                </option>
              ))}
            </select>
          </div>

          <div className="form-group">
            <label>Дата начала</label>
            <input
              type="date"
              className="form-control"
              value={startDate}
              onChange={(e) => setStartDate(e.target.value)}
            />
          </div>

          <div className="form-group">
            <label>Дата окончания</label>
            <input
              type="date"
              className="form-control"
              value={endDate}
              onChange={(e) => setEndDate(e.target.value)}
            />
          </div>

          <div className="report-actions">
            <button 
              className="btn btn-primary" 
              onClick={fetchReport}
              disabled={loading}
            >
              {loading ? 'Загрузка...' : 'Сформировать отчет'}
            </button>
            
            {reportData && (
              <>
                <button 
                  className="btn btn-success" 
                  onClick={() => exportReport('excel')}
                >
                  📊 Экспорт в Excel
                </button>
                <button 
                  className="btn btn-danger" 
                  onClick={() => exportReport('pdf')}
                >
                  📄 Экспорт в PDF
                </button>
              </>
            )}
          </div>
        </div>
      </div>

      {reportData && (
        <div className="card">
          <h2>Результаты отчета</h2>
          <div className="report-data">
            <pre>{JSON.stringify(reportData, null, 2)}</pre>
          </div>
        </div>
      )}
    </div>
  );
};

export default Reports;
