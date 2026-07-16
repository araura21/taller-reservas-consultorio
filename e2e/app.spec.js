const { test, expect } = require('@playwright/test');

test.describe('Sistema de Reservas - E2E base', () => {
  test('muestra la pagina de login con el formulario principal', async ({ page }) => {
    await page.goto('/');

    await expect(page.getByRole('heading', { name: 'Iniciar Sesión' })).toBeVisible();
    await expect(page.getByPlaceholder('admin@reservas.com')).toBeVisible();
    await expect(page.getByPlaceholder('Tu contraseña')).toBeVisible();
    await expect(page.getByRole('button', { name: 'Iniciar Sesión' })).toBeVisible();
  });

  test('permite acceder al formulario publico de reservas', async ({ page }) => {
    await page.goto('/reservar');

    await expect(page.getByRole('heading', { name: 'Nueva Reserva' })).toBeVisible();
  });
});
