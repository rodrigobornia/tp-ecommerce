package visitor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import pedido.Pedido;
import producto.Paquete;
import producto.ProductoBase;

class VisitorTest {

    private Pedido pedidoValido1;
    private Pedido pedidoValido2;
    private Pedido pedidoFueraDeFecha;
    
    private ProductoBase termoMock;
    private Paquete comboMock;
    
    private List<Pedido> historialMock;

    @BeforeEach
    void setUp() {
        // 1. Mockeamos los productos que se van a vender
        termoMock = mock(ProductoBase.class);
        when(termoMock.getNombre()).thenReturn("Termo Stanley");
        when(termoMock.precioFinal()).thenReturn(5000.0); // Precio al que se vendió

        comboMock = mock(Paquete.class);
        when(comboMock.getNombre()).thenReturn("Combo Mate");
        when(comboMock.precioFinal()).thenReturn(8000.0);

        // 2. Mockeamos los pedidos simulando sus fechas y su contenido
        // Pedido 1: Se hizo en enero, llevó 2 Termos y 1 Combo
        pedidoValido1 = mock(Pedido.class);
        when(pedidoValido1.getFecha()).thenReturn(LocalDate.of(2023, 1, 10));
        when(pedidoValido1.getProductos()).thenReturn(Arrays.asList(termoMock, termoMock, comboMock));

        // Pedido 2: Se hizo a fin de enero, llevó 1 Termo
        pedidoValido2 = mock(Pedido.class);
        when(pedidoValido2.getFecha()).thenReturn(LocalDate.of(2023, 1, 25));
        when(pedidoValido2.getProductos()).thenReturn(Arrays.asList(termoMock));

        // Pedido 3: Fuera del rango de búsqueda (Febrero), llevó 1 Combo (No debería sumarse)
        pedidoFueraDeFecha = mock(Pedido.class);
        when(pedidoFueraDeFecha.getFecha()).thenReturn(LocalDate.of(2023, 2, 5));
        when(pedidoFueraDeFecha.getProductos()).thenReturn(Arrays.asList(comboMock));

        // Armamos el historial completo
        historialMock = Arrays.asList(pedidoValido1, pedidoValido2, pedidoFueraDeFecha);
    }

    // =========================================================================
    // TESTS DEL NÚCLEO MATEMÁTICO (VISITOR)
    // =========================================================================

    @Test
    void testRecolectorCalculaEstadisticasYOrdenaCorrectamente() {
        // Buscamos ventas solo de Enero
        LocalDate inicio = LocalDate.of(2023, 1, 1);
        LocalDate fin = LocalDate.of(2023, 1, 31);
        
        RecolectorVentasVisitor recolector = new RecolectorVentasVisitor(historialMock, inicio, fin);
        
        // Simulamos que el ECommerce hace el accept pasándole el visitor
        recolector.visitProductoBase(termoMock);
        recolector.visitPaquete(comboMock);
        
        List<ItemReporteDTO> resultados = recolector.getResultadosOrdenados();
        
        // Verificaciones
        assertEquals(2, resultados.size(), "Debería haber 2 ítems en el reporte");
        
        // Como se vendieron 3 Termos y 1 Combo en Enero, el Termo debe estar primero (índice 0)
        ItemReporteDTO masVendido = resultados.get(0);
        assertEquals("Termo Stanley", masVendido.getNombreItem());
        assertEquals(3, masVendido.getUnidadesVendidas()); // 2 del pedido1 + 1 del pedido2
        assertEquals(5000.0, masVendido.getPrecioPromedioCobrado());
        
        ItemReporteDTO segundoVendido = resultados.get(1);
        assertEquals("Combo Mate", segundoVendido.getNombreItem());
        assertEquals(1, segundoVendido.getUnidadesVendidas()); // 1 del pedido1 (el de febrero se ignora)
        assertEquals(8000.0, segundoVendido.getPrecioPromedioCobrado());
    }

    // =========================================================================
    // TESTS DE LOS FORMATOS (STRATEGIES)
    // =========================================================================

    @Test
    void testExportadorCSV() {
        List<ItemReporteDTO> datos = Arrays.asList(
            new ItemReporteDTO("Termo", 5, 1000.0)
        );
        
        ExportadorReporte exportador = new ExportadorCSV();
        String resultado = exportador.exportar(datos);
        
        System.out.println("\n=== REPORTE CSV GENERADO ===");
        System.out.println(resultado);
        System.out.println("============================\n");
        
        assertTrue(resultado.contains("Nombre_Item,Unidades_Vendidas,Precio_Promedio"));
        assertTrue(resultado.contains("Termo,5,1000,00") || resultado.contains("Termo,5,1000.00")); 
    }

    @Test
    void testExportadorTXT() {
        List<ItemReporteDTO> datos = Arrays.asList(
            new ItemReporteDTO("Mate", 2, 500.0)
        );
        
        ExportadorReporte exportador = new ExportadorTXT();
        String resultado = exportador.exportar(datos);
       
        System.out.println("\n=== REPORTE TXT GENERADO ===");
        System.out.println(resultado);
        System.out.println("============================\n");
        
        assertTrue(resultado.contains("=== REPORTE DE VENTAS ==="));
        assertTrue(resultado.contains("- Mate"));
        assertTrue(resultado.contains("Unidades: 2"));
    }
    @Test
    void testExportadorHTML() {
        List<ItemReporteDTO> datos = Arrays.asList(
            new ItemReporteDTO("Bombilla", 10, 200.0)
        );
        
        ExportadorReporte exportador = new ExportadorHTML();
        String resultado = exportador.exportar(datos);
        
   
        System.out.println("\n=== REPORTE HTML GENERADO ===");
        System.out.println(resultado);
        System.out.println("=============================\n");

        assertTrue(resultado.contains("<!DOCTYPE html>"));
        assertTrue(resultado.contains("<td>Bombilla</td>"));
        assertTrue(resultado.contains("<td>10</td>"));
    
    }
}