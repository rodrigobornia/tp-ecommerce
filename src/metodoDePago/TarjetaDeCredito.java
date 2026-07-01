package metodoDePago;

import java.time.LocalDate;

public class TarjetaDeCredito extends MetodoDePago {
	private String numero;
	private String cvv;
	private LocalDate vencimiento;
	private ApiTarjetaCredito api;
	
	
	public TarjetaDeCredito (double monto, String numero, String cvv,
			LocalDate vencimiento, ApiTarjetaCredito api) {
		super(monto);
		this.numero = numero;
		this.cvv = cvv;
		this.vencimiento = vencimiento;
		this.api = api;
	}
	
	@Override
	protected void validarDatos() {
        if (!api.validarTarjeta(
                numero,
                cvv,
                vencimiento)) {

            throw new RuntimeException("Tarjeta inválida");
        }
	}
	@Override
	protected void reservarFondos() {
		api.preAutorizar(monto);
		
	}
	@Override
	protected void ejecutarTransaccion() {
		codigoTransaccion = api.transferir(monto);
	}
	@Override
	protected void notificarResultado() {
		super.notificarResultado();
        System.out.println("Cupón generado");	
	}

	public String getNumero() {
		return numero;
	}

	public String getCvv() {
		return cvv;
	}

	public LocalDate getVencimiento() {
		return vencimiento;
	}

	public ApiTarjetaCredito getApi() {
		return api;
	}
	
}