package envio;

import java.time.LocalDate;

import pedido.Pedido;

public class RetiroSucursal extends MetodoDeEnvio{
	private Sucursal sucursalSeleccionada;
	@Override
	public double calcularCostoEnvio(Pedido pedido) {
		return 0.0;
	}
	@Override
    public LocalDate calcularEntrega(Pedido pedido) {
        // "inmediato si hay stock local, hasta 3 días si requiere traslado interno"
        if (this.sucursalSeleccionada.tieneStockDePedido(pedido)) {
            return LocalDate.now(); 
        } else {
            return LocalDate.now().plusDays(3); // 3 días de traslado
        }
    }
}
  