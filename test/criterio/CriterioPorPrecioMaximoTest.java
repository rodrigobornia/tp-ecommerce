package criterio;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import producto.Producto;


public class CriterioPorPrecioMaximoTest {

	@Test
    void testFiltrarProductoMenorQuePrecioMaximo() {
        // setUp - Construccion de clases con Mock 
        double precioMaximo = 5000;
        Producto productoCoincidente = mock(Producto.class);
        Producto productoNoCoincidente = mock(Producto.class);
        
        //Configuro el comportamiento de los mocks 
        when(productoCoincidente.precioFinal()).thenReturn(4000.00);
        when(productoNoCoincidente.precioFinal()).thenReturn(5500.00);
        
        //Armo lista de productos a filtrar y construyo el criterio con la categoria
        List<Producto> productos = Arrays.asList(productoCoincidente, productoNoCoincidente);
        CriterioPorPrecioMaximo criterio = new CriterioPorPrecioMaximo(precioMaximo);
        
        // Exercise
        List<Producto> resultado = criterio.filtrar(productos);
        
        // Verify - Verificacion de los resultados esperados 
        assertEquals(1, resultado.size());
        assertEquals(productoCoincidente, resultado.get(0));
        verify(productoCoincidente).precioFinal();
        verify(productoNoCoincidente).precioFinal();
    }
	
	@Test
    void testFiltrarListaVacia() {
        // SetUp - Armo el escenario a probar 
		double precioMaximo = 5000;
        List<Producto> productosVacio = new ArrayList<>();
        CriterioPorPrecioMaximo criterio = new CriterioPorPrecioMaximo(precioMaximo);
        
        // Exercise
        List<Producto> resultado = criterio.filtrar(productosVacio);
        
        // Verify
        assertTrue(resultado.isEmpty());
    }
    
    @Test
    void testFiltrarVariosProductosQueCoinciden() {
        // setUp - construccion de productos con mock 
    	double precioMaximo = 5000;
        Producto producto1 = mock(Producto.class);
        Producto producto2 = mock(Producto.class);
        Producto producto3 = mock(Producto.class);
    	
        // Configuracion de comportamiento de los mocks
        when(producto1.precioFinal()).thenReturn(4500.00);
        when(producto2.precioFinal()).thenReturn(4000.00);
        when(producto3.precioFinal()).thenReturn(6000.00);
        
        List<Producto> productos = Arrays.asList(producto1, producto2, producto3);
        CriterioPorPrecioMaximo criterio = new CriterioPorPrecioMaximo(precioMaximo);
        
        
        // Exercise 
        List<Producto> resultado = criterio.filtrar(productos);
        
        // Verify - verificar los resultados esperados 
        assertEquals(2, resultado.size());
        assertTrue(resultado.contains(producto1));
        assertTrue(resultado.contains(producto2));
        assertFalse(resultado.contains(producto3));
    }
    
    @Test
    void testFiltrarNingunProductoCoincide() {
        // setUp - construccion de productos con mock 
    	double precioMaximo = 5000;
        Producto producto1 = mock(Producto.class);
        Producto producto2 = mock(Producto.class);
        
        //Configuracion de comportamiento de los mocks
        when(producto1.precioFinal()).thenReturn(6500.00);
        when(producto2.precioFinal()).thenReturn(40000.00);
        
        //Construccion de lista de productos y el filtro 
        List<Producto> productos = Arrays.asList(producto1, producto2);
        CriterioPorPrecioMaximo criterio = new CriterioPorPrecioMaximo(precioMaximo);
        
        // Exercise 
        List<Producto> resultado = criterio.filtrar(productos);
        
        // Verify
        assertTrue(resultado.isEmpty());
    }
    
    @Test
    void testFiltrar_TodosLosProductosCoinciden() {
        // Given
    	// setUp - construccion de productos con mock 
    	double precioMaximo = 5000;
        Producto producto1 = mock(Producto.class);
        Producto producto2 = mock(Producto.class);
        List<Producto> productos = Arrays.asList(producto1, producto2);
        CriterioPorPrecioMaximo criterio = new CriterioPorPrecioMaximo(precioMaximo);
        
        when(producto1.precioFinal()).thenReturn(3500.00);
        when(producto2.precioFinal()).thenReturn(4000.00);
        
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
    	double precioMaximo = 5000;
        CriterioPorNombre otroFiltro = mock(CriterioPorNombre.class);
        CriterioPorPrecioMaximo criterio = new CriterioPorPrecioMaximo(precioMaximo);
        
        // When - No debería lanzar excepción
        criterio.agregar(otroFiltro);
        
        // Then - El método se ejecuta sin problemas
        assertTrue(true);
    }

    @Test
    void testEliminarCriterio_NoHaceNada() {
    	// Given - El método agregar está vacío en la implementación
    	double precioMaximo = 5000;
        CriterioPorNombre otroFiltro = mock(CriterioPorNombre.class);
        CriterioPorPrecioMaximo criterio = new CriterioPorPrecioMaximo(precioMaximo);
        
        // When - No debería lanzar excepción
        criterio.eliminar(otroFiltro);
        
        // Then - El método se ejecuta sin problemas
        assertTrue(true);
    }
    
}
