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



public class CriterioPorCategoriaTest {

	@Test
    void testFiltrarProductoQueCoincideConCategoria() {
        // setUp - Construccion de clases con Mock 
        String categoriaFiltro = "Limpieza";
        Producto productoCoincidente = mock(Producto.class);
        Producto productoNoCoincidente = mock(Producto.class);
        
        //Configuro el comportamiento de los mocks 
        when(productoCoincidente.getCategoria()).thenReturn(categoriaFiltro);
        when(productoNoCoincidente.getCategoria()).thenReturn("Almacen");
        
        //Armo lista de productos a filtrar y construyo el criterio con la categoria
        List<Producto> productos = Arrays.asList(productoCoincidente, productoNoCoincidente);
        CriterioPorCategoria categoria = new CriterioPorCategoria(categoriaFiltro);
        
        // Exercise
        List<Producto> resultado = categoria.filtrar(productos);
        
        // Verify - Verificacion de los resultados esperados 
        assertEquals(1, resultado.size());
        assertEquals(productoCoincidente, resultado.get(0));
        verify(productoCoincidente).getCategoria();
        verify(productoNoCoincidente).getCategoria();
    }
	
	@Test
    void testFiltrarListaVacia() {
        // SetUp - Armo el escenario a probar 
        String categoriaFiltro = "Electronica";
        List<Producto> productosVacio = new ArrayList<>();
        CriterioPorCategoria criterio = new CriterioPorCategoria(categoriaFiltro);
        
        // Exercise
        List<Producto> resultado = criterio.filtrar(productosVacio);
        
        // Verify
        assertTrue(resultado.isEmpty());
    }
    
    @Test
    void testFiltrarVariosProductosQueCoinciden() {
        // setUp - construccion de productos con mock 
    	String categoriaFiltro = "Electronica";
        Producto producto1 = mock(Producto.class);
        Producto producto2 = mock(Producto.class);
        Producto producto3 = mock(Producto.class);
    	
        // Configuracion de comportamiento de los mocks
        when(producto1.getCategoria()).thenReturn(categoriaFiltro);
        when(producto2.getCategoria()).thenReturn(categoriaFiltro);
        when(producto3.getCategoria()).thenReturn("Hogar");
        
        List<Producto> productos = Arrays.asList(producto1, producto2, producto3);
        CriterioPorCategoria categoria = new CriterioPorCategoria(categoriaFiltro);
        
        
        // Exercise 
        List<Producto> resultado = categoria.filtrar(productos);
        
        // Verify - verificar los resultados esperados 
        assertEquals(2, resultado.size());
        assertTrue(resultado.contains(producto1));
        assertTrue(resultado.contains(producto2));
        assertFalse(resultado.contains(producto3));
    }
    
    @Test
    void testFiltrarNingunProductoCoincide() {
        // setUp - construccion de productos con mock 
    	String categoriaFiltro = "Electronica";
        Producto producto1 = mock(Producto.class);
        Producto producto2 = mock(Producto.class);
        
        //Configuracion de comportamiento de los mocks
        when(producto1.getCategoria()).thenReturn("Limpieza");
        when(producto2.getCategoria()).thenReturn("Audio");
        
        //Construccion de lista de productos y el filtro 
        List<Producto> productos = Arrays.asList(producto1, producto2);
        CriterioPorCategoria categoria = new CriterioPorCategoria(categoriaFiltro);
        
        // Exercise 
        List<Producto> resultado = categoria.filtrar(productos);
        
        // Verify
        assertTrue(resultado.isEmpty());
    }
    
    @Test
    void testFiltrar_TodosLosProductosCoinciden() {
        // Given
    	// setUp - construccion de productos con mock 
    	String categoriaFiltro = "Electronica";
        Producto producto1 = mock(Producto.class);
        Producto producto2 = mock(Producto.class);
        List<Producto> productos = Arrays.asList(producto1, producto2);
        CriterioPorCategoria categoria = new CriterioPorCategoria(categoriaFiltro);
        
        when(producto1.getCategoria()).thenReturn(categoriaFiltro);
        when(producto2.getCategoria()).thenReturn(categoriaFiltro);
        
        // When
        List<Producto> resultado = categoria.filtrar(productos);

        // Then
        assertEquals(2, resultado.size());
        assertTrue(resultado.contains(producto1));
        assertTrue(resultado.contains(producto2));
    }
    
    // ===== TESTS DE MÉTODOS DE COMPOSITE (agregar y eliminar) =====

    @Test
    void testAgregarCriterio_NoHaceNada() {
        // Given - El método agregar está vacío en la implementación
    	String categoriaFiltro = "Electronica";
        CriterioPorNombre otroFiltro = mock(CriterioPorNombre.class);
        CriterioPorCategoria categoria = new CriterioPorCategoria(categoriaFiltro);
        
        // When - No debería lanzar excepción
        categoria.agregar(otroFiltro);
        
        // Then - El método se ejecuta sin problemas
        assertTrue(true);
    }

    @Test
    void testEliminarCriterio_NoHaceNada() {
        // Given - El método eliminar está vacío en la implementación
    	String categoriaFiltro = "Electronica";
        CriterioPorNombre otroFiltro = mock(CriterioPorNombre.class);
        CriterioPorCategoria categoria = new CriterioPorCategoria(categoriaFiltro);
        
        // When - No debería lanzar excepción
        categoria.eliminar(otroFiltro);
        
        // Then - El método se ejecuta sin problemas
        assertTrue(true);
    }
    
	
}
