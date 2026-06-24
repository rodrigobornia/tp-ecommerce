package producto;

import visitor.ReporteVisitor;
import visitor.Visitable;

public abstract class Producto implements Visitable {
	private  String nombre;
	private  String descripcion;
	private  double precioBase;
	private  int stock;
	
	public Producto(String nombre, String descripcion, double precioBase, int stock) {
	    this.nombre = nombre;
	    this.descripcion = descripcion;
	    this.precioBase = precioBase;
	    this.stock = stock;
	}  
	
	public abstract Double precioFinal();
	
	
	public abstract void accept(ReporteVisitor visitor);
	
	
	
	//GETTERS----------------------------------------
	
	public String getNombre() {
		return nombre;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public double getPrecioBase() {
		return precioBase;
	}
	public int getStock() {
		return stock;
	}

	
		
		
 
}
