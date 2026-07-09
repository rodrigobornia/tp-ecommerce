package pedido;

import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import producto.Producto;
import envio.*;
import metodoDePago.MetodoDePago;

public class PedidoTest {
    
    @Mock
    private Producto productoMock;
    @Mock
    private NotificadorEmail.MailSender mailSenderMock;
    @Mock
    private ObservadorPedido observerMock;
    @Mock
    private GeneradorFactura generadorFacturaMock;
    @Mock
    private Fidelizacion fidelizacionMock;
    @Mock
    private CorreoArgentino correoArgentino;
    @Mock
    private MetodoDePago metodopagoMock;

    private Pedido pedido;
    private NotificadorEmail notificadorEmail;
    
    @BeforeEach
    void setUp() {
    	MockitoAnnotations.openMocks(this);
    	
    	MetodoDeEnvio envioEstandar = new EnvioEstandar(correoArgentino);
        notificadorEmail = new NotificadorEmail(mailSenderMock);
        
        List<Producto> productos = new ArrayList<>();
        productos.add(productoMock);
        
        pedido = new Pedido("Calle 123", productos, envioEstandar, metodopagoMock);
    }
    
    // ============ TEST BORRADOR ============
    
    @Test
    void testBorrador_AgregarItem_DeberiaAgregarProducto() {
        pedido.agregarItem(productoMock);
        assertTrue(pedido.productos.contains(productoMock));
        assertEquals(2, pedido.productos.size());
    }
    
    @Test
    void testBorrador_QuitarItem_DeberiaQuitarProducto() {
        pedido.agregarItem(productoMock);
        pedido.quitarItem(productoMock);
        assertEquals(1, pedido.productos.size());
    }
    
    @Test
    void testBorrador_Confirmar_CambiaAConfirmado() {
        pedido.confirmar();
        assertEquals(Confirmado.class, pedido.getEstado().getClass());
    }
    
    @Test
    void testBorrador_Cancelar_CambiaACancelado() {
        pedido.cancelar();
        assertEquals(Cancelado.class, pedido.getEstado().getClass());
    }
    
    @Test
    void testBorrador_NoPuedePreparar_LanzaExcepcion() {
        assertThrows(RuntimeException.class, () -> pedido.preparar());
    }
    
    @Test
    void testBorrador_NoPuedeEnviar_LanzaExcepcion() {
        assertThrows(RuntimeException.class, () -> pedido.enviar());
    }
    
    @Test
    void testBorrador_NoPuedeEntregar_LanzaExcepcion() {
        assertThrows(RuntimeException.class, () -> pedido.entregar());
    }
    
    // ============ TEST CONFIRMADO ============
    
    @Test
    void testConfirmado_Preparar_DecrementaStockYCambiaEstado() {
        Pedido pedidoSpy = spy(pedido);
        pedidoSpy.setEstado(new Confirmado());
        
        pedidoSpy.preparar();
        
        verify(pedidoSpy, times(1)).decrementarStock();
        verify(pedidoSpy, never()).incrementarStock();
        assertEquals(Preparacion.class, pedidoSpy.getEstado().getClass());
    }
    
    @Test
    void testConfirmado_Cancelar_NOIncrementaStock_PorqueNuncaSeDecremento() {
        Pedido pedidoSpy = spy(pedido);
        pedidoSpy.setEstado(new Confirmado());
        
        pedidoSpy.cancelar();
        
        verify(pedidoSpy, never()).incrementarStock();
        verify(pedidoSpy, never()).reembolsarCostoProductos();
        verify(pedidoSpy, never()).reembolsarCostoEnvio();
        assertEquals(Cancelado.class, pedidoSpy.getEstado().getClass());
    }
    
    @Test
    void testConfirmado_NoPuedeAgregarItem_LanzaExcepcion() {
        pedido.setEstado(new Confirmado());
        assertThrows(RuntimeException.class, 
            () -> pedido.agregarItem(productoMock));
    }
    
    @Test
    void testConfirmado_NoPuedeQuitarItem_LanzaExcepcion() {
        pedido.setEstado(new Confirmado());
        assertThrows(RuntimeException.class, 
            () -> pedido.quitarItem(productoMock));
    }
    
    @Test
    void testConfirmado_NoPuedeConfirmar_LanzaExcepcion() {
        pedido.setEstado(new Confirmado());
        assertThrows(RuntimeException.class, () -> pedido.confirmar());
    }
    
    @Test
    void testConfirmado_NoPuedeEnviar_LanzaExcepcion() {
        pedido.setEstado(new Confirmado());
        assertThrows(RuntimeException.class, () -> pedido.enviar());
    }
    
    @Test
    void testConfirmado_NoPuedeEntregar_LanzaExcepcion() {
        pedido.setEstado(new Confirmado());
        assertThrows(RuntimeException.class, () -> pedido.entregar());
    }
    
    // ============ TEST PREPARACION ============
    
    @Test
    void testPreparacion_Enviar_CambiaAEnviado() {
        pedido.setEstado(new Preparacion());
        pedido.enviar();
        assertEquals(Enviado.class, pedido.getEstado().getClass());
    }
    
    @Test
    void testPreparacion_Cancelar_IncrementaStockYReembolsaTodo() {
        Pedido pedidoSpy = spy(pedido);
        pedidoSpy.setEstado(new Preparacion());
        
        pedidoSpy.cancelar();
        
        verify(pedidoSpy, times(1)).incrementarStock();
        verify(pedidoSpy, times(1)).reembolsarCostoProductos();
        verify(pedidoSpy, times(1)).reembolsarCostoEnvio();
        assertEquals(Cancelado.class, pedidoSpy.getEstado().getClass());
    }
    
    @Test
    void testPreparacion_NoPuedeAgregarItem_LanzaExcepcion() {
        pedido.setEstado(new Preparacion());
        assertThrows(RuntimeException.class, 
            () -> pedido.agregarItem(productoMock));
    }
    
    @Test
    void testPreparacion_NoPuedeQuitarItem_LanzaExcepcion() {
        pedido.setEstado(new Preparacion());
        assertThrows(RuntimeException.class, 
            () -> pedido.quitarItem(productoMock));
    }
    
    @Test
    void testPreparacion_NoPuedeConfirmar_LanzaExcepcion() {
        pedido.setEstado(new Preparacion());
        assertThrows(RuntimeException.class, () -> pedido.confirmar());
    }
    
    @Test
    void testPreparacion_NoPuedeEntregar_LanzaExcepcion() {
        pedido.setEstado(new Preparacion());
        assertThrows(RuntimeException.class, () -> pedido.entregar());
    }
    
    // ============ TEST ENVIADO ============
    
    @Test
    void testEnviado_Entregar_CambiaAEntregado() {
        pedido.setEstado(new Enviado());
        pedido.entregar();
        assertEquals(Entregado.class, pedido.getEstado().getClass());
    }
    
    @Test
    void testEnviado_Cancelar_IncrementaStockYReembolsaSoloProductos() {
    	// Given
        Pedido pedidoSpy = spy(pedido);
        
        // When
        pedidoSpy.setEstado(new Enviado());
        pedidoSpy.cancelar();
        
        // Then
        verify(pedidoSpy, times(1)).incrementarStock();
        verify(pedidoSpy, times(1)).reembolsarCostoProductos();
        verify(pedidoSpy, never()).reembolsarCostoEnvio();
        assertTrue(pedidoSpy.getEstado().estaCancelado());
    }
    
    @Test
    void testEnviado_NoPuedeAgregarItem_LanzaExcepcion() {
        pedido.setEstado(new Enviado());
        assertThrows(RuntimeException.class, 
            () -> pedido.agregarItem(productoMock));
    }
    
    @Test
    void testEnviado_NoPuedePreparar_LanzaExcepcion() {
        pedido.setEstado(new Enviado());
        assertThrows(RuntimeException.class, () -> pedido.preparar());
    }
    
    @Test
    void testEnviado_NoPuedeConfirmar_LanzaExcepcion() {
        pedido.setEstado(new Enviado());
        assertThrows(RuntimeException.class, () -> pedido.confirmar());
    }
    
    // ============ TEST ENTREGADO ============
    
    @Test
    void testEntregado_NoPuedeHacerNada_LanzaExcepcion() {
        pedido.setEstado(new Entregado());
        
        assertThrows(RuntimeException.class, () -> pedido.agregarItem(productoMock));
        assertThrows(RuntimeException.class, () -> pedido.quitarItem(productoMock));
        assertThrows(RuntimeException.class, () -> pedido.confirmar());
        assertThrows(RuntimeException.class, () -> pedido.preparar());
        assertThrows(RuntimeException.class, () -> pedido.enviar());
        assertThrows(RuntimeException.class, () -> pedido.cancelar());
    }
    
    // ============ TEST CANCELADO ============
    
    @Test
    void testCancelado_NoPuedeHacerNada_LanzaExcepcion() {
        pedido.setEstado(new Cancelado());
        
        assertThrows(RuntimeException.class, () -> pedido.agregarItem(productoMock));
        assertThrows(RuntimeException.class, () -> pedido.quitarItem(productoMock));
        assertThrows(RuntimeException.class, () -> pedido.confirmar());
        assertThrows(RuntimeException.class, () -> pedido.preparar());
        assertThrows(RuntimeException.class, () -> pedido.enviar());
        assertThrows(RuntimeException.class, () -> pedido.entregar());
    }
    
    // ============ TEST FLUJO COMPLETO ============
    
    @Test
    void testFlujoCompleto_BorradorAEntregado_Exitoso() {
        Pedido pedidoSpy = spy(pedido);
        
        // 1. Borrador - Agregar items
        pedidoSpy.agregarItem(productoMock);
        verify(pedidoSpy, times(1)).agregarItem(productoMock);
        assertTrue(pedidoSpy.productos.contains(productoMock));
        
        // 2. Borrador -> Confirmado
        pedidoSpy.confirmar();
        assertEquals(Confirmado.class, pedidoSpy.getEstado().getClass());
        
        // 3. Confirmado -> Preparacion
        pedidoSpy.preparar();
        verify(pedidoSpy, times(1)).decrementarStock();
        assertEquals(Preparacion.class, pedidoSpy.getEstado().getClass());
        
        // 4. Preparacion -> Enviado
        pedidoSpy.enviar();
        assertEquals(Enviado.class, pedidoSpy.getEstado().getClass());
        
        // 5. Enviado -> Entregado
        pedidoSpy.entregar();
        assertEquals(Entregado.class, pedidoSpy.getEstado().getClass());
    }
    
    @Test
    void testFlujoConCancelacion_DesdeBorrador_NoReembolsaNiStock() {
        Pedido pedidoSpy = spy(pedido);
        
        // Borrador -> Cancelado
        pedidoSpy.cancelar();
        
        verify(pedidoSpy, never()).incrementarStock();
        verify(pedidoSpy, never()).reembolsarCostoProductos();
        verify(pedidoSpy, never()).reembolsarCostoEnvio();
        assertTrue(pedidoSpy.getEstado().estaCancelado());
        
    }
    
    @Test
    void testFlujoConCancelacion_DesdeConfirmado_NoReembolsaNiStock() {
        Pedido pedidoSpy = spy(pedido);
        pedidoSpy.setEstado(new Confirmado());
        
        pedidoSpy.cancelar();
        
        verify(pedidoSpy, never()).incrementarStock();
        verify(pedidoSpy, never()).reembolsarCostoProductos();
        verify(pedidoSpy, never()).reembolsarCostoEnvio();
        assertEquals(Cancelado.class, pedidoSpy.getEstado().getClass());
    }
    
    @Test
    void testFlujoConCancelacion_DesdePreparacion_ReembolsaTodoEIncrementaStock() {
        Pedido pedidoSpy = spy(pedido);
        pedidoSpy.setEstado(new Preparacion());
        
        pedidoSpy.cancelar();
        
        verify(pedidoSpy, times(1)).incrementarStock();
        verify(pedidoSpy, times(1)).reembolsarCostoProductos();
        verify(pedidoSpy, times(1)).reembolsarCostoEnvio();
        assertEquals(Cancelado.class, pedidoSpy.getEstado().getClass());
    }
    
    @Test
    void testFlujoConCancelacion_DesdeEnviado_ReembolsaSoloProductosEIncrementaStock() {
        Pedido pedidoSpy = spy(pedido);
        pedidoSpy.setEstado(new Enviado());
        
        pedidoSpy.cancelar();
        
        verify(pedidoSpy, times(1)).incrementarStock();
        verify(pedidoSpy, times(1)).reembolsarCostoProductos();
        verify(pedidoSpy, never()).reembolsarCostoEnvio();
        assertEquals(Cancelado.class, pedidoSpy.getEstado().getClass());
    }
    
    // ============ TEST OPERACIONES INVÁLIDAS ADICIONALES ============
    
    @Test
    void testBorrador_NoPuedeCancelarDosVeces() {
        pedido.cancelar();
        assertEquals(Cancelado.class, pedido.getEstado().getClass());
        
        // Intentar cancelar nuevamente desde Cancelado
        assertThrows(RuntimeException.class, () -> pedido.cancelar());
    }
    
    @Test
    void testConfirmado_NoPuedePrepararDosVeces() {
        pedido.setEstado(new Confirmado());
        pedido.preparar();
        assertEquals(Preparacion.class, pedido.getEstado().getClass());
        
        // Intentar preparar nuevamente desde Preparacion (no debería poder)
        assertThrows(RuntimeException.class, () -> pedido.preparar());
    }
    
    // -------------------- TESTS DE SUSCRIPCIÓN --------------------
    
    @Test
    void testAgregarObserver_DeberiaAgregarALaLista() {
        pedido.agregarObserver(generadorFacturaMock);
        pedido.confirmar();
        verify(generadorFacturaMock, times(1)).notificar(any(), any(), any());
    }
    
    @Test
    void testQuitarObserver_DeberiaQuitarDeLaLista() {
        pedido.agregarObserver(generadorFacturaMock);
        pedido.quitarObserver(generadorFacturaMock);
        pedido.confirmar();
        verify(generadorFacturaMock, never()).notificar(any(), any(), any());
    }
    
    // -------------------- TESTS DE NOTIFICADOR EMAIL --------------------
    
    @Test
    void testNotificadorEmail_DeberiaEnviarMailEnEstadosPermitidos() {
        pedido.agregarObserver(notificadorEmail);
        
        // Confirmado y Enviado deben enviar mail
        pedido.confirmar();
        pedido.preparar();
        pedido.enviar();
        
        verify(mailSenderMock, times(2))
            .enviarMail(anyString(), anyString(), anyString(), anyString());
    }
    
    @Test
    void testNotificadorEmail_NoDeberiaEnviarMailEnEstadosNoPermitidos() {
        pedido.agregarObserver(notificadorEmail);
        
        pedido.cancelar(); // Cancelado no envía mail
        
        verify(mailSenderMock, never())
            .enviarMail(anyString(), anyString(), anyString(), anyString());
    }
    
    // -------------------- TESTS DE GENERADOR FACTURA --------------------
    
    @Test
    void testGeneradorFactura_DeberiaActuarSoloEnEntregado() {
        pedido.agregarObserver(generadorFacturaMock);
        
        // Confirmado - NO debe generar factura
        pedido.confirmar();
        verify(generadorFacturaMock, times(1))
            .notificar(any(), any(), any());
        
        pedido.preparar();
        pedido.enviar();
        // Entregado - SI debe generar factura
        pedido.entregar();
        verify(generadorFacturaMock, times(4)) //Se notifica 4 veces pero solo genera en entregado
            .notificar(any(), any(), any());
        
    }
    
    // -------------------- TESTS DE FIDELIZACION --------------------
    
    @Test
    void testFidelizacion_DeberiaActuarSoloEnCancelado() {
        pedido.agregarObserver(fidelizacionMock);
        
        // Confirmado - NO debe enviar cupón
        pedido.confirmar();
        verify(fidelizacionMock, times(1))
            .notificar(any(), any(), any());
        
        // Cancelado - SI debe enviar cupón
        pedido.cancelar();
        verify(fidelizacionMock, times(2)) // le llega dos veces la notificacion pero solo envia el mensaje en cancelar
            .notificar(any(), any(), any());
    }
    
    // -------------------- TESTS DE NOTIFICACIÓN MÚLTIPLE --------------------
    
    @Test
    void testMultiplesObservadores_CuandoConfirmado_SoloEmail() {
        pedido.agregarObserver(notificadorEmail);
        pedido.agregarObserver(generadorFacturaMock);
        pedido.agregarObserver(fidelizacionMock);
        
        pedido.confirmar();
        
        verify(mailSenderMock, times(1)).enviarMail(anyString(), anyString(), anyString(), anyString());
        verify(generadorFacturaMock, times(1)).notificar(any(), any(), any());
        verify(fidelizacionMock, times(1)).notificar(any(), any(), any());
    }
    
    @Test
    void testMultiplesObservadores_CuandoEntregado_EmailYFactura() {
        pedido.agregarObserver(notificadorEmail);
        pedido.agregarObserver(generadorFacturaMock);
        pedido.agregarObserver(fidelizacionMock);
        
        pedido.confirmar();
        pedido.preparar();
        pedido.enviar();
        pedido.entregar();
        
        verify(mailSenderMock, times(3)).enviarMail(anyString(), anyString(), anyString(), anyString());
        verify(generadorFacturaMock, times(4)).notificar(any(), any(), any());
        verify(fidelizacionMock, times(4)).notificar(any(), any(), any());
    }
    
    @Test
    void testMultiplesObservadores_CuandoCancelado_SoloFidelizacion() {
        pedido.agregarObserver(notificadorEmail);
        pedido.agregarObserver(generadorFacturaMock);
        pedido.agregarObserver(fidelizacionMock);
        
        pedido.cancelar();
        
        verify(mailSenderMock, never()).enviarMail(anyString(), anyString(), anyString(), anyString());
        verify(generadorFacturaMock, times(1)).notificar(any(), any(), any());
        verify(fidelizacionMock, times(1)).notificar(any(), any(), any());
    }
    
    // -------------------- TESTS DE FLUJO COMPLETO --------------------
    
    @Test
    void testFlujoCompleto_ConfirmadoEnviadoEntregado_ConNotificaciones() {
        pedido.agregarObserver(notificadorEmail);
        pedido.agregarObserver(generadorFacturaMock);
        pedido.agregarObserver(fidelizacionMock);
        
        pedido.confirmar();
        pedido.preparar();
        pedido.enviar();
        pedido.entregar();
        
        // Email: 3 veces (Confirmado, Enviado, Entregado)
        verify(mailSenderMock, times(3)).enviarMail(anyString(), anyString(), anyString(), anyString());
        
        // Factura: recibe 4 notificaciones pero solo actúa en Entregado
        verify(generadorFacturaMock, times(4)).notificar(any(), any(), any());
        
        // Fidelización: recibe 4 notificaciones pero solo actúa en Cancelado
        verify(fidelizacionMock, times(4)).notificar(any(), any(), any());
    }
    
    @Test
    void testFlujoCompleto_ConfirmadoCancelado_ConNotificaciones() {
        pedido.agregarObserver(notificadorEmail);
        pedido.agregarObserver(generadorFacturaMock);
        pedido.agregarObserver(fidelizacionMock);
        
        pedido.confirmar();
        pedido.cancelar();
        
        // Email: solo 1 vez (Confirmado)
        verify(mailSenderMock, times(1)).enviarMail(anyString(), anyString(), anyString(), anyString());
        
        // Todos los observers reciben 2 notificaciones
        verify(generadorFacturaMock, times(2)).notificar(any(), any(), any());
        verify(fidelizacionMock, times(2)).notificar(any(), any(), any());
    }

}