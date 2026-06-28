package metodoDePago;

import java.util.Map;

public class TransferenciaBancaria extends MetodoDePago {
    
    public interface APITB {
        boolean validarCBU(String cbu, String alias);
        String ejecutarTransferencia(double monto, String cbuOrigen, String cbuDestino);
        boolean esTransferenciaProgramada();
    }
    
    private APITB api;
    
    public TransferenciaBancaria(APITB api) {
        this.api = api;
    }
    
    @Override
    protected void validar(Map<String, Object> datos) throws Exception {
        String cbuOrigen = (String) datos.get("cbuOrigen");
        String alias = (String) datos.get("alias");
        
        if (!api.validarCBU(cbuOrigen, alias)) {
            throw new Exception("CBU o alias inválido");
        }
    }
    
    @Override
    protected String reservar(double monto, Map<String, Object> datos) throws Exception {
        return "NO_RESERVA";
    }
    
    @Override
    protected String ejecutar(double monto, Map<String, Object> datos, String reservaId) throws Exception {
        String cbuOrigen = (String) datos.get("cbuOrigen");
        String cbuDestino = (String) datos.get("cbuDestino");
        
        String transaccionId = api.ejecutarTransferencia(monto, cbuOrigen, cbuDestino);
        
        if (transaccionId == null || transaccionId.isEmpty()) {
            throw new Exception("Error al ejecutar la transferencia");
        }
        return transaccionId;
    }
}