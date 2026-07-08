package visitor;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

import pedido.Entregado;
import pedido.Pedido;
import producto.Producto;

public class GeneradorReporteVentas {
	public String generar(List<Pedido> pedidos,
			List<Producto> items,
			LocalDate desde,
			LocalDate hasta,
			ReporteVisitor visitor) {

		// 1. Reiniciar las ventas acumuladas de todos los ítems.
		items.forEach(Producto::reiniciarVentas);

		// 2. Recorrer los pedidos ENTREGADOS dentro del período y registrar ventas.
		pedidos.stream()
		.filter(this::estaEntregado)
		.filter(pedido -> estaEnPeriodo(pedido, desde, hasta))
		.forEach(this::registrarVentasDelPedido);

		// 3. Quedarse solo con los ítems que se vendieron, ordenados por
		//    unidades vendidas (de mayor a menor).
		List<Producto> masVendidos = items.stream()
				.filter(item -> item.getUnidadesVendidas() > 0)
				.sorted(Comparator.comparingInt(Producto::getUnidadesVendidas).reversed())
				.toList();

		// 4. Disparar el Visitor sobre cada ítem (double dispatch).
		masVendidos.forEach(item -> item.accept(visitor));

		// 5. Devolver el resultado formateado.
		return visitor.obtenerResultado();
	}

	private boolean estaEntregado(Pedido pedido) {
	    
	    return pedido.getEstado() instanceof Entregado;
	}
	private boolean estaEnPeriodo(Pedido pedido, LocalDate desde, LocalDate hasta) {
		LocalDate fecha = pedido.getFecha();
		return fecha != null
				&& !fecha.isBefore(desde)
				&& !fecha.isAfter(hasta);
	}

	private void registrarVentasDelPedido(Pedido pedido) {
	    pedido.getProductos().forEach(p -> p.registrarVenta(1, p.precioFinal()));
	}
}
