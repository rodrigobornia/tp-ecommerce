package pedido;

import static org.mockito.Mockito.*;


import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import producto.Producto;

public class PedidoTest {
    
    @Mock
    private Producto productoMock;
    
    private Pedido pedido;
    
    @BeforeEach
    void setUp() {
        pedido = new Pedido();
    }
    
    // ============ TEST BORRADOR ============
    
    @Test
    void testBorrador_AgregarItem_DeberiaAgregarProducto() {
        pedido.agregarItem(productoMock);
        assertTrue(pedido.productos.contains(productoMock));
        assertEquals(1, pedido.productos.size());
    }
    
    @Test
    void testBorrador_QuitarItem_DeberiaQuitarProducto() {
        pedido.agregarItem(productoMock);
        pedido.quitarItem(productoMock);
        assertFalse(pedido.productos.contains(productoMock));
        assertEquals(0, pedido.productos.size());
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
        Pedido pedidoSpy = spy(Pedido.class);
        pedidoSpy.setEstado(new Confirmado());
        
        pedidoSpy.preparar();
        
        verify(pedidoSpy, times(1)).decrementarStock();
        verify(pedidoSpy, never()).incrementarStock();
        assertEquals(Preparacion.class, pedidoSpy.getEstado().getClass());
    }
    
    @Test
    void testConfirmado_Cancelar_NOIncrementaStock_PorqueNuncaSeDecremento() {
        Pedido pedidoSpy = spy(Pedido.class);
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
        Pedido pedidoSpy = spy(Pedido.class);
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
        Pedido pedidoSpy = spy(Pedido.class);
        pedidoSpy.setEstado(new Enviado());
        
        pedidoSpy.cancelar();
        
        verify(pedidoSpy, times(1)).incrementarStock();
        verify(pedidoSpy, times(1)).reembolsarCostoProductos();
        verify(pedidoSpy, never()).reembolsarCostoEnvio();
        assertEquals(Cancelado.class, pedidoSpy.getEstado().getClass());
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
        Pedido pedidoSpy = spy(Pedido.class);
        
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
        Pedido pedidoSpy = spy(Pedido.class);
        
        // Borrador -> Cancelado
        pedidoSpy.cancelar();
        
        verify(pedidoSpy, never()).incrementarStock();
        verify(pedidoSpy, never()).reembolsarCostoProductos();
        verify(pedidoSpy, never()).reembolsarCostoEnvio();
        assertEquals(Cancelado.class, pedidoSpy.getEstado().getClass());
    }
    
    @Test
    void testFlujoConCancelacion_DesdeConfirmado_NoReembolsaNiStock() {
        Pedido pedidoSpy = spy(Pedido.class);
        pedidoSpy.setEstado(new Confirmado());
        
        pedidoSpy.cancelar();
        
        verify(pedidoSpy, never()).incrementarStock();
        verify(pedidoSpy, never()).reembolsarCostoProductos();
        verify(pedidoSpy, never()).reembolsarCostoEnvio();
        assertEquals(Cancelado.class, pedidoSpy.getEstado().getClass());
    }
    
    @Test
    void testFlujoConCancelacion_DesdePreparacion_ReembolsaTodoEIncrementaStock() {
        Pedido pedidoSpy = spy(Pedido.class);
        pedidoSpy.setEstado(new Preparacion());
        
        pedidoSpy.cancelar();
        
        verify(pedidoSpy, times(1)).incrementarStock();
        verify(pedidoSpy, times(1)).reembolsarCostoProductos();
        verify(pedidoSpy, times(1)).reembolsarCostoEnvio();
        assertEquals(Cancelado.class, pedidoSpy.getEstado().getClass());
    }
    
    @Test
    void testFlujoConCancelacion_DesdeEnviado_ReembolsaSoloProductosEIncrementaStock() {
        Pedido pedidoSpy = spy(Pedido.class);
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
}