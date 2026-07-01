package metodoDePago;

public interface ApiTransferenciaBancaria {

	boolean validarCuenta(
            String cbu,
            String alias);

    String transferir(double monto);
}
