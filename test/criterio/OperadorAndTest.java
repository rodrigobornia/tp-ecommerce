package criterio;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


import producto.Producto;

public class OperadorAndTest {

	@Test 
	void andConMultiplesCriteriosTest() {
		//SetUp construccion de clases con Mock
		OperadorOr or = mock(OperadorOr.class);
		OperadorNot not = mock(OperadorNot.class);
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
		Producto productoFiltrado = mock(Producto.class);
		
		List<Producto> productos = Arrays.asList(producto1,producto2,producto3,producto4,producto5,producto6,producto7,producto8,producto9,productoFiltrado);
		
		List<Producto> productosCriterioCategoria = Arrays.asList(producto1,producto2,productoFiltrado);
		List<Producto> productosCriterioDisponible = Arrays.asList(producto3,producto4,productoFiltrado);
		List<Producto> productosCriterioNombre = Arrays.asList(producto5,producto6,productoFiltrado);
		List<Producto> productosCriterioPrecioMaximo = Arrays.asList(producto7,producto8,productoFiltrado);
		List<Producto> productosCriterioOr = Arrays.asList(producto9,producto4,productoFiltrado);
		List<Producto> productosCriterioNot = Arrays.asList(producto1,producto5,productoFiltrado);
		
		//Configuro el comportamiento de los mocks 
		when(categoria.filtrar(productos)).thenReturn(productosCriterioCategoria);
		when(disponibilidad.filtrar(productos)).thenReturn(productosCriterioDisponible);
		when(nombre.filtrar(productos)).thenReturn(productosCriterioNombre);
		when(precioMaximo.filtrar(productos)).thenReturn(productosCriterioPrecioMaximo);
		when(or.filtrar(productos)).thenReturn(productosCriterioOr);
		when(not.filtrar(productos)).thenReturn(productosCriterioNot);
		
		//Instancio el operador And y le agrego los criterios
		OperadorAnd and = new OperadorAnd();
		and.agregar(categoria);
		and.agregar(disponibilidad);
		and.agregar(nombre);
		and.agregar(precioMaximo);
		and.agregar(or);
		and.agregar(not);
		
		//Exercise 
		List<Producto> resultado = and.filtrar(productos);
		
		//Verify - Verifico los resultados esperados
		assertEquals(1, resultado.size());
		assertEquals(productoFiltrado, resultado.get(0));
		verify(categoria).filtrar(productos);
		verify(disponibilidad).filtrar(productos);
		verify(nombre).filtrar(productos);
		verify(precioMaximo).filtrar(productos);
		verify(or).filtrar(productos);
		verify(not).filtrar(productos);
		
	}
	
	@Test
    void testOperadorAndConListaVacia() {
        // setUp
        CriterioPorCategoria categoria = mock(CriterioPorCategoria.class);
        List<Producto> listaVacia = new ArrayList<>();
        
        when(categoria.filtrar(listaVacia)).thenReturn(new ArrayList<>());
        
        OperadorAnd and = new OperadorAnd();
        and.agregar(categoria);
        
        // Exercise
        List<Producto> resultado = and.filtrar(listaVacia);
        
        // Verify
        assertTrue(resultado.isEmpty(), "El resultado debería estar vacío");
    }
	
	@Test
	void testOperadorAndSinFiltros() {
        // setUp
		CriterioPorNombre criterio = mock(CriterioPorNombre.class);
        Producto producto1 = mock(Producto.class);
        Producto producto2 = mock(Producto.class);
        List<Producto> productos = Arrays.asList(producto1, producto2);
        
        OperadorAnd and = new OperadorAnd();
        and.agregar(criterio); //Agrego el criterio
        
        
        // Exercise
        and.eliminar(criterio);//Elimino el criterio
        List<Producto> resultado = and.filtrar(productos);
        
        // Verify
        assertEquals(2, resultado.size(), "Sin criterios, debería devolver todos los productos");
        assertTrue(resultado.containsAll(productos));
    }
}
