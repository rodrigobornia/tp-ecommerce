package Producto;

import java.util.ArrayList;
import java.util.List;

import Visitor.ReporteVisitor;

public class Paquete extends Producto {
	private int descuento;
	private List<Producto> productos = new ArrayList<>(); 
	
	
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
}
