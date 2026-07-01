package producto;



import visitor.ReporteVisitor;
import visitor.Visitable;


public abstract class Producto implements Visitable {
	private  String nombre;
	private  String descripcion;
	private  double precioBase;
	private  int stock;
	private double peso;
	private String categoria;
	private int descuento;


	public Producto(String nombre, String descripcion, String categoria, double precioBase,int descuento, int stock) {
	    this.nombre = nombre;
	    this.descripcion = descripcion;
	    this.precioBase = precioBase;
	    this.descuento = descuento;
	    this.stock = stock;
	    this.categoria = categoria;
	}  
	
	public abstract Double precioFinal();
	
	
	public abstract void accept(ReporteVisitor visitor);
	
	
	
	//GETTERS----------------------------------------
	public int getDescuento() {
		return descuento;
	}
	
	public void setDescuento(int descuento) {
		this.descuento = descuento;
	}
	
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

	public double getPeso() {
		return peso;
	}

	public abstract void setStock(int i);	
	
	
		
		
 
}
