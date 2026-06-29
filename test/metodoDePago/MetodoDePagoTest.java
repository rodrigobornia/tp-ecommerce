package metodoDePago;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.HashMap;
import java.util.Map;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class MetodoDePagoTest {
    
    // Creo los mocks
    private TarjetaDeCredito.APITarjetaCredito apiTCMock;
    private TransferenciaBancaria.APITransferenciaBancaria apiTBMock;
    private BilleteraVirtual.APIBilleteraVirtual apiBVMock;
    
    @BeforeEach
    void setUp() {
        // ✅ Crear mocks manualmente
        apiTCMock = mock(TarjetaDeCredito.APITarjetaCredito.class);
        apiTBMock = mock(TransferenciaBancaria.APITransferenciaBancaria.class);
        apiBVMock = mock(BilleteraVirtual.APIBilleteraVirtual.class);
    }
    
    // ============================================================
    // ===== TESTS TARJETA CRÉDITO =====
    // ============================================================
    
    @Test
    void testTarjetaCredito_PagoExitoso() throws Exception {
        when(apiTCMock.validarTarjeta(anyString(), anyString(), anyString()))
            .thenReturn(true);
        when(apiTCMock.preAutorizar(anyDouble(), anyString()))
            .thenReturn("AUT-123");
        when(apiTCMock.ejecutarPago(anyDouble(), anyString()))
            .thenReturn("TXN-456");
        
        Map<String, Object> datos = new HashMap<>();
        datos.put("numero", "1234567890");
        datos.put("cvv", "123");
        datos.put("vencimiento", "12/25");
        
        MetodoDePago procesador = new TarjetaDeCredito(apiTCMock);
        String resultado = procesador.procesarPago(100.0, datos);
        
        assertEquals("EXITO:TXN-456", resultado);
        verify(apiTCMock, times(1)).validarTarjeta("1234567890", "123", "12/25");
        verify(apiTCMock, times(1)).preAutorizar(100.0, "1234567890");
        verify(apiTCMock, times(1)).ejecutarPago(100.0, "1234567890");
    }
    
    @Test
    void testTarjetaCredito_TarjetaInvalida() throws Exception {
        when(apiTCMock.validarTarjeta(anyString(), anyString(), anyString()))
            .thenReturn(false);
        
        Map<String, Object> datos = new HashMap<>();
        datos.put("numero", "1234567890");
        datos.put("cvv", "123");
        datos.put("vencimiento", "12/25");
        
        MetodoDePago procesador = new TarjetaDeCredito(apiTCMock);
        String resultado = procesador.procesarPago(100.0, datos);
        
        assertEquals("ERROR:Tarjeta de crédito inválida", resultado);
        verify(apiTCMock, times(1)).validarTarjeta(anyString(), anyString(), anyString());
        verify(apiTCMock, never()).preAutorizar(anyDouble(), anyString());
        verify(apiTCMock, never()).ejecutarPago(anyDouble(), anyString());
    }
    
    @Test
    void testTarjetaCredito_ErrorPreAutorizacion() throws Exception {
        when(apiTCMock.validarTarjeta(anyString(), anyString(), anyString()))
            .thenReturn(true);
        when(apiTCMock.preAutorizar(anyDouble(), anyString()))
            .thenReturn(null);
        
        Map<String, Object> datos = new HashMap<>();
        datos.put("numero", "1234567890");
        datos.put("cvv", "123");
        datos.put("vencimiento", "12/25");
        
        MetodoDePago procesador = new TarjetaDeCredito(apiTCMock);
        String resultado = procesador.procesarPago(100.0, datos);
        
        assertEquals("ERROR:No se pudo pre-autorizar la tarjeta", resultado);
        verify(apiTCMock, times(1)).validarTarjeta(anyString(), anyString(), anyString());
        verify(apiTCMock, times(1)).preAutorizar(anyDouble(), anyString());
        verify(apiTCMock, never()).ejecutarPago(anyDouble(), anyString());
    }
    
    @Test
    void testTarjetaCredito_ErrorEjecucion() throws Exception {
        when(apiTCMock.validarTarjeta(anyString(), anyString(), anyString()))
            .thenReturn(true);
        when(apiTCMock.preAutorizar(anyDouble(), anyString()))
            .thenReturn("AUT-123");
        when(apiTCMock.ejecutarPago(anyDouble(), anyString()))
            .thenReturn(null);
        
        Map<String, Object> datos = new HashMap<>();
        datos.put("numero", "1234567890");
        datos.put("cvv", "123");
        datos.put("vencimiento", "12/25");
        
        MetodoDePago procesador = new TarjetaDeCredito(apiTCMock);
        String resultado = procesador.procesarPago(100.0, datos);
        
        assertEquals("ERROR:Error al ejecutar el pago con tarjeta", resultado);
        verify(apiTCMock, times(1)).validarTarjeta(anyString(), anyString(), anyString());
        verify(apiTCMock, times(1)).preAutorizar(anyDouble(), anyString());
        verify(apiTCMock, times(1)).ejecutarPago(anyDouble(), anyString());
    }
    
    @Test
    void testTarjetaCredito_VerificarOrden() throws Exception {
        when(apiTCMock.validarTarjeta(anyString(), anyString(), anyString()))
            .thenReturn(true);
        when(apiTCMock.preAutorizar(anyDouble(), anyString()))
            .thenReturn("AUT-123");
        when(apiTCMock.ejecutarPago(anyDouble(), anyString()))
            .thenReturn("TXN-456");
        
        Map<String, Object> datos = new HashMap<>();
        datos.put("numero", "1234567890");
        datos.put("cvv", "123");
        datos.put("vencimiento", "12/25");
        
        MetodoDePago procesador = new TarjetaDeCredito(apiTCMock);
        procesador.procesarPago(100.0, datos);
        
        var inOrder = inOrder(apiTCMock);
        inOrder.verify(apiTCMock).validarTarjeta(anyString(), anyString(), anyString());
        inOrder.verify(apiTCMock).preAutorizar(anyDouble(), anyString());
        inOrder.verify(apiTCMock).ejecutarPago(anyDouble(), anyString());
    }
    
    // ============================================================
    // ===== TRANSFERENCIA BANCARIA TESTS =====
    // ============================================================
    
    @Test
    void testTransferenciaBancaria_PagoExitoso() throws Exception {
        when(apiTBMock.validarCBU(anyString(), anyString()))
            .thenReturn(true);
        when(apiTBMock.ejecutarTransferencia(anyDouble(), anyString(), anyString()))
            .thenReturn("TRX-789");
        
        Map<String, Object> datos = new HashMap<>();
        datos.put("cbuOrigen", "CBU123");
        datos.put("alias", "alias123");
        datos.put("cbuDestino", "CBU456");
        
        MetodoDePago procesador = new TransferenciaBancaria(apiTBMock);
        String resultado = procesador.procesarPago(150.0, datos);
        
        assertEquals("EXITO:TRX-789", resultado);
        verify(apiTBMock, times(1)).validarCBU("CBU123", "alias123");
        verify(apiTBMock, times(1)).ejecutarTransferencia(150.0, "CBU123", "CBU456");
    }
    
    @Test
    void testTransferenciaBancaria_CBUInvalido() throws Exception {
        when(apiTBMock.validarCBU(anyString(), anyString()))
            .thenReturn(false);
        
        Map<String, Object> datos = new HashMap<>();
        datos.put("cbuOrigen", "CBU123");
        datos.put("alias", "alias123");
        datos.put("cbuDestino", "CBU456");
        
        MetodoDePago procesador = new TransferenciaBancaria(apiTBMock);
        String resultado = procesador.procesarPago(150.0, datos);
        
        assertEquals("ERROR:CBU o alias inválido", resultado);
        verify(apiTBMock, times(1)).validarCBU(anyString(), anyString());
        verify(apiTBMock, never()).ejecutarTransferencia(anyDouble(), anyString(), anyString());
    }
    
    @Test
    void testTransferenciaBancaria_ErrorEjecucion() throws Exception {
        when(apiTBMock.validarCBU(anyString(), anyString()))
            .thenReturn(true);
        when(apiTBMock.ejecutarTransferencia(anyDouble(), anyString(), anyString()))
            .thenReturn(null);
        
        Map<String, Object> datos = new HashMap<>();
        datos.put("cbuOrigen", "CBU123");
        datos.put("alias", "alias123");
        datos.put("cbuDestino", "CBU456");
        
        MetodoDePago procesador = new TransferenciaBancaria(apiTBMock);
        String resultado = procesador.procesarPago(150.0, datos);
        
        assertEquals("ERROR:Error al ejecutar la transferencia", resultado);
        verify(apiTBMock, times(1)).validarCBU(anyString(), anyString());
        verify(apiTBMock, times(1)).ejecutarTransferencia(anyDouble(), anyString(), anyString());
    }
    
    // ============================================================
    // ===== BILLETERA VIRTUAL TESTS =====
    // ============================================================
    
    @Test
    void testBilleteraVirtual_PagoExitoso() throws Exception {
        when(apiBVMock.verificarSaldo(anyString(), anyDouble()))
            .thenReturn(true);
        when(apiBVMock.bloquearSaldo(anyString(), anyDouble()))
            .thenReturn("BLQ-001");
        when(apiBVMock.acreditarFondos(anyString(), anyDouble()))
            .thenReturn("TXN-999");
        
        Map<String, Object> datos = new HashMap<>();
        datos.put("idUsuario", "USR-123");
        datos.put("idVendedor", "VEND-456");
        datos.put("email", "juan@email.com");
        datos.put("monto", 75.0);
        
        MetodoDePago procesador = new BilleteraVirtual(apiBVMock);
        String resultado = procesador.procesarPago(75.0, datos);
        
        assertEquals("EXITO:TXN-999", resultado);
        verify(apiBVMock, times(1)).verificarSaldo("USR-123", 75.0);
        verify(apiBVMock, times(1)).bloquearSaldo("USR-123", 75.0);
        verify(apiBVMock, times(1)).acreditarFondos("VEND-456", 75.0);
        verify(apiBVMock, times(1)).enviarNotificacionPush(anyString());
    }
    
    @Test
    void testBilleteraVirtual_SaldoInsuficiente() throws Exception {
        when(apiBVMock.verificarSaldo(anyString(), anyDouble()))
            .thenReturn(false);
        
        Map<String, Object> datos = new HashMap<>();
        datos.put("idUsuario", "USR-123");
        datos.put("idVendedor", "VEND-456");
        datos.put("email", "juan@email.com");
        datos.put("monto", 70.0);
        
        MetodoDePago procesador = new BilleteraVirtual(apiBVMock);
        String resultado = procesador.procesarPago(75.0, datos);
        
        assertEquals("ERROR:Saldo insuficiente en la billetera virtual", resultado);
        verify(apiBVMock, times(1)).verificarSaldo(anyString(), anyDouble());
        verify(apiBVMock, never()).bloquearSaldo(anyString(), anyDouble());
        verify(apiBVMock, never()).acreditarFondos(anyString(), anyDouble());
    }
    
    @Test
    void testBilleteraVirtual_ErrorBloqueo() throws Exception {
        when(apiBVMock.verificarSaldo(anyString(), anyDouble()))
            .thenReturn(true);
        when(apiBVMock.bloquearSaldo(anyString(), anyDouble()))
            .thenReturn(null);
        
        Map<String, Object> datos = new HashMap<>();
        datos.put("idUsuario", "USR-123");
        datos.put("idVendedor", "VEND-456");
        datos.put("email", "juan@email.com");
        datos.put("monto", 70.0);
        
        MetodoDePago procesador = new BilleteraVirtual(apiBVMock);
        String resultado = procesador.procesarPago(75.0, datos);
        
        assertEquals("ERROR:No se pudo bloquear el saldo", resultado);
        verify(apiBVMock, times(1)).verificarSaldo(anyString(), anyDouble());
        verify(apiBVMock, times(1)).bloquearSaldo(anyString(), anyDouble());
        verify(apiBVMock, never()).acreditarFondos(anyString(), anyDouble());
    }
    
    @Test
    void testBilleteraVirtual_ErrorAcreditacion() throws Exception {
        when(apiBVMock.verificarSaldo(anyString(), anyDouble()))
            .thenReturn(true);
        when(apiBVMock.bloquearSaldo(anyString(), anyDouble()))
            .thenReturn("BLQ-001");
        when(apiBVMock.acreditarFondos(anyString(), anyDouble()))
            .thenReturn(null);
        
        Map<String, Object> datos = new HashMap<>();
        datos.put("idUsuario", "USR-123");
        datos.put("idVendedor", "VEND-456");
        datos.put("email", "juan@email.com");
        datos.put("monto", 75.0);
        
        MetodoDePago procesador = new BilleteraVirtual(apiBVMock);
        String resultado = procesador.procesarPago(75.0, datos);
        
        assertEquals("ERROR:Error al acreditar fondos al vendedor", resultado);
        verify(apiBVMock, times(1)).verificarSaldo(anyString(), anyDouble());
        verify(apiBVMock, times(1)).bloquearSaldo(anyString(), anyDouble());
        verify(apiBVMock, times(1)).acreditarFondos(anyString(), anyDouble());
    }
    
    @Test
    void testBilleteraVirtual_VerificarOrden() throws Exception {
        
        when(apiBVMock.verificarSaldo(anyString(), anyDouble()))
            .thenReturn(true);
        when(apiBVMock.bloquearSaldo(anyString(), anyDouble()))
            .thenReturn("BLQ-001");
        when(apiBVMock.acreditarFondos(anyString(), anyDouble()))
            .thenReturn("TXN-999");
        
        Map<String, Object> datos = new HashMap<>();
        datos.put("idUsuario", "USR-123");
        datos.put("idVendedor", "VEND-456");
        datos.put("email", "juan@email.com");
        datos.put("monto", 75.0);
        
        MetodoDePago procesador = new BilleteraVirtual(apiBVMock);
        procesador.procesarPago(75.0, datos);
        
        var inOrder = inOrder(apiBVMock);
        inOrder.verify(apiBVMock).verificarSaldo(anyString(), anyDouble());
        inOrder.verify(apiBVMock).bloquearSaldo(anyString(), anyDouble());
        inOrder.verify(apiBVMock).acreditarFondos(anyString(), anyDouble());
        inOrder.verify(apiBVMock).enviarNotificacionPush(anyString());
    }
}

