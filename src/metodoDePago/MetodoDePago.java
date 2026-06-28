package metodoDePago;

import java.util.Map;

public abstract class MetodoDePago {
    
    public final String procesarPago(double monto, Map<String, Object> datos) {
        try {
            validar(datos);
            String reservaId = reservar(monto, datos);
            String transaccionId = ejecutar(monto, datos, reservaId);
            notificar(transaccionId);
            return "EXITO:" + transaccionId;
        } catch (Exception e) {
            return "ERROR:" + e.getMessage();
        }
    }
    
    protected abstract void validar(Map<String, Object> datos) throws Exception;
    protected abstract String reservar(double monto, Map<String, Object> datos) throws Exception;
    protected abstract String ejecutar(double monto, Map<String, Object> datos, String reservaId) throws Exception;
    protected void notificar(String transaccionId) { }
}
