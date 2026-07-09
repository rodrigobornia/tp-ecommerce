package pedido;

public class Fidelizacion implements ObservadorPedido{

	@Override
	public void notificar(EstadoDePedido anterior, EstadoDePedido nuevo, Pedido p) {
		// TODO Auto-generated method stub
		if(nuevo.estaCancelado()) {
            System.out.println("cupon de descuento del 5%. El pedido "+p+" cambio su estado de "+anterior+" a "+nuevo);

        }
	}

	
}
