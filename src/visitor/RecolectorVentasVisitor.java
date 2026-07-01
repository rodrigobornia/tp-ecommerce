package visitor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import producto.Paquete;
import producto.Producto;
import producto.ProductoBase;
import pedido.Pedido;
public class RecolectorVentasVisitor implements ReporteVisitor {
	private List<Pedido> historialPedidos;
    private LocalDate inicio;
    private LocalDate fin;
    private List<ItemReporteDTO> resultados;

    public RecolectorVentasVisitor(List<Pedido> historialPedidos, LocalDate inicio, LocalDate fin) {
        this.historialPedidos = historialPedidos;
        this.inicio = inicio;
        this.fin = fin;
        this.resultados = new ArrayList<>();
    }

    @Override
    public void visitProductoBase(ProductoBase productoBase) {
        calcularEstadisticas(productoBase);
    }

    @Override
    public void visitPaquete(Paquete paquete) {
        calcularEstadisticas(paquete);
    }

    private void calcularEstadisticas(Producto itemCatalogo) {
        int totalUnidades = 0;
        double ingresosTotales = 0.0;

        for (Pedido pedido : historialPedidos) {
            LocalDate fechaPedido = pedido.getFecha();
            
            // Verificamos rango de fechas inclusivo
            if ((fechaPedido.isEqual(inicio) || fechaPedido.isAfter(inicio)) && 
                (fechaPedido.isEqual(fin) || fechaPedido.isBefore(fin))) {
                
                // Buscamos cuántas veces aparece el producto en el pedido
                for (Producto productoVendido : pedido.getProductos()) {
                    if (productoVendido.getNombre().equals(itemCatalogo.getNombre())) {
                        totalUnidades++; 
                        ingresosTotales += productoVendido.precioFinal(); 
                    }
                }
            }
        }

        // Guardamos solo si se vendió al menos una vez en ese período
        if (totalUnidades > 0) {
            double precioPromedio = ingresosTotales / totalUnidades;
            resultados.add(new ItemReporteDTO(itemCatalogo.getNombre(), totalUnidades, precioPromedio));
        }
    }

    public List<ItemReporteDTO> getResultadosOrdenados() {
        // Ordenamiento descendente (de mayor a menor)
        resultados.sort((a, b) -> Integer.compare(b.getUnidadesVendidas(), a.getUnidadesVendidas()));
        return resultados;
    }
}