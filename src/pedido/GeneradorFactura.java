package pedido;

public class GeneradorFactura implements ObservadorPedido {

	@Override
	public void notificar(EstadoDePedido anterior, EstadoDePedido nuevo, Pedido p) {
		// TODO Auto-generated method stub
		 if(nuevo instanceof Entregado ) {
	            System.out.println("comprobante fiscal. el pedido "+ p +" cambio su estado de "+ anterior 
	            		+" a"+nuevo);
	        }
	}



	
}
