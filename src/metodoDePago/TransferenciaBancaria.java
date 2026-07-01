package metodoDePago;


public class TransferenciaBancaria extends MetodoDePago {

    private String cbu;
    private String alias;
    private ApiTransferenciaBancaria api;

    public TransferenciaBancaria(
            double monto,
            String cbu,
            String alias,
            ApiTransferenciaBancaria api) {

        super(monto);
        this.setCbu(cbu);
        this.setAlias(alias);
        this.setApi(api);
    }
	@Override
    protected void validarDatos() {
        if (!api.validarCuenta(
                cbu,
                alias)) {
            throw new RuntimeException( "Cuenta inválida");
        }
    }
    @Override
    protected void reservarFondos() {}//Metodo hook 
    @Override
    protected void ejecutarTransaccion() {
        codigoTransaccion =
                api.transferir(monto);
    }
    @Override
    protected void notificarResultado() {

        super.notificarResultado();
        System.out.println("Comprobante CBU generado");
    }
    private void setApi(ApiTransferenciaBancaria api) {
    	this.api = api;
 	}
 	private void setAlias(String alias) {
 		this.alias = alias;
 	}
 	private void setCbu(String cbu) {
 		this.cbu = cbu;
 	}
    
 	public String getCbu() {
 		return cbu;
 	}
 	public String getAlias() {
 		return alias;
 	}
 	public ApiTransferenciaBancaria getApi() {
 		return api;
 	}
    
    
}