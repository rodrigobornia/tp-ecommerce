package criterio;

import java.util.List;
import java.util.stream.Collectors;

import producto.Producto;

public class CriterioPorNombre implements Criterio {
	
	String nombre;
	
	public CriterioPorNombre(String nombreFiltro) {
		this.nombre = nombreFiltro;
	}

	@Override
	public List<Producto> filtrar(List<Producto> listaDeProductos) {
		// TODO Auto-generated method stub
		return listaDeProductos.stream()
				.filter(producto -> producto.getNombre()
						.toLowerCase()
						.contains(this.nombre.toLowerCase()))
				.collect(Collectors.toList());
	}

	@Override
	public void agregar(Criterio criterio) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void eliminar(Criterio criterio) {
		// TODO Auto-generated method stub
		
	}

}
