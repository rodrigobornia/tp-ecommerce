package criterio;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import Producto.Producto;


public class OperadorAnd implements Criterio{
	
	List<Criterio> criterios = new ArrayList<>();

	@Override
	public List<Producto> filtrar(List<Producto> listaDeProductos) {
		// TODO Auto-generated method stub
		if (criterios.isEmpty()) {
	        return new ArrayList<>(listaDeProductos);
	    }
	    
	    Set<Producto> resultado = new HashSet<>(listaDeProductos);
	    
	    for (Criterio criterio : criterios) {
	        Set<Producto> filtrado = new HashSet<>(criterio.filtrar(listaDeProductos));
	        resultado.retainAll(filtrado);
	        
	        // si ya está vacío, salir del loop
	        if (resultado.isEmpty()) {
	            break;
	        }
	    }
	    return new ArrayList<>(resultado);
	}

	@Override
	public void agregar(Criterio criterio) {
		// TODO Auto-generated method stub
		criterios.add(criterio);
		
	}

	@Override
	public void eliminar(Criterio criterio) {
		// TODO Auto-generated method stub
		criterios.remove(criterio);
	}

}
