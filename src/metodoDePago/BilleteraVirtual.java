package metodoDePago;


public class BilleteraVirtual extends MetodoDePago {
    
	private ApiBilleteraVirtual api;

    public BilleteraVirtual(double monto, ApiBilleteraVirtual api) {
        super(monto);
        this.setApi(api);
        ;
    }

    @Override
    protected void validarDatos() {
        if (!api.tieneSaldo(monto)) {
            throw new RuntimeException("Saldo insuficiente");
        }
    }

    @Override
    protected void reservarFondos() {
        api.bloquearSaldo(monto);
    }

    @Override
    protected void ejecutarTransaccion() {
        codigoTransaccion = api.acreditar(monto);
    }
	public ApiBilleteraVirtual getApi() {
		return api;
	}

	public void setApi(ApiBilleteraVirtual api) {
		this.api = api;
	}

    // Usa la implementación por defecto de notificarResultado()
}
