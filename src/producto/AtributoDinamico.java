package producto;

public class AtributoDinamico {
	private String nombre;
	private String descripcion;
	
	
	public AtributoDinamico(String nombre, String descripcion) {
		super();
		this.nombre = nombre;
		this.descripcion = descripcion;
	}
	public String getNombre() {
		return nombre;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public boolean esValido() {
	    return (this.nombre != null && !this.nombre.isBlank()) &&
	           (this.descripcion != null && !this.descripcion.isBlank());
	}

}
