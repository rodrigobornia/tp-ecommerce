package visitor;

public class ItemReporteDTO {
	private String nombreItem;
    private int unidadesVendidas;
    private double precioPromedioCobrado;
//transporta los datos ya calculados
    public ItemReporteDTO(String nombreItem, int unidadesVendidas, double precioPromedioCobrado) {
        this.nombreItem = nombreItem;
        this.unidadesVendidas = unidadesVendidas;
        this.precioPromedioCobrado = precioPromedioCobrado;
    }

    public String getNombreItem() {
    	return nombreItem;
    }
    public int getUnidadesVendidas() { 
    	return unidadesVendidas;
    }
    public double getPrecioPromedioCobrado() { 
    	return precioPromedioCobrado; 
    }

}
