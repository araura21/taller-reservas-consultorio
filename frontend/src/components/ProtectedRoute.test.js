import React from 'react';
import { render, screen } from '@testing-library/react';
import { MemoryRouter } from 'react-router-dom';
import ProtectedRoute from './ProtectedRoute';
import { useAuth } from '../context/AuthContext';

jest.mock('../context/AuthContext', () => ({
  useAuth: jest.fn(),
}));

describe('ProtectedRoute', () => {
  test('no renderiza contenido cuando el usuario no esta autenticado', () => {
    useAuth.mockReturnValue({
      isAuthenticated: () => false,
      isAdmin: () => false,
      loading: false,
    });

    render(
      <MemoryRouter>
        <ProtectedRoute>
          <div>Contenido protegido</div>
        </ProtectedRoute>
      </MemoryRouter>
    );

    expect(screen.queryByText('Contenido protegido')).not.toBeInTheDocument();
  });
});
