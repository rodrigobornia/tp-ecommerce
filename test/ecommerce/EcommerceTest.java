package ecommerce;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import criterio.Criterio;
import envio.MetodoDeEnvio;
import metodoDePago.MetodoDePago;
import pedido.Pedido;
import producto.Producto;
import visitor.ExportadorReporte;

import visitor.RecolectorVentasVisitor;


class EcommerceTest {

    private Ecommerce ecommerce;
    
    @Mock
    private Producto productoMock1;
    
    @Mock
    private Producto productoMock2;
    
    @Mock
    private Criterio criterioMock;
    
    @Mock
    private MetodoDeEnvio metodoEnvioMock;
    
    @Mock
    private MetodoDePago metodoPagoMock;
    
    @BeforeEach
    void setUp() {
        ecommerce = new Ecommerce();
    }
    
    // ============================================================
    // TEST 1: Agregar producto al catálogo
    // ============================================================
    @Test
    void testAgregarProductoACatalogo() {
        Criterio criterioMock = mock(Criterio.class);
    	// When
        ecommerce.agregarProductoACatalogo(productoMock1);
        ecommerce.agregarProductoACatalogo(productoMock2);
        
        // Then - Verificar que se agregaron usando buscarProducto
        when(criterioMock.filtrar(anyList())).thenReturn(Arrays.asList(productoMock1, productoMock2));
        
        List<Producto> resultado = ecommerce.buscarProducto(criterioMock);
        
        assertEquals(2, resultado.size());
        verify(criterioMock, times(1)).filtrar(anyList());
    }
    
    // ============================================================
    // TEST 2: Buscar producto con criterio
    // ============================================================
    @Test
    void testBuscarProducto_DelegaEnCriterio() {
        // Given
        Criterio criterioMock = mock(Criterio.class);
        List<Producto> catalogoEsperado = Arrays.asList(productoMock1);
        when(criterioMock.filtrar(anyList())).thenReturn(catalogoEsperado);
        
        // When
        List<Producto> resultado = ecommerce.buscarProducto(criterioMock);
        
        // Then
        assertEquals(1, resultado.size());
        verify(criterioMock, times(1)).filtrar(anyList());
    }
    
    // ============================================================
    // TEST 3: Buscar producto sin resultados
    // ============================================================
    @Test
    void testBuscarProducto_SinResultados() {
        // Given
        Criterio criterioMock = mock(Criterio.class);

        when(criterioMock.filtrar(anyList())).thenReturn(Arrays.asList());
        
        // When
        List<Producto> resultado = ecommerce.buscarProducto(criterioMock);
        
        // Then
        assertTrue(resultado.isEmpty());
        verify(criterioMock, times(1)).filtrar(anyList());
    }
    
    // ============================================================
    // TEST 4: Crear pedido
    // ============================================================
    @Test
    void testCrearPedido() {
        // Given
        String direccion = "Calle Falsa 123";
        List<Producto> productos = Arrays.asList(productoMock1, productoMock2);
        
        // When
        ecommerce.crearPedido(direccion, productos, metodoEnvioMock, metodoPagoMock);
        
        // Then - Verificar que no lanza excepción
        assertDoesNotThrow(() -> 
            ecommerce.crearPedido(direccion, productos, metodoEnvioMock, metodoPagoMock)
        );
    }
    
    // ============================================================
    // TEST 5: Agregar y quitar pedido
    // ============================================================
    @Test
    void testAgregarYQuitarPedido() {
        // Given
        Pedido pedidoMock = mock(Pedido.class);
        
        // When - Agregar
        ecommerce.agregarPedido(pedidoMock);
        
        // Then - No lanza excepción
        assertDoesNotThrow(() -> ecommerce.agregarPedido(pedidoMock));
        
        // When - Quitar
        ecommerce.quitarPedido(pedidoMock);
        
        // Then - No lanza excepción
        assertDoesNotThrow(() -> ecommerce.quitarPedido(pedidoMock));
    }
    
    // ============================================================
    // TEST 6: Flujo completo - Crear pedido con productos del catálogo
    // ============================================================
    @Test
    void testFlujoCompleto_AgregarProductos_Buscar_CrearPedido() {
        Criterio criterioMock = mock(Criterio.class);
        // Given - Agregar productos al catálogo

        ecommerce.agregarProductoACatalogo(productoMock1);
        ecommerce.agregarProductoACatalogo(productoMock2);
        
        // Given - Configurar criterio para buscar
        when(criterioMock.filtrar(anyList())).thenReturn(Arrays.asList(productoMock1));
        
        // When - Buscar productos
        List<Producto> productosEncontrados = ecommerce.buscarProducto(criterioMock);
        
        // Then - Verificar búsqueda
        assertEquals(1, productosEncontrados.size());
        verify(criterioMock, times(1)).filtrar(anyList());
        
        // When - Crear pedido con el producto encontrado
        String direccion = "Calle Falsa 123";
        
        // Then - Verificar que no lanza excepción
        assertDoesNotThrow(() -> 
            ecommerce.crearPedido(direccion, productosEncontrados, metodoEnvioMock, metodoPagoMock)
        );
    }
 // ============================================================
    // TEST 7: Quitar producto del catálogo
    // ============================================================
    @Test
    void testQuitarProductoDeCatalogo() {
        // Given
        ecommerce.agregarProductoACatalogo(productoMock1);
        
        // When
        ecommerce.quitarProductoDeCatalogo(productoMock1);
        
        // Then - Verificamos que no rompa y se quite (usamos buscar para comprobar)
        Criterio criterioMock = mock(Criterio.class);
        when(criterioMock.filtrar(anyList())).thenReturn(Arrays.asList()); 
        
        List<Producto> resultado = ecommerce.buscarProducto(criterioMock);
        assertTrue(resultado.isEmpty());
    }

    // ============================================================
    // TEST 8: Generar Reporte
    // ============================================================

    @Test
    void testGenerarReporte_Exito() {
        // 1. Creamos los mocks
        Producto producto1 = mock(Producto.class);
        Producto producto2 = mock(Producto.class);
        
        // 2. Los agregamos al catálogo
        ecommerce.agregarProductoACatalogo(producto1);
        ecommerce.agregarProductoACatalogo(producto2);
        
        // 3. Usamos un Visitor REAL 
        RecolectorVentasVisitor visitorReal = new RecolectorVentasVisitor( new java.util.ArrayList<>(), java.time.LocalDate.now(), java.time.LocalDate.now());
        
        // 4. Mockeamos el Exportador
        ExportadorReporte exportadorMock = mock(ExportadorReporte.class);
        when(exportadorMock.exportar(anyList())).thenReturn("REPORTE_MOCK_GENERADO");
        
        // When
        String resultado = ecommerce.generarReporte(visitorReal, exportadorMock);
        
        // Then
        // A. Verificamos que devuelva el string correcto
        assertEquals("REPORTE_MOCK_GENERADO", resultado);
        
        // B. Verificamos que el ECommerce le hizo "accept" a los productos que creamos acá
        verify(producto1, times(1)).accept(visitorReal);
        verify(producto2, times(1)).accept(visitorReal);
    }

}
