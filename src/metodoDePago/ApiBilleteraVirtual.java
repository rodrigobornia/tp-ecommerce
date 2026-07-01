package metodoDePago;

public interface ApiBilleteraVirtual {

	boolean tieneSaldo(double monto);

    void bloquearSaldo(double monto);

    String acreditar(double monto);
}
