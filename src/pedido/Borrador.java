package pedido;

import producto.Producto;

public class Borrador extends EstadoDePedido{

	@Override
	public void agregarItem(Pedido pe, Producto p) {
		pe.agregarProducto(p);
	}
	
	@Override
	public void quitarItem(Pedido pe, Producto p) {
		pe.quitarProducto(p);
	}
	
	@Override
	public void confirmar(Pedido p) {
		p.procesarPago();
		p.setEstado(new Confirmado());
	}
	
	@Override
	public void cancelar(Pedido p) {
		p.setEstado(new Cancelado());
	}
}
