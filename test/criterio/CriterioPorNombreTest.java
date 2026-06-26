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


public class CriterioPorNombreTest {

	@Test
    void testFiltrarProductoQueCoincideConNombre() {
        // setUp - Construccion de clases con Mock 
        String nombreFiltro = "auriculares";
        Producto productoCoincidente = mock(Producto.class);
        Producto productoNoCoincidente = mock(Producto.class);
        
        //Configuro el comportamiento de los mocks 
        when(productoCoincidente.getNombre()).thenReturn("Auriculares inalambrico");
        when(productoNoCoincidente.getNombre()).thenReturn("Teclado inalambrico");
        
        //Armo lista de productos a filtrar y construyo el criterio con la categoria
        List<Producto> productos = Arrays.asList(productoCoincidente, productoNoCoincidente);
        CriterioPorNombre criterio = new CriterioPorNombre(nombreFiltro);
        
        // Exercise
        List<Producto> resultado = criterio.filtrar(productos);
        
        // Verify - Verificacion de los resultados esperados 
        assertEquals(1, resultado.size());
        assertEquals(productoCoincidente, resultado.get(0));
        verify(productoCoincidente).getNombre();
        verify(productoNoCoincidente).getNombre();
    }
	
	@Test
    void testFiltrarListaVacia() {
        // SetUp - Armo el escenario a probar 
        String nombreFiltro = "auriculares";
        List<Producto> productosVacio = new ArrayList<>();
        CriterioPorNombre criterio = new CriterioPorNombre(nombreFiltro);
        
        // Exercise
        List<Producto> resultado = criterio.filtrar(productosVacio);
        
        // Verify
        assertTrue(resultado.isEmpty());
    }
    
    @Test
    void testFiltrarVariosProductosQueCoinciden() {
        // setUp - construccion de productos con mock 
    	String nombreFiltro = "auriculares";
        Producto producto1 = mock(Producto.class);
        Producto producto2 = mock(Producto.class);
        Producto producto3 = mock(Producto.class);
    	
        // Configuracion de comportamiento de los mocks
        when(producto1.getNombre()).thenReturn("Auriculares inalambricos");
        when(producto2.getNombre()).thenReturn("Teclado inalambricos");
        when(producto3.getNombre()).thenReturn("Auriculares Samsung");
        
        List<Producto> productos = Arrays.asList(producto1, producto2, producto3);
        CriterioPorNombre criterio = new CriterioPorNombre(nombreFiltro);
        
        // Exercise 
        List<Producto> resultado = criterio.filtrar(productos);
        
        // Verify - verificar los resultados esperados 
        assertEquals(2, resultado.size());
        assertTrue(resultado.contains(producto1));
        assertTrue(resultado.contains(producto3));
        assertFalse(resultado.contains(producto2));
    }
    
    @Test
    void testFiltrarNingunProductoCoincide() {
        // setUp - construccion de productos con mock 
    	String nombreFiltro = "auriculares";
        Producto producto1 = mock(Producto.class);
        Producto producto2 = mock(Producto.class);
        
        //Configuracion de comportamiento de los mocks
        when(producto1.getNombre()).thenReturn("Monitor Samsung");
        when(producto2.getNombre()).thenReturn("Teclado inalambricos");
        
        //Construccion de lista de productos y el filtro 
        List<Producto> productos = Arrays.asList(producto1, producto2);
        CriterioPorNombre criterio = new CriterioPorNombre(nombreFiltro);
        
        // Exercise 
        List<Producto> resultado = criterio.filtrar(productos);
        
        // Verify
        assertTrue(resultado.isEmpty());
    }
    
    @Test
    void testFiltrar_TodosLosProductosCoinciden() {
        // Given
    	// setUp - construccion de productos con mock 
    	String nombreFiltro = "inalambricos";
        Producto producto1 = mock(Producto.class);
        Producto producto2 = mock(Producto.class);
        List<Producto> productos = Arrays.asList(producto1, producto2);
        CriterioPorNombre criterio = new CriterioPorNombre(nombreFiltro);
        
        when(producto1.getNombre()).thenReturn("Auriculares inalambricos");
        when(producto2.getNombre()).thenReturn("Teclado inalambricos");
        
        // When
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
    	String nombreFiltro = "inalambricos";
        CriterioPorDisponibilidad otroFiltro = mock(CriterioPorDisponibilidad.class);
        CriterioPorNombre criterio = new CriterioPorNombre(nombreFiltro);
        
        // When - No debería lanzar excepción
        criterio.agregar(otroFiltro);
        
        // Then - El método se ejecuta sin problemas
        assertTrue(true);
    }

    @Test
    void testEliminarCriterio_NoHaceNada() {
    	// Given - El método agregar está vacío en la implementación
    	String nombreFiltro = "inalambricos";
        CriterioPorDisponibilidad otroFiltro = mock(CriterioPorDisponibilidad.class);
        CriterioPorNombre criterio = new CriterioPorNombre(nombreFiltro);
        
        // When - No debería lanzar excepción
        criterio.eliminar(otroFiltro);
        
        // Then - El método se ejecuta sin problemas
        assertTrue(true);
    }
}
