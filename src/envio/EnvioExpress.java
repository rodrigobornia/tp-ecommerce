package envio;

import java.time.LocalDate;

import pedido.Pedido;

public class EnvioExpress extends MetodoDeEnvio{
	
	private  LibEnvioExpress envioExpress;
	
	
	
	public EnvioExpress(LibEnvioExpress envioExpress) {
		super();
		this.envioExpress = envioExpress;
	}

	@Override 
	public  double calcularCostoEnvio(Pedido pedido) {
		return envioExpress.calcularCosto(pedido.valorTotal());
	}

	@Override
    public LocalDate calcularEntrega(Pedido pedido) {
        // "La entrega se garantiza en 1 día hábil"
        return LocalDate.now().plusDays(1);
    }
}
