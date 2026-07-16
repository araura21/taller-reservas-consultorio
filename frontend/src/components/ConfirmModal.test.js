import React from 'react';
import { render, screen, fireEvent } from '@testing-library/react';
import ConfirmModal from './ConfirmModal';

describe('ConfirmModal', () => {
  test('ejecuta onConfirm al hacer clic en el boton de confirmacion', () => {
    const onConfirm = jest.fn();
    const onClose = jest.fn();

    render(
      <ConfirmModal
        isOpen={true}
        onClose={onClose}
        onConfirm={onConfirm}
        title="Confirmar accion"
        message="Desea continuar?"
        confirmText="Si, continuar"
      />
    );

    fireEvent.click(screen.getByRole('button', { name: 'Si, continuar' }));

    expect(onConfirm).toHaveBeenCalledTimes(1);
  });
});
