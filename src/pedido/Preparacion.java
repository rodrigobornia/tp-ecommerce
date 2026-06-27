package pedido;

public class Preparacion extends EstadoDePedido {

	@Override
	public void enviar(Pedido pe) {
		pe.setEstado(new Enviado());
	}
	
	@Override
	public void cancelar(Pedido pe) {
		pe.setEstado(new Cancelado());
		pe.incrementarStock();
		pe.reembolsarCostoProductos();
		pe.reembolsarCostoEnvio();
	}
}
