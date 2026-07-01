package ecommerce;

import java.util.ArrayList;
import java.util.List;

import criterio.Criterio;
import envio.MetodoDeEnvio;
import metodoDePago.MetodoDePago;
import pedido.Pedido;
import producto.Producto;

public class Ecommerce {

	private List<Producto> catalogo = new ArrayList<>();
	private List<Pedido> pedidos = new ArrayList<>();
	
	
	
	public List<Producto> buscarProducto (Criterio criterio){
		return criterio.filtrar(this.catalogo);
	}
	
	public void crearPedido(String direccion, List<Producto> productos, MetodoDeEnvio metodoEnvio,
							MetodoDePago metodoPago) {
		Pedido nuevoPedido = new Pedido(direccion,productos,metodoEnvio,metodoPago);
		this.agregarPedido(nuevoPedido);
	}
	
	public void agregarPedido(Pedido p) {
		pedidos.add(p);
	}
	
	public void quitarPedido(Pedido p) {
		pedidos.remove(p);
	}
	
	public void agregarProductoACatalogo(Producto p) {
		catalogo.add(p);
	}
	
	public void quitarProductoDeCatalogo(Producto p) {
		catalogo.remove(p);
	}
	
}
