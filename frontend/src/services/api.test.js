jest.mock('axios', () => {
  const mockInstance = {
    get: jest.fn(),
    post: jest.fn(),
    put: jest.fn(),
    delete: jest.fn(),
    interceptors: {
      request: { use: jest.fn() },
    },
  };

  return {
    __esModule: true,
    default: {
      create: jest.fn(() => mockInstance),
    },
    create: jest.fn(() => mockInstance),
  };
});

import axios from 'axios';
import { authApi } from './api';

const mockAxios = axios.create();

describe('authApi', () => {
  beforeEach(() => {
    jest.clearAllMocks();
  });

  test('login envia credenciales al endpoint /auth/login', async () => {
    const credentials = { email: 'admin@reservas.com', password: 'password' };
    const mockResponse = {
      data: {
        token: 'admin-token-1',
        nombre: 'Admin',
        email: credentials.email,
        rol: 'Administrador',
      },
    };
    mockAxios.post.mockResolvedValue(mockResponse);

    const response = await authApi.login(credentials);

    expect(mockAxios.post).toHaveBeenCalledWith('/auth/login', credentials);
    expect(response.data.token).toBe('admin-token-1');
  });
});
