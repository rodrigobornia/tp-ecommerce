package pedido;

public interface ObservadorPedido {

	public void notificar(EstadoDePedido anterior, EstadoDePedido nuevo, Pedido p);
}
