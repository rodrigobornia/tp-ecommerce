package criterio;

import java.util.List;
import java.util.stream.Collectors;

import Producto.Producto;

public class CriterioPorCategoria implements Criterio{

	String categoria;

	public CriterioPorCategoria(String categoriaFiltro) {
		this.categoria = categoriaFiltro;
	}

	@Override
	public List<Producto> filtrar(List<Producto> listaDeProductos) {
		// TODO Auto-generated method stub
		return listaDeProductos.stream().filter(producto -> producto.getCategoria().contentEquals(this.categoria)).collect(Collectors.toList());
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
