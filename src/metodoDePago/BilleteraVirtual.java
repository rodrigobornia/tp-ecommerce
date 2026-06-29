package metodoDePago;

import java.util.Map;

public class BilleteraVirtual extends MetodoDePago {
    
    public interface APIBilleteraVirtual {
        boolean verificarSaldo(String idUsuario, double monto);
        String bloquearSaldo(String idUsuario, double monto);
        String acreditarFondos(String idVendedor, double monto);
        void enviarNotificacionPush(String mensaje);
    }
    
    private APIBilleteraVirtual api;
    
    public BilleteraVirtual(APIBilleteraVirtual api) {
        this.api = api;
    }
    
    @Override
    protected void validar(Map<String, Object> datos) throws Exception {
        String idUsuario = (String) datos.get("idUsuario");
        double monto = (double) datos.get("monto");
        
        if (!api.verificarSaldo(idUsuario, monto)) {
            throw new Exception("Saldo insuficiente en la billetera virtual");
        }
    }
    
    @Override
    protected String reservar(double monto, Map<String, Object> datos) throws Exception {
        String idUsuario = (String) datos.get("idUsuario");
        String bloqueoId = api.bloquearSaldo(idUsuario, monto);
        
        if (bloqueoId == null || bloqueoId.isEmpty()) {
            throw new Exception("No se pudo bloquear el saldo");
        }
        return bloqueoId;
    }
    
    @Override
    protected String ejecutar(double monto, Map<String, Object> datos, String reservaId) throws Exception {
        String idVendedor = (String) datos.get("idVendedor");
        String transaccionId = api.acreditarFondos(idVendedor, monto);
        
        if (transaccionId == null || transaccionId.isEmpty()) {
            throw new Exception("Error al acreditar fondos al vendedor");
        }
        return transaccionId;
    }
    
    @Override
    protected void notificar(String transaccionId) {
        
        api.enviarNotificacionPush("Pago realizado con éxito. ID: " + transaccionId);
    }
    
}
