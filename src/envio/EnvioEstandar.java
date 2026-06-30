package envio;

import java.time.LocalDate;

import pedido.Pedido;

public class EnvioEstandar extends MetodoDeEnvio{
	private CorreoArgentino correoArgentino;

	
	public EnvioEstandar(CorreoArgentino correoArgentino) {
		super();
		this.correoArgentino = correoArgentino;
	}

	@Override
	public double calcularCostoEnvio(Pedido pedido) {
		return correoArgentino.estimarEnvio(pedido.getPesoTotal(), pedido.getDireccionDestino());
	}
	
	@Override
    public LocalDate calcularEntrega(Pedido pedido) {
        // El enunciado dice: "La estimación de días es fija entre 5 y 7 días hábiles"
        // Para simplificar, le sumamos 6 días corridos a la fecha actual
        return LocalDate.now().plusDays(6);
    }


	
}
