package metodoDePago;


import org.junit.jupiter.api.Test;


import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;


import java.time.LocalDate;

import org.mockito.Mock;

class MetodoDePagoTest {

    // ===== MOCKS DE LAS APIS =====
    @Mock
    private ApiBilleteraVirtual apiBilleteraMock;
    
    @Mock
    private ApiTarjetaCredito apiTarjetaMock;
    
    @Mock
    private ApiTransferenciaBancaria apiTransferenciaMock;
    
    // ============================================================
    // ===== TESTS BILLETERA VIRTUAL =====
    // ============================================================
    
    @Test
    void testBilleteraVirtual_PagoExitoso() {
        // Given
    	ApiBilleteraVirtual apiBilleteraMock = mock(ApiBilleteraVirtual.class);

        when(apiBilleteraMock.tieneSaldo(100.0)).thenReturn(true);
        doNothing().when(apiBilleteraMock).bloquearSaldo(100.0);
        when(apiBilleteraMock.acreditar(100.0)).thenReturn("TXN-123");
        
        MetodoDePago metodoPago = new BilleteraVirtual(100.0, apiBilleteraMock);
        
        // When
        metodoPago.procesarPago();
        
        // Then
        verify(apiBilleteraMock, times(1)).tieneSaldo(100.0);
        verify(apiBilleteraMock, times(1)).bloquearSaldo(100.0);
        verify(apiBilleteraMock, times(1)).acreditar(100.0);
    }
    
    @Test
    void testBilleteraVirtual_SaldoInsuficiente_LanzaExcepcion() {
        // Given
    	ApiBilleteraVirtual apiBilleteraMock = mock(ApiBilleteraVirtual.class);

        when(apiBilleteraMock.tieneSaldo(100.0)).thenReturn(false);
        
        MetodoDePago metodoPago = new BilleteraVirtual(100.0, apiBilleteraMock);
        
        // When / Then
        RuntimeException exception = assertThrows(RuntimeException.class, 
            () -> metodoPago.procesarPago());
        
        assertEquals("Saldo insuficiente", exception.getMessage());
        verify(apiBilleteraMock, times(1)).tieneSaldo(100.0);
        verify(apiBilleteraMock, never()).bloquearSaldo(anyDouble());
        verify(apiBilleteraMock, never()).acreditar(anyDouble());
    }
    
    // ============================================================
    // ===== TESTS TARJETA DE CRÉDITO =====
    // ============================================================
    
    @Test
    void testTarjetaCredito_PagoExitoso() {
        // Given
    	ApiTarjetaCredito apiTarjetaMock = mock(ApiTarjetaCredito.class);

        LocalDate vencimiento = LocalDate.of(2025, 12, 31);
        when(apiTarjetaMock.validarTarjeta("1234567890", "123", vencimiento)).thenReturn(true);
        doNothing().when(apiTarjetaMock).preAutorizar(100.0);
        when(apiTarjetaMock.transferir(100.0)).thenReturn("TXN-456");
        
        MetodoDePago metodoPago = new TarjetaDeCredito(100.0, "1234567890", "123", vencimiento, apiTarjetaMock);
        
        // When
        metodoPago.procesarPago();
        
        // Then
        verify(apiTarjetaMock, times(1)).validarTarjeta("1234567890", "123", vencimiento);
        verify(apiTarjetaMock, times(1)).preAutorizar(100.0);
        verify(apiTarjetaMock, times(1)).transferir(100.0);
    }
    
    @Test
    void testTarjetaCredito_TarjetaInvalida_LanzaExcepcion() {
        // Given
    	ApiTarjetaCredito apiTarjetaMock = mock(ApiTarjetaCredito.class);
        LocalDate vencimiento = LocalDate.of(2025, 12, 31);
        when(apiTarjetaMock.validarTarjeta("1234567890", "123", vencimiento)).thenReturn(false);
        
        MetodoDePago metodoPago = new TarjetaDeCredito(100.0, "1234567890", "123", vencimiento, apiTarjetaMock);
        
        // When / Then
        RuntimeException exception = assertThrows(RuntimeException.class, 
            () -> metodoPago.procesarPago());
        
        assertEquals("Tarjeta inválida", exception.getMessage());
        verify(apiTarjetaMock, times(1)).validarTarjeta("1234567890", "123", vencimiento);
        verify(apiTarjetaMock, never()).preAutorizar(anyDouble());
        verify(apiTarjetaMock, never()).transferir(anyDouble());
    }
    
    // ============================================================
    // ===== TESTS TRANSFERENCIA BANCARIA =====
    // ============================================================
    
    @Test
    void testTransferenciaBancaria_PagoExitoso() {
        // Given
    	ApiTransferenciaBancaria apiTransferenciaMock = mock(ApiTransferenciaBancaria.class);
        when(apiTransferenciaMock.validarCuenta("CBU123", "alias123")).thenReturn(true);
        when(apiTransferenciaMock.transferir(150.0)).thenReturn("TXN-789");
        
        MetodoDePago metodoPago = new TransferenciaBancaria(150.0, "CBU123", "alias123", apiTransferenciaMock);
        
        // When
        metodoPago.procesarPago();
        
        // Then
        verify(apiTransferenciaMock, times(1)).validarCuenta("CBU123", "alias123");
        verify(apiTransferenciaMock, times(1)).transferir(150.0);
    }
    
    @Test
    void testTransferenciaBancaria_CuentaInvalida_LanzaExcepcion() {
        // Given
    	ApiTransferenciaBancaria apiTransferenciaMock = mock(ApiTransferenciaBancaria.class);
        when(apiTransferenciaMock.validarCuenta("CBU123", "alias123")).thenReturn(false);
        
        MetodoDePago metodoPago = new TransferenciaBancaria(150.0, "CBU123", "alias123", apiTransferenciaMock);
        
        // When / Then
        RuntimeException exception = assertThrows(RuntimeException.class, 
            () -> metodoPago.procesarPago());
        
        assertEquals("Cuenta inválida", exception.getMessage());
        verify(apiTransferenciaMock, times(1)).validarCuenta("CBU123", "alias123");
        verify(apiTransferenciaMock, never()).transferir(anyDouble());
    }
}

