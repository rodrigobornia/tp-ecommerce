package criterio;

import java.util.List;

import Producto.Producto;

public interface Criterio {

	public List<Producto> filtrar(List<Producto> listaDeProductos);
	public void agregar(Criterio criterio);
	public void eliminar(Criterio criterio);
}
