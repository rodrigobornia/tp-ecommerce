package criterio;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import producto.Producto;


public class OperadorNotTest {

	@Test 
	void notConMultiplesCriteriosTest() {
		//SetUp construccion de clases con Mock
		OperadorAnd and = mock(OperadorAnd.class);
		OperadorOr or = mock(OperadorOr.class);
		CriterioPorCategoria categoria = mock(CriterioPorCategoria.class);
		CriterioPorDisponibilidad disponibilidad = mock(CriterioPorDisponibilidad.class);
		CriterioPorNombre nombre = mock(CriterioPorNombre.class);
		CriterioPorPrecioMaximo precioMaximo = mock(CriterioPorPrecioMaximo.class);
		
		Producto producto1 = mock(Producto.class);
		Producto producto2 = mock(Producto.class);
		Producto producto3 = mock(Producto.class);
		Producto producto4 = mock(Producto.class);
		Producto producto5 = mock(Producto.class);
		Producto producto6 = mock(Producto.class);
		Producto producto7 = mock(Producto.class);
		Producto producto8 = mock(Producto.class);
		Producto producto9 = mock(Producto.class);
		
		List<Producto> productos = Arrays.asList(producto1,producto2,producto3,producto4,producto5,producto6,producto7,producto8,producto9);
		
		List<Producto> productosCriterioCategoria = Arrays.asList(producto1,producto2);
		List<Producto> productosCriterioDisponible = Arrays.asList(producto3,producto4);
		List<Producto> productosCriterioNombre = Arrays.asList(producto5,producto1);
		List<Producto> productosCriterioPrecioMaximo = Arrays.asList(producto7,producto8);
		List<Producto> productosCriterioAnd = Arrays.asList(producto8,producto4);
		List<Producto> productosCriterioOr = Arrays.asList(producto1,producto5);
		
		//Configuro el comportamiento de los mocks 
		when(categoria.filtrar(productos)).thenReturn(productosCriterioCategoria);
		when(disponibilidad.filtrar(productos)).thenReturn(productosCriterioDisponible);
		when(nombre.filtrar(productos)).thenReturn(productosCriterioNombre);
		when(precioMaximo.filtrar(productos)).thenReturn(productosCriterioPrecioMaximo);
		when(and.filtrar(productos)).thenReturn(productosCriterioAnd);
		when(or.filtrar(productos)).thenReturn(productosCriterioOr);
		
		//Instancio el operador Or y le agrego los criterios
		OperadorNot not = new OperadorNot();
		not.agregar(categoria);
		not.agregar(disponibilidad);
		not.agregar(nombre);
		not.agregar(precioMaximo);
		not.agregar(and);
		not.agregar(or);
		
		//Exercise 
		List<Producto> resultado = not.filtrar(productos);
		
		//Verify - Verifico los resultados esperados
		assertEquals(2, resultado.size());
		verify(categoria).filtrar(productos);
		verify(disponibilidad).filtrar(productos);
		verify(nombre).filtrar(productos);
		verify(precioMaximo).filtrar(productos);
		verify(and).filtrar(productos);
		verify(or).filtrar(productos);
		
	}
	
	@Test
    void testOperadorNotConListaVacia() {
        // setUp
        CriterioPorCategoria categoria = mock(CriterioPorCategoria.class);
        List<Producto> listaVacia = new ArrayList<>();
        
        when(categoria.filtrar(listaVacia)).thenReturn(new ArrayList<>());
        
        OperadorNot not = new OperadorNot();
        not.agregar(categoria);
        
        // Exercise
        List<Producto> resultado = not.filtrar(listaVacia);
        
        // Verify
        assertTrue(resultado.isEmpty(), "El resultado debería estar vacío");
    }
	
	@Test
	void testOperadorNotSinFiltros() {
        // setUp
		CriterioPorNombre criterio = mock(CriterioPorNombre.class);
        Producto producto1 = mock(Producto.class);
        Producto producto2 = mock(Producto.class);
        List<Producto> productos = Arrays.asList(producto1, producto2);
        
        OperadorNot not = new OperadorNot();
        not.agregar(criterio); //Agrego el criterio
        
        
        // Exercise
        not.eliminar(criterio);//Elimino el criterio
        List<Producto> resultado = not.filtrar(productos);
        
        // Verify
        assertEquals(2, resultado.size(), "Sin criterios, debería devolver todos los productos");
        assertTrue(resultado.containsAll(productos));
    }

}
