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
import { authApi } from '../services/api';

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

  test('logout envia solicitud al endpoint /auth/logout', async () => {
    const mockResponse = {
      data: {
        message: 'Sesión cerrada exitosamente',
      },
    };
    mockAxios.post.mockResolvedValue(mockResponse);

    const response = await authApi.logout();

    expect(mockAxios.post).toHaveBeenCalledWith('/auth/logout');
    expect(response.data.message).toBe('Sesión cerrada exitosamente');
  });

  test('obtenerServicios retorna lista de servicios disponibles', async () => {
    const mockResponse = {
      data: [
        {
          id: 1,
          nombreServicio: 'Consulta General',
          precio: 25,
          duracion: 30,
        },
        {
          id: 2,
          nombreServicio: 'Consulta Especializada',
          precio: 50,
          duracion: 45,
        },
      ],
    };
    mockAxios.get.mockResolvedValue(mockResponse);

    const response = await authApi.obtenerServicios();

    expect(mockAxios.get).toHaveBeenCalledWith('/servicios');
    expect(response.data.length).toBe(2);
    expect(response.data[0].nombreServicio).toBe('Consulta General');
  });
});
