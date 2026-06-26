package criterio;

import java.util.List;
import java.util.stream.Collectors;

import producto.Producto;

public class CriterioPorPrecioMaximo implements Criterio {
	
	double precioMaximo;
	
	public CriterioPorPrecioMaximo(double precioMaximoFiltro) {
		this.precioMaximo = precioMaximoFiltro;
	}

	@Override
	public List<Producto> filtrar(List<Producto> listaDeProductos) {
		// TODO Auto-generated method stub
		return listaDeProductos.stream()
				.filter(producto -> producto.precioFinal() <= this.precioMaximo)
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
