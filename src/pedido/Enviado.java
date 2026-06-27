package pedido;

public class Enviado extends EstadoDePedido {

	@Override
	public void entregar(Pedido pe) {
		pe.setEstado(new Entregado());
	}
	
	@Override
	public void cancelar(Pedido pe) {
		pe.incrementarStock();
		pe.reembolsarCostoProductos();
		pe.setEstado(new Cancelado());
	}
}
