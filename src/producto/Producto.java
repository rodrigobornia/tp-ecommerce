package producto;

import visitor.ReporteVisitor;

public abstract class Producto{
	private  String nombre;
	private  String descripcion;
	private  double precioBase;
	private double peso;
	private String categoria;
	private int descuento;
	//  venta 
	protected int unidadesVendidas = 0;
	protected double sumaPreciosCobrados = 0.0;


	public Producto(String nombre, String descripcion, String categoria, double precioBase,int descuento) {
	    this.nombre = nombre;
	    this.descripcion = descripcion;
	    this.precioBase = precioBase;
	    this.descuento = descuento;
	    this.categoria = categoria;
	}  
	
	public abstract Double precioFinal();
	
	
	public abstract void accept(ReporteVisitor visitor);
	
	public abstract int getStock();
	
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

	public String getCategoria() {
		return categoria;
	}

	public double getPeso() {
		return peso;
	}
	

	public int getUnidadesVendidas() {
	    return this.unidadesVendidas;
	}
		
	public double precioPromedio() {
	    if (this.unidadesVendidas == 0) return 0.0;
	    return this.sumaPreciosCobrados / this.unidadesVendidas;
	}	
	
	public void reiniciarVentas() {
	    this.unidadesVendidas = 0;
	    this.sumaPreciosCobrados = 0.0;
	}
	public void registrarVenta(int cantidad, double precioCobrado) {
	    this.unidadesVendidas += cantidad;
	    this.sumaPreciosCobrados += precioCobrado;
	}
}
