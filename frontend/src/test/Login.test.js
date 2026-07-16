import React from 'react';
import { render, screen, fireEvent, waitFor } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import { MemoryRouter } from 'react-router-dom';
import Login from '../pages/Login';
import { useAuth } from '../context/AuthContext';

jest.mock('../context/AuthContext', () => ({
  useAuth: jest.fn(),
}));

jest.mock('react-hot-toast', () => ({
  __esModule: true,
  default: {
    success: jest.fn(),
    error: jest.fn(),
  },
}));

describe('Login', () => {
  beforeEach(() => {
    useAuth.mockReturnValue({
      login: jest.fn(),
      isAuthenticated: () => false,
    });
  });

  test('renderiza el formulario de inicio de sesion con campos obligatorios', () => {
    render(
      <MemoryRouter>
        <Login />
      </MemoryRouter>
    );

    expect(screen.getByRole('heading', { name: 'Iniciar Sesión' })).toBeInTheDocument();
    expect(screen.getByPlaceholderText('admin@reservas.com')).toBeInTheDocument();
    expect(screen.getByPlaceholderText('Tu contraseña')).toBeInTheDocument();
    expect(screen.getByRole('button', { name: 'Iniciar Sesión' })).toBeInTheDocument();
  });

  test('envia credenciales cuando se hace submit del formulario', async () => {
    const mockLogin = jest.fn().mockResolvedValue({ success: true });
    useAuth.mockReturnValue({
      login: mockLogin,
      isAuthenticated: () => false,
    });

    render(
      <MemoryRouter>
        <Login />
      </MemoryRouter>
    );

    const emailInput = screen.getByPlaceholderText('admin@reservas.com');
    const passwordInput = screen.getByPlaceholderText('Tu contraseña');
    const submitButton = screen.getByRole('button', { name: 'Iniciar Sesión' });

    await userEvent.type(emailInput, 'user@example.com');
    await userEvent.type(passwordInput, 'password123');
    await userEvent.click(submitButton);

    await waitFor(() => {
      expect(mockLogin).toHaveBeenCalledWith({
        email: 'user@example.com',
        password: 'password123',
      });
    });
  });

  test('muestra mensaje de error cuando credenciales son inválidas', async () => {
    const mockLogin = jest.fn().mockRejectedValue(new Error('Credenciales inválidas'));
    useAuth.mockReturnValue({
      login: mockLogin,
      isAuthenticated: () => false,
    });

    render(
      <MemoryRouter>
        <Login />
      </MemoryRouter>
    );

    const emailInput = screen.getByPlaceholderText('admin@reservas.com');
    const passwordInput = screen.getByPlaceholderText('Tu contraseña');
    const submitButton = screen.getByRole('button', { name: 'Iniciar Sesión' });

    await userEvent.type(emailInput, 'wrong@example.com');
    await userEvent.type(passwordInput, 'wrongpassword');
    await userEvent.click(submitButton);

    await waitFor(() => {
      expect(mockLogin).toHaveBeenCalled();
    });
  });
});
