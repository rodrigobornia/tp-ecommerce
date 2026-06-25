package envio;

import java.time.LocalDate;

import pedido.Pedido;

public abstract class MetodoDeEnvio {
	 
	public abstract double calcularCostoEnvio(Pedido pedido);

	public abstract LocalDate calcularEntrega(Pedido pedido);
		
	
}
