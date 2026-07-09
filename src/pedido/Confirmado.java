package pedido;

public class Confirmado extends EstadoDePedido{

	@Override
	public boolean estaConfirmado() {
		return true;
	}
	
	@Override
	public void preparar(Pedido pe) {
		pe.decrementarStock();
		pe.setEstado(new Preparacion());
	}
	
	@Override
	public void cancelar(Pedido pe) {
		pe.setEstado(new Cancelado());
	}
}
