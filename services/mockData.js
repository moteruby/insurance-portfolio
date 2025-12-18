// Инициализация тестовых данных в localStorage
export const initMockData = () => {
  if (!localStorage.getItem('clients')) {
    localStorage.setItem('clients', JSON.stringify([
      { id: 1, fullName: 'Иванов Иван Иванович', birthDate: '1985-05-15', passportData: '1234 567890', address: 'г. Москва, ул. Ленина, д. 10', region: 'Москва', phone: '+7 (999) 123-45-67', email: 'ivanov@example.com', status: 'ACTIVE' },
      { id: 2, fullName: 'Петрова Мария Сергеевна', birthDate: '1990-08-20', passportData: '2345 678901', address: 'г. Санкт-Петербург, пр. Невский, д. 25', region: 'Санкт-Петербург', phone: '+7 (999) 234-56-78', email: 'petrova@example.com', status: 'ACTIVE' },
      { id: 3, fullName: 'Сидоров Петр Александрович', birthDate: '1978-03-10', passportData: '3456 789012', address: 'г. Казань, ул. Баумана, д. 5', region: 'Татарстан', phone: '+7 (999) 345-67-89', email: 'sidorov@example.com', status: 'ACTIVE' }
    ]));
  }

  if (!localStorage.getItem('agents')) {
    localStorage.setItem('agents', JSON.stringify([
      { id: 1, fullName: 'Агентов Агент Агентович', agentCode: 'AGT-001', branch: 'Московский филиал', phone: '+7 (999) 111-11-11', email: 'agent1@insurance.com', hireDate: '2020-01-15', status: 'ACTIVE' },
      { id: 2, fullName: 'Продавцов Продавец Продавцович', agentCode: 'AGT-002', branch: 'Санкт-Петербургский филиал', phone: '+7 (999) 222-22-22', email: 'agent2@insurance.com', hireDate: '2021-03-20', status: 'ACTIVE' }
    ]));
  }

  if (!localStorage.getItem('policies')) {
    localStorage.setItem('policies', JSON.stringify([
      { id: 1, policyNumber: 'POL-2024-001', clientId: 1, clientName: 'Иванов Иван Иванович', agentId: 1, agentName: 'Агентов Агент Агентович', insuranceType: 'PROPERTY', insuredAmount: 1000000, premium: 15000, startDate: '2024-01-01', endDate: '2024-12-31', status: 'ACTIVE' },
      { id: 2, policyNumber: 'POL-2024-002', clientId: 2, clientName: 'Петрова Мария Сергеевна', agentId: 1, agentName: 'Агентов Агент Агентович', insuranceType: 'PERSONAL', insuredAmount: 500000, premium: 8000, startDate: '2024-02-01', endDate: '2025-01-31', status: 'ACTIVE' },
      { id: 3, policyNumber: 'POL-2024-003', clientId: 3, clientName: 'Сидоров Петр Александрович', agentId: 2, agentName: 'Продавцов Продавец Продавцович', insuranceType: 'LIABILITY', insuredAmount: 750000, premium: 12000, startDate: '2024-03-01', endDate: '2025-02-28', status: 'ACTIVE' }
    ]));
  }

  if (!localStorage.getItem('payments')) {
    localStorage.setItem('payments', JSON.stringify([
      { id: 1, policyId: 1, policyNumber: 'POL-2024-001', amount: 15000, paymentDate: '2024-01-15', paymentType: 'PREMIUM', status: 'PAID', comment: 'Оплата полной премии' },
      { id: 2, policyId: 2, policyNumber: 'POL-2024-002', amount: 4000, paymentDate: '2024-02-10', paymentType: 'INSTALLMENT', status: 'PAID', comment: 'Первый взнос' },
      { id: 3, policyId: 2, policyNumber: 'POL-2024-002', amount: 4000, paymentDate: '2024-08-10', paymentType: 'INSTALLMENT', status: 'PENDING', comment: 'Второй взнос' }
    ]));
  }

  if (!localStorage.getItem('claims')) {
    localStorage.setItem('claims', JSON.stringify([
      { id: 1, policyId: 1, policyNumber: 'POL-2024-001', claimNumber: 'CLM-2024-001', incidentDate: '2024-06-15', reportDate: '2024-06-16', description: 'Повреждение имущества в результате пожара', claimAmount: 250000, status: 'APPROVED', approvedAmount: 250000 },
      { id: 2, policyId: 2, policyNumber: 'POL-2024-002', claimNumber: 'CLM-2024-002', incidentDate: '2024-07-20', reportDate: '2024-07-21', description: 'Несчастный случай', claimAmount: 100000, status: 'UNDER_REVIEW', approvedAmount: null }
    ]));
  }

  // Счетчики для ID
  if (!localStorage.getItem('nextClientId')) localStorage.setItem('nextClientId', '4');
  if (!localStorage.getItem('nextPolicyId')) localStorage.setItem('nextPolicyId', '4');
  if (!localStorage.getItem('nextPaymentId')) localStorage.setItem('nextPaymentId', '4');
  if (!localStorage.getItem('nextClaimId')) localStorage.setItem('nextClaimId', '3');
};

// Вспомогательные функции для работы с localStorage
export const getFromStorage = (key) => {
  const data = localStorage.getItem(key);
  return data ? JSON.parse(data) : [];
};

export const saveToStorage = (key, data) => {
  localStorage.setItem(key, JSON.stringify(data));
};

export const getNextId = (key) => {
  const nextId = parseInt(localStorage.getItem(key) || '1');
  localStorage.setItem(key, (nextId + 1).toString());
  return nextId;
};

// API функции для работы с данными
export const mockApi = {
  // Клиенты
  getClients: () => Promise.resolve(getFromStorage('clients')),
  getClient: (id) => Promise.resolve(getFromStorage('clients').find(c => c.id === parseInt(id))),
  createClient: (client) => {
    const clients = getFromStorage('clients');
    const newClient = { ...client, id: getNextId('nextClientId') };
    clients.push(newClient);
    saveToStorage('clients', clients);
    return Promise.resolve(newClient);
  },
  updateClient: (id, client) => {
    const clients = getFromStorage('clients');
    const index = clients.findIndex(c => c.id === parseInt(id));
    if (index !== -1) {
      clients[index] = { ...clients[index], ...client };
      saveToStorage('clients', clients);
      return Promise.resolve(clients[index]);
    }
    return Promise.reject(new Error('Client not found'));
  },
  deleteClient: (id) => {
    const clients = getFromStorage('clients').filter(c => c.id !== parseInt(id));
    saveToStorage('clients', clients);
    return Promise.resolve();
  },

  // Полисы
  getPolicies: () => Promise.resolve(getFromStorage('policies')),
  getPolicy: (id) => Promise.resolve(getFromStorage('policies').find(p => p.id === parseInt(id))),
  createPolicy: (policy) => {
    const policies = getFromStorage('policies');
    const clients = getFromStorage('clients');
    const agents = getFromStorage('agents');
    
    const client = clients.find(c => c.id === parseInt(policy.clientId));
    const agent = agents.find(a => a.id === parseInt(policy.agentId));
    
    const newPolicy = { 
      ...policy, 
      id: getNextId('nextPolicyId'),
      clientName: client?.fullName || 'Unknown',
      agentName: agent?.fullName || 'Unknown'
    };
    policies.push(newPolicy);
    saveToStorage('policies', policies);
    return Promise.resolve(newPolicy);
  },
  updatePolicy: (id, policy) => {
    const policies = getFromStorage('policies');
    const index = policies.findIndex(p => p.id === parseInt(id));
    if (index !== -1) {
      policies[index] = { ...policies[index], ...policy };
      saveToStorage('policies', policies);
      return Promise.resolve(policies[index]);
    }
    return Promise.reject(new Error('Policy not found'));
  },
  deletePolicy: (id) => {
    const policies = getFromStorage('policies').filter(p => p.id !== parseInt(id));
    saveToStorage('policies', policies);
    return Promise.resolve();
  },

  // Платежи
  getPayments: () => Promise.resolve(getFromStorage('payments')),
  getPayment: (id) => Promise.resolve(getFromStorage('payments').find(p => p.id === parseInt(id))),
  createPayment: (payment) => {
    const payments = getFromStorage('payments');
    const policies = getFromStorage('policies');
    
    const policy = policies.find(p => p.id === parseInt(payment.policyId));
    
    const newPayment = { 
      ...payment, 
      id: getNextId('nextPaymentId'),
      policyNumber: policy?.policyNumber || 'Unknown'
    };
    payments.push(newPayment);
    saveToStorage('payments', payments);
    return Promise.resolve(newPayment);
  },

  // Страховые случаи
  getClaims: () => Promise.resolve(getFromStorage('claims')),
  getClaim: (id) => Promise.resolve(getFromStorage('claims').find(c => c.id === parseInt(id))),
  createClaim: (claim) => {
    const claims = getFromStorage('claims');
    const policies = getFromStorage('policies');
    
    const policy = policies.find(p => p.id === parseInt(claim.policyId));
    
    const newClaim = { 
      ...claim, 
      id: getNextId('nextClaimId'),
      policyNumber: policy?.policyNumber || 'Unknown'
    };
    claims.push(newClaim);
    saveToStorage('claims', claims);
    return Promise.resolve(newClaim);
  },
  approveClaim: (id) => {
    const claims = getFromStorage('claims');
    const index = claims.findIndex(c => c.id === parseInt(id));
    if (index !== -1) {
      claims[index].status = 'APPROVED';
      claims[index].approvedAmount = claims[index].claimAmount;
      saveToStorage('claims', claims);
      return Promise.resolve(claims[index]);
    }
    return Promise.reject(new Error('Claim not found'));
  },
  rejectClaim: (id, reason) => {
    const claims = getFromStorage('claims');
    const index = claims.findIndex(c => c.id === parseInt(id));
    if (index !== -1) {
      claims[index].status = 'REJECTED';
      claims[index].rejectionReason = reason;
      saveToStorage('claims', claims);
      return Promise.resolve(claims[index]);
    }
    return Promise.reject(new Error('Claim not found'));
  },

  // Агенты
  getAgents: () => Promise.resolve(getFromStorage('agents'))
};
