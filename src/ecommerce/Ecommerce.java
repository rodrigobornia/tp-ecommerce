package ecommerce;

import java.util.ArrayList;
import java.util.List;

import pedido.Pedido;
import producto.Producto;
import visitor.ExportadorReporte;
import visitor.ItemReporteDTO;
import visitor.RecolectorVentasVisitor;
import visitor.ReporteVisitor;

public class Ecommerce {
    
    // Listas principales del sistema
    private List<Producto> productos;
    private List<Pedido> historialPedidos;

    public Ecommerce() {
        this.productos = new ArrayList<>();
        this.historialPedidos = new ArrayList<>();
    }

    // =======================================================
    // GESTIÓN DEL CATÁLOGO Y PEDIDOS
    // =======================================================
    
    public void agregarProducto(Producto producto) {
        this.productos.add(producto);
    }

    public void registrarPedido(Pedido pedido) {
        this.historialPedidos.add(pedido);
    }

    public List<Producto> getProductos() {
        return productos;
    }

    public List<Pedido> getHistorialPedidos() {
        return historialPedidos;
    }

    // =======================================================
    // GENERACIÓN DE REPORTES (Visitor + Strategy)
    // =======================================================
    
    /**
     * Genera un reporte aplicando el patrón Visitor para la recolección 
     * y el patrón Strategy para la exportación.
     */
    public String generarReporte(ReporteVisitor visitor, ExportadorReporte exportador) {
        
        // 1. Double Dispatch: El E-Commerce le pasa el visitor a cada elemento del catálogo
        for (Producto item : this.productos) {
            item.accept(visitor);
        }
        
        // 2. Extracción de datos: Verificamos la instancia concreta para obtener los DTOs
        if (visitor instanceof RecolectorVentasVisitor) {
            RecolectorVentasVisitor recolector = (RecolectorVentasVisitor) visitor;
            List<ItemReporteDTO> datosListos = recolector.getResultadosOrdenados();
            
            // 3. Strategy: Delegamos la responsabilidad del formato al exportador elegido
            return exportador.exportar(datosListos);
        }
        
        // Retorno por defecto de seguridad
        return "El reporte no pudo ser generado con el formato solicitado.";
    }
}