package envio;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pedido.Pedido;

class EnvioTest {

    private Pedido pedidoMock;

    @BeforeEach
    void setUp() {
        // Mockeamos el pedido una sola vez para usarlo en todos los tests
        pedidoMock = mock(Pedido.class);
    }

    @Test
    void testEnvioEstandarCalculaCostoYFechaCorrectamente() {
        
        CorreoArgentino correoMock = mock(CorreoArgentino.class);
  
        EnvioEstandar envioEstandar = new EnvioEstandar(correoMock);
        
        when(pedidoMock.getPesoTotal()).thenReturn(12.5);
        when(pedidoMock.getDireccionDestino()).thenReturn("Calle Falsa 123");
        when(correoMock.estimarEnvio(12.5, "Calle Falsa 123")).thenReturn(1500.0);

        
        double costo = envioEstandar.calcularCostoEnvio(pedidoMock);
        LocalDate fechaEntrega = envioEstandar.calcularEntrega(pedidoMock);

   
        assertEquals(1500.0, costo, 0.01);
        assertEquals(LocalDate.now().plusDays(6), fechaEntrega);
    }

    @Test
    void testEnvioExpressCalculaCostoYFechaCorrectamente() {
     
        LibEnvioExpress libExpressMock = mock(LibEnvioExpress.class);
        
        EnvioExpress envioExpress = new EnvioExpress(libExpressMock);
        
        when(pedidoMock.valorTotal()).thenReturn(45000.0);
        when(libExpressMock.calcularCosto(45000.0)).thenReturn(2500.0);
        
      
        double costo = envioExpress.calcularCostoEnvio(pedidoMock);
        LocalDate fechaEntrega = envioExpress.calcularEntrega(pedidoMock);

        
        assertEquals(2500.0, costo, 0.01);
        assertEquals(LocalDate.now().plusDays(1), fechaEntrega);
    }

    @Test
    void testRetiroSucursalEsCostoCeroYEntregaInmediataConStock() {
      
        Sucursal sucursalMock = mock(Sucursal.class);
       
        RetiroSucursal retiro = new RetiroSucursal(sucursalMock);
        
        when(sucursalMock.tieneStockDePedido(pedidoMock)).thenReturn(true);

    
        assertEquals(0.0, retiro.calcularCostoEnvio(pedidoMock));
        assertEquals(LocalDate.now(), retiro.calcularEntrega(pedidoMock));
    }

    @Test
    void testRetiroSucursalEsCostoCeroYEntregaEnTresDiasSinStockLocal() {
        
        Sucursal sucursalMock = mock(Sucursal.class);
        RetiroSucursal retiro = new RetiroSucursal(sucursalMock);
        
        when(sucursalMock.tieneStockDePedido(pedidoMock)).thenReturn(false);

        
        assertEquals(0.0, retiro.calcularCostoEnvio(pedidoMock));
        assertEquals(LocalDate.now().plusDays(3), retiro.calcularEntrega(pedidoMock));
    }
}