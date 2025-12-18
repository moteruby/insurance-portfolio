import React, { createContext, useState, useContext } from 'react';

const AuthContext = createContext();

export const useAuth = () => {
  const context = useContext(AuthContext);
  if (!context) {
    throw new Error('useAuth must be used within AuthProvider');
  }
  return context;
};

// Тестовые пользователи
const MOCK_USERS = {
  admin: { username: 'admin', password: 'admin123', role: 'ADMIN', fullName: 'Администратор' },
  agent1: { username: 'agent1', password: 'password123', role: 'AGENT', fullName: 'Агент Иванов' },
  analyst1: { username: 'analyst1', password: 'password123', role: 'ANALYST', fullName: 'Аналитик Петров' }
};

export const AuthProvider = ({ children }) => {
  const [token, setToken] = useState(localStorage.getItem('token'));
  const [user, setUser] = useState(JSON.parse(localStorage.getItem('user') || 'null'));
  const [role, setRole] = useState(localStorage.getItem('role'));

  const login = async (username, password) => {
    // Имитация задержки сети
    await new Promise(resolve => setTimeout(resolve, 500));
    
    const mockUser = MOCK_USERS[username];
    
    if (mockUser && mockUser.password === password) {
      const mockToken = 'mock-jwt-token-' + Date.now();
      
      setToken(mockToken);
      setUser(mockUser);
      setRole(mockUser.role);
      
      localStorage.setItem('token', mockToken);
      localStorage.setItem('user', JSON.stringify(mockUser));
      localStorage.setItem('role', mockUser.role);
      
      return { success: true };
    } else {
      return { 
        success: false, 
        error: 'Неверный логин или пароль' 
      };
    }
  };

  const logout = () => {
    setToken(null);
    setUser(null);
    setRole(null);
    localStorage.removeItem('token');
    localStorage.removeItem('user');
    localStorage.removeItem('role');
  };

  return (
    <AuthContext.Provider value={{ token, user, role, login, logout }}>
      {children}
    </AuthContext.Provider>
  );
};
