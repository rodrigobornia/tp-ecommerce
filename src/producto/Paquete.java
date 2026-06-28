package producto;

import java.util.ArrayList;
import java.util.List;

import visitor.ReporteVisitor;

public class Paquete extends Producto {

	private List<Producto> productos = new ArrayList<>(); 
	
	
	public Paquete(String nombre, String descripcion, String categoria, double precio, int descuento, int stock) {
	    
	    super(nombre, descripcion, categoria, precio, descuento, stock); 
	}
	@Override
	public void accept(ReporteVisitor visitor) {
		visitor.visitPaquete(this);
	}
	
	@Override
	public double getPrecioBase() {
		return this.productos.stream()
									 .mapToDouble(p -> p.getPrecioBase())
									 .sum();
	}
	@Override
	public Double precioFinal() {
		return this.getPrecioBase() * (1 - this.getDescuento()/ 100.0);
		
	}
	public List<Producto> getProductos() {
		return productos;
	}

	
}
