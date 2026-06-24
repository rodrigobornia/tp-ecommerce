package producto;

import java.util.ArrayList;
import java.util.List;

import visitor.ReporteVisitor;

public class Paquete extends Producto {
	private int descuento;
	private List<Producto> productos = new ArrayList<>(); 
	
	
	
	public Paquete(String nombre, String descripcion, int descuento,double precio, Integer stock) {
	    super(nombre, descripcion,precio, stock); // <-- Pasamos los datos al padre
	    this.descuento = descuento;
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
	public int getDescuento() {
		return descuento;
	}

	public List<Producto> getProductos() {
		return productos;
	}

	
}
