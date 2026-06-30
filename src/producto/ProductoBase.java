package producto;

import java.util.ArrayList;

import visitor.ReporteVisitor;

public class ProductoBase extends Producto {
	private int SKU;		
	private String marca;
	
	private ArrayList<AtributoDinamico> atributos = new ArrayList<AtributoDinamico>();
	
	
	
	
	public ProductoBase(int SKU, String nombre, String descripcion,String marca, String categoria, Double precio, int descuento,int stock) {
	super(nombre, descripcion, categoria, precio, descuento ,stock); // <-- Pasamos los datos al padre
	this.SKU = SKU;
	this.marca = marca;
}

	@Override 
	public Double precioFinal() {
		return this.getPrecioBase() * (1 - this.getDescuento() / 100); 
	}
	@Override
	public void accept(ReporteVisitor visitor) {
		visitor.visitProductoBase(this);
	}

	public void validarItem() {
		if (!esItemValido()) {
			throw new RuntimeException("No es un producto válido");
		}
	}
	public Boolean esItemValido() {
	    return (this.SKU > 0)                                                   &&   
	           (this.getNombre() != null && !this.getNombre().isBlank())   		&&
	           (this.atributos != null)                                         &&
	           (this.atributos.stream().allMatch(AtributoDinamico::esValido));
	         
	}

	public void agregarAtributo(String nombre, String descripcion) {
		this.atributos.add(new AtributoDinamico(nombre, descripcion));
	}
	
	
    //GETTERS----------------------------------------------------
	public int getSKU() {
		return SKU;
	}

	public String getMarca() {
		return marca;
	}
	

	public ArrayList<AtributoDinamico> getAtributos() {
		return atributos;
	}

}
