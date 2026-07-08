package ecommerce;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import criterio.Criterio;
import envio.MetodoDeEnvio;
import metodoDePago.MetodoDePago;
import pedido.Pedido;
import producto.Producto;

import visitor.GeneradorReporteVentas;

import visitor.ReporteVisitor;

public class Ecommerce {

	private List<Producto> catalogo = new ArrayList<>();
	private List<Pedido> pedidos = new ArrayList<>();
	
	GeneradorReporteVentas generador = new GeneradorReporteVentas();
	
	public Ecommerce() {
		super();
	}

	public List<Producto> buscarProducto (Criterio criterio){
		return criterio.filtrar(this.catalogo);
	}

	public void crearPedido(String direccion, List<Producto> productos, MetodoDeEnvio metodoEnvio,
							MetodoDePago metodoPago) {
		Pedido nuevoPedido = new Pedido(direccion,productos,metodoEnvio,metodoPago);
		this.agregarPedido(nuevoPedido);
	}
	// =======================================================
	// GESTIÓN DEL CATÁLOGO Y PEDIDOS
	// =======================================================
	
	
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
	public String generarReporte(LocalDate desde, LocalDate hasta, ReporteVisitor visitor) {
       
        
 
        return generador.generar(this.pedidos, this.catalogo, desde, hasta, visitor);
    }
} 

