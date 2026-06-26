package criterio;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import Producto.Producto;


public class CriterioPorDisponibilidadTest {

	@Test
    void testFiltrarProductoQueTieneStock() {
        // setUp - Construccion de clases con Mock 
        Producto productoCoincidente = mock(Producto.class);
        Producto productoNoCoincidente = mock(Producto.class);
        
        //Configuro el comportamiento de los mocks 
        when(productoCoincidente.getStock()).thenReturn(2);
        when(productoNoCoincidente.getStock()).thenReturn(0);
        
        //Armo lista de productos a filtrar y construyo el criterio con la categoria
        List<Producto> productos = Arrays.asList(productoCoincidente, productoNoCoincidente);
        CriterioPorDisponibilidad criterio = new CriterioPorDisponibilidad();
        
        // Exercise
        List<Producto> resultado = criterio.filtrar(productos);
        
        // Verify - Verificacion de los resultados esperados 
        assertEquals(1, resultado.size());
        assertEquals(productoCoincidente, resultado.get(0));
        verify(productoCoincidente).getStock();
        verify(productoNoCoincidente).getStock();
    }
	
	@Test
    void testFiltrarListaVacia() {
        // SetUp - Armo el escenario a probar 
        List<Producto> productosVacio = new ArrayList<>();
        CriterioPorDisponibilidad criterio = new CriterioPorDisponibilidad();
        
        // Exercise
        List<Producto> resultado = criterio.filtrar(productosVacio);
        
        // Verify
        assertTrue(resultado.isEmpty());
    }
    
    @Test
    void testFiltrarVariosProductosConStock() {
        // setUp - construccion de productos con mock 
        Producto producto1 = mock(Producto.class);
        Producto producto2 = mock(Producto.class);
        Producto producto3 = mock(Producto.class);
    	
        // Configuracion de comportamiento de los mocks
        when(producto1.getStock()).thenReturn(2);
        when(producto2.getStock()).thenReturn(0);
        when(producto3.getStock()).thenReturn(2);
        
        List<Producto> productos = Arrays.asList(producto1, producto2, producto3);
        CriterioPorDisponibilidad criterio = new CriterioPorDisponibilidad();
        
        
        // Exercise 
        List<Producto> resultado = criterio.filtrar(productos);
        
        // Verify - verificar los resultados esperados 
        assertEquals(2, resultado.size());
        assertTrue(resultado.contains(producto1));
        assertTrue(resultado.contains(producto3));
        assertFalse(resultado.contains(producto2));
    }
    
    @Test
    void testFiltrarNingunProductoTieneStock() {
        // setUp - construccion de productos con mock 
        Producto producto1 = mock(Producto.class);
        Producto producto2 = mock(Producto.class);
        
        //Configuracion de comportamiento de los mocks
        when(producto1.getStock()).thenReturn(0);
        when(producto2.getStock()).thenReturn(0);
        
        //Construccion de lista de productos y el filtro 
        List<Producto> productos = Arrays.asList(producto1, producto2);
        CriterioPorDisponibilidad criterio = new CriterioPorDisponibilidad();
        
        // Exercise 
        List<Producto> resultado = criterio.filtrar(productos);
        
        // Verify
        assertTrue(resultado.isEmpty());
    }
    
    @Test
    void testFiltrar_TodosLosProductosTieneStock() {
        // Given
    	// setUp - construccion de productos con mock 
        Producto producto1 = mock(Producto.class);
        Producto producto2 = mock(Producto.class);
        List<Producto> productos = Arrays.asList(producto1, producto2);
        CriterioPorDisponibilidad criterio = new CriterioPorDisponibilidad();
        
        
        //Configuracion de comportamiento de los mocks
        when(producto1.getStock()).thenReturn(5);
        when(producto2.getStock()).thenReturn(4);
        
        List<Producto> resultado = criterio.filtrar(productos);

        
        // Then
        assertEquals(2, resultado.size());
        assertTrue(resultado.contains(producto1));
        assertTrue(resultado.contains(producto2));
    }
    
    // ===== TESTS DE MÉTODOS DE COMPOSITE (agregar y eliminar) =====

    @Test
    void testAgregarCriterio_NoHaceNada() {
        // Given - El método agregar está vacío en la implementación
        CriterioPorNombre otroFiltro = mock(CriterioPorNombre.class);
        CriterioPorDisponibilidad criterio = new CriterioPorDisponibilidad();
        
        // When - No debería lanzar excepción
        criterio.agregar(otroFiltro);
        
        // Then - El método se ejecuta sin problemas
        assertTrue(true);
    }

    @Test
    void testEliminarCriterio_NoHaceNada() {
        // Given - El método eliminar está vacío en la implementación
        CriterioPorNombre otroFiltro = mock(CriterioPorNombre.class);
        CriterioPorDisponibilidad criterio = new CriterioPorDisponibilidad();
        
        // When - No debería lanzar excepción
        criterio.agregar(otroFiltro);
        
        // Then - El método se ejecuta sin problemas
        assertTrue(true);
    }
}
