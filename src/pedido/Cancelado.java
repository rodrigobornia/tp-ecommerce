package pedido;

public class Cancelado extends EstadoDePedido{

	@Override
	public boolean estaCancelado() {
		return true;
	}
}
