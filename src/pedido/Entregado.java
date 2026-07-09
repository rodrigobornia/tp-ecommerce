package pedido;

public class Entregado extends EstadoDePedido {

	@Override
	public boolean estaEntregado() {
		return true;
	}
}
