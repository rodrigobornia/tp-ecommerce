package pedido;

import producto.Producto;

public abstract class EstadoDePedido {
	
	protected void operacionInvalida() {
		throw new RuntimeException("Operacion no permitida en este estado de pedido");
	}
	
	public boolean estaConfirmado() {
		return false;
	}
	
	public boolean estaEnviado() {
		return false;
	}
	
	public boolean estaCancelado() {
		return false;
	}
	
	public boolean estaEntregado() {
		return false;
	}

	public void agregarItem(Pedido pe, Producto p) {
		this.operacionInvalida();
	}
	
	public void quitarItem(Pedido pe, Producto p) {
		this.operacionInvalida();
	}

	public void confirmar(Pedido pe) {
		this.operacionInvalida();
	}
	
	public void preparar(Pedido pe) {
		this.operacionInvalida();
	}
	
	public void enviar(Pedido pe) {
		this.operacionInvalida();
	}
	
	public void entregar(Pedido pe) {
		this.operacionInvalida();
	}
	
	public void cancelar(Pedido pe) {
		this.operacionInvalida();
	}
	
}
