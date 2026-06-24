package criterio;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import Producto.Producto;

public class OperadorOrTest {

	@Test 
	void orConMultiplesCriteriosTest() {
		//SetUp construccion de clases con Mock
		OperadorAnd and = mock(OperadorAnd.class);
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
		
		List<Producto> productos = Arrays.asList(producto1,producto2,producto3,producto4,producto5,producto6,producto7,producto8,producto9);
		
		List<Producto> productosCriterioCategoria = Arrays.asList(producto1,producto2);
		List<Producto> productosCriterioDisponible = Arrays.asList(producto3,producto4);
		List<Producto> productosCriterioNombre = Arrays.asList(producto5,producto6);
		List<Producto> productosCriterioPrecioMaximo = Arrays.asList(producto7,producto8);
		List<Producto> productosCriterioAnd = Arrays.asList(producto9,producto4);
		List<Producto> productosCriterioNot = Arrays.asList(producto1,producto5);
		
		//Configuro el comportamiento de los mocks 
		when(categoria.filtrar(productos)).thenReturn(productosCriterioCategoria);
		
	}
}
