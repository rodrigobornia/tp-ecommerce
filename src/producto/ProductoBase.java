package producto;

import java.util.ArrayList;

import visitor.ReporteVisitor;

public class ProductoBase extends Producto {
	private int SKU;		
	private String marca;
	private String categoria;
	private double peso;
	private ArrayList<AtributoDinamico> atributos = new ArrayList<AtributoDinamico>();
	
	
	
	
	public ProductoBase(int SKU, String nombre, String descripcion,
            String marca, String categoria, Double precio, Integer stock) {
	super(nombre, descripcion, precio, stock); // <-- Pasamos los datos al padre
	this.SKU = SKU;
	this.marca = marca;
	this.categoria = categoria;
}

	@Override 
	public Double precioFinal() {
		return this.getPrecioBase();
	}
	@Override
	public void accept(ReporteVisitor visitor) {
		visitor.visitProductoBase(this);
	}
/*	VER ESTOOO
 * public boolean esValido() {

        if (this.SKU <= 0) return false; 
        if (this.getNombre() == null || this.getNombre().isEmpty()) return false;
        
     
        return true; // Si pasó todos los controles, el producto es válido
    }
*/
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

	public String getCategoria() {
		return categoria;
	}

	public double getPeso() {
		return peso;
	}

	public ArrayList<AtributoDinamico> getAtributos() {
		return atributos;
	}

}
