package metodoDePago;

import java.time.LocalDate;

public interface ApiTarjetaCredito {

	boolean validarTarjeta(
            String numero,
            String cvv,
            LocalDate vencimiento);

    void preAutorizar(double monto);

    String transferir(double monto);
}
