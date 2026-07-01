package ecommerce;

import java.util.ArrayList;
import java.util.List;

import criterio.Criterio;
import envio.MetodoDeEnvio;
import metodoDePago.MetodoDePago;
import pedido.Pedido;
import producto.Producto;
import visitor.ExportadorReporte;
import visitor.ItemReporteDTO;
import visitor.RecolectorVentasVisitor;
import visitor.ReporteVisitor;

public class Ecommerce {

	private List<Producto> catalogo = new ArrayList<>();
	private List<Pedido> pedidos = new ArrayList<>();
	
	
	
	public Ecommerce() {
		super();
	}

	public List<Producto> buscarProducto (Criterio criterio){
		return criterio.filtrar(this.catalogo);
	}

    // =======================================================
    // GESTIÓN DEL CATÁLOGO Y PEDIDOS
    // =======================================================
    
	public void crearPedido(String direccion, List<Producto> productos, MetodoDeEnvio metodoEnvio,
							MetodoDePago metodoPago) {
		Pedido nuevoPedido = new Pedido(direccion,productos,metodoEnvio,metodoPago);
		this.agregarPedido(nuevoPedido);
	}
	
	public void agregarPedido(Pedido p) {
		pedidos.add(p);
	}
	
	public void quitarPedido(Pedido p) {
		pedidos.remove(p);
	}
	
	public void agregarProductoACatalogo(Producto p) {
		catalogo.add(p);
	}
	
	public void quitarProductoDeCatalogo(Producto p) {
		catalogo.remove(p);
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
        for (Producto item : this.catalogo) {
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

