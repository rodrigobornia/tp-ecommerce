package Producto;


import Visitor.ReporteVisitor;
import Visitor.Visitable;

public abstract class Producto implements Visitable {
	private  String nombre;
	private  String descripcion;
	private  double precioBase;
	private  int stock;
	private String categoria;
	
	public Producto(String nombre, String descripcion, String categoria, double precioBase, int stock) {
	    this.nombre = nombre;
	    this.descripcion = descripcion;
	    this.precioBase = precioBase;
	    this.stock = stock;
	    this.categoria = categoria;
	}  
	
	public abstract Double precioFinal();
	
	@Override
	public void accept(ReporteVisitor visitor) {
		visitor.visitProducto(this);
	}
	
	
	
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

	public String getCategoria() {
		return categoria;
	}

	
		
		
 
}
