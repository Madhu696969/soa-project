import React, { createContext, useContext, useState } from 'react';
import { authService } from '../services/authService';

const AuthContext = createContext(null);

export function AuthProvider({ children }) {
  const [user, setUser] = useState(() => {
    const stored = localStorage.getItem('bibliotech_user');
    return stored ? JSON.parse(stored) : null;
  });

  const login = async (email, password) => {
    const response = await authService.login({ email, password });
    const { token, user: loggedInUser } = response.data;
    localStorage.setItem('bibliotech_token', token);
    localStorage.setItem('bibliotech_user', JSON.stringify(loggedInUser));
    setUser(loggedInUser);
    return loggedInUser;
  };

  const register = async (data) => {
    const response = await authService.register(data);
    const { token, user: newUser } = response.data;
    localStorage.setItem('bibliotech_token', token);
    localStorage.setItem('bibliotech_user', JSON.stringify(newUser));
    setUser(newUser);
    return newUser;
  };

  const logout = () => {
    localStorage.removeItem('bibliotech_token');
    localStorage.removeItem('bibliotech_user');
    setUser(null);
  };

  return (
    <AuthContext.Provider value={{ user, login, register, logout, isAuthenticated: !!user }}>
      {children}
    </AuthContext.Provider>
  );
}

export function useAuth() {
  return useContext(AuthContext);
}
