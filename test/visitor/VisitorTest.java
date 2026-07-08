package visitor;

import static org.junit.jupiter.api.Assertions.assertTrue;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ecommerce.Ecommerce;
import pedido.Borrador;
import pedido.Entregado;
import pedido.Pedido;
import producto.Producto;
import producto.ProductoBase;

class VisitorTest {

    private GeneradorReporteVentas generador;
    
    private ProductoBase termo;
    private ProductoBase mate;
    
    private Pedido pedidoValido;
    private Pedido pedidoViejo;
    private Pedido pedidoNoEntregado;
 
    private Ecommerce ecommerce;
    private List<Producto> catalogo;
    private List<Pedido> historial;

    @BeforeEach
    void setUp() {
    	
    
        generador = new GeneradorReporteVentas();

        // 1. Creamos productos reales para que la matemática interna funcione perfecta
        termo = new ProductoBase(1, "Termo Stanley", "Verde", "Stanley", "Bazar", 5000.0, 0, 10);
        mate = new ProductoBase(2, "Mate de Cuero", "Negro", "Generico", "Bazar", 1000.0, 0, 10);
        
        catalogo = Arrays.asList(termo, mate);

        // 2. Mockeamos los pedidos simulando distintos escenarios
        
        // A) Pedido Perfecto: En fecha (Junio) y Entregado. Llevó 2 termos y 1 mate.
        pedidoValido = mock(Pedido.class);
        when(pedidoValido.getEstado()).thenReturn(new Entregado());
        when(pedidoValido.getFecha()).thenReturn(LocalDate.of(2023, 6, 15));
        when(pedidoValido.getProductos()).thenReturn(Arrays.asList(termo, termo, mate));

        // B) Pedido Fuera de Rango: Entregado, pero en Enero (No debe salir en el reporte)
        pedidoViejo = mock(Pedido.class);
        when(pedidoViejo.getEstado()).thenReturn(new Entregado());
        when(pedidoViejo.getFecha()).thenReturn(LocalDate.of(2023, 1, 10));
        when(pedidoViejo.getProductos()).thenReturn(Arrays.asList(mate, mate));

        // C) Pedido No Entregado: En fecha (Junio), pero en Borrador (No debe salir)
        pedidoNoEntregado = mock(Pedido.class);
        when(pedidoNoEntregado.getEstado()).thenReturn(new Borrador());
        when(pedidoNoEntregado.getFecha()).thenReturn(LocalDate.of(2023, 6, 20));
        when(pedidoNoEntregado.getProductos()).thenReturn(Arrays.asList(termo));

        historial = Arrays.asList(pedidoValido, pedidoViejo, pedidoNoEntregado);
       
        ecommerce = new Ecommerce();
        
        // Le cargamos los productos
        catalogo.forEach(p -> ecommerce.agregarProductoACatalogo(p));
        
        // Le cargamos los pedidos mockeados
        historial.forEach(p -> ecommerce.agregarPedido(p));
    }

    @Test
    void testGenerarReporte_FiltraYCalculaBien_FormatoCSV() {
        // Rango de búsqueda: Solo el mes de Junio
        LocalDate desde = LocalDate.of(2023, 6, 1);
        LocalDate hasta = LocalDate.of(2023, 6, 30);
        
        ReporteVisitor visitorCSV = new ReporteCSV();
        
        String resultado = generador.generar(historial, catalogo, desde, hasta, visitorCSV);
        
        // IMPRESIÓN PARA VERLO EN CONSOLA
        System.out.println("=== REPORTE CSV ===");
        System.out.println(resultado);
        
        // VERIFICACIONES
        // 1.  encabezado
        assertTrue(resultado.contains("Nombre,UnidadesVendidas,PrecioPromedio"));
        
        // 2. Termo se vendió 2 veces a 5000 (Solo suma el pedidoValido)
        assertTrue(resultado.contains("Termo Stanley,2,5000"));
        
        // 3. Mate se vendió 1 vez a 1000 (El pedido de Enero se ignoró)
        assertTrue(resultado.contains("Mate de Cuero,1,1000"));
    }

    @Test
    void testGenerarReporte_FormatoHTML() {
        LocalDate desde = LocalDate.of(2023, 6, 1);
        LocalDate hasta = LocalDate.of(2023, 6, 30);
        
        ReporteVisitor visitorHTML = new ReporteHTML();
        
        String resultado = generador.generar(historial, catalogo, desde, hasta, visitorHTML);
        
        System.out.println("\n=== REPORTE HTML ===");
        System.out.println(resultado);
        
        // Verificamos etiquetas HTML y datos
        assertTrue(resultado.contains("<td>Termo Stanley</td>"));
        assertTrue(resultado.contains("<td>2</td>")); // Unidades
        assertTrue(resultado.contains("<td>$5000"));  // Precio Promedio
        assertTrue(resultado.endsWith("</table>\n"));
    }

    @Test
    void testGenerarReporte_FormatoTXT() {
        LocalDate desde = LocalDate.of(2023, 6, 1);
        LocalDate hasta = LocalDate.of(2023, 6, 30);
        
        ReporteVisitor visitorTXT = new ReporteTXT();
        
        String resultado = generador.generar(historial, catalogo, desde, hasta, visitorTXT);
        
        System.out.println("\n=== REPORTE TXT ===");
        System.out.println(resultado);
        
        assertTrue(resultado.contains("Termo Stanley - Unidades vendidas: 2 - Precio promedio: $5000"));
        assertTrue(resultado.contains("Mate de Cuero - Unidades vendidas: 1 - Precio promedio: $1000"));
    }
    
    @Test
    void testGenerarReporte_SinVentasEnEsePeriodo() {
        // Rango de búsqueda: Diciembre (no hay ventas acá)
        LocalDate desde = LocalDate.of(2023, 12, 1);
        LocalDate hasta = LocalDate.of(2023, 12, 31);
        
        ReporteVisitor visitorTXT = new ReporteTXT();
        
        String resultado = generador.generar(historial, catalogo, desde, hasta, visitorTXT);
        
        // El reporte debería estar vacío porque ningún producto pasó el filtro de "unidadesVendidas > 0"
        assertTrue(resultado.isEmpty(), "El reporte debería estar vacío si no hay ventas");
    }
    @Test
    void testGenerarReporte_A_Traves_Del_Ecommerce() {
        
        LocalDate desde = LocalDate.of(2023, 6, 1);
        LocalDate hasta = LocalDate.of(2023, 6, 30);
        ReporteVisitor visitorTXT = new ReporteTXT();
        
        // When - Le pedimos a la tienda que se encargue de todo
        String resultado = ecommerce.generarReporte(desde, hasta, visitorTXT);
        
        // IMPRESIÓN PARA VERLO EN CONSOLA
        System.out.println("\n=== REPORTE GENERADO DESDE ECOMMERCE (FACADE) ===");
        System.out.println(resultado);
        
        // Then - Verificamos que delegue bien y arme el texto correcto
        assertTrue(resultado.contains("Termo Stanley - Unidades vendidas: 2"));
        assertTrue(resultado.contains("Mate de Cuero - Unidades vendidas: 1"));
    }
}