package criterio;

import java.util.List;
import java.util.stream.Collectors;

import producto.Producto;

public class CriterioPorDisponibilidad implements Criterio{

	@Override
	public List<Producto> filtrar(List<Producto> listaDeProductos) {
		// TODO Auto-generated method stub
		return listaDeProductos.stream().filter(producto -> producto.getStock() > 0).collect(Collectors.toList());
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
