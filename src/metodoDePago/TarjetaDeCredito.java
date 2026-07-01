package metodoDePago;

import java.util.Map;

public class TarjetaDeCredito extends MetodoDePago {
    
    public interface APITarjetaCredito {
        boolean validarTarjeta(String numero, String cvv, String vencimiento);
        String preAutorizar(double monto, String numeroTarjeta);
        String ejecutarPago(double monto, String numeroTarjeta);
    }
    
    private APITarjetaCredito api;
    
    public TarjetaDeCredito(APITarjetaCredito api) {
        this.api = api;
    }
    
    @Override
    protected void validar(Map<String, Object> datos) throws Exception {
        String numero = (String) datos.get("numero");
        String cvv = (String) datos.get("cvv");
        String vencimiento = (String) datos.get("vencimiento");
        
        if (!api.validarTarjeta(numero, cvv, vencimiento)) {
            throw new Exception("Tarjeta de crédito inválida");
        }
    }
    
    @Override
    protected String reservar(double monto, Map<String, Object> datos) throws Exception {
        String numero = (String) datos.get("numero");
        String autorizacionId = api.preAutorizar(monto, numero);
        
        if (autorizacionId == null || autorizacionId.isEmpty()) {
            throw new Exception("No se pudo pre-autorizar la tarjeta");
        }
        return autorizacionId;
    }
    
    @Override
    protected String ejecutar(double monto, Map<String, Object> datos, String reservaId) throws Exception {
        String numero = (String) datos.get("numero");
        String transaccionId = api.ejecutarPago(monto, numero);
        
        if (transaccionId == null || transaccionId.isEmpty()) {
            throw new Exception("Error al ejecutar el pago con tarjeta");
        }
        return transaccionId;
    }


}