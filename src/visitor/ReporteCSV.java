package visitor;


import producto.Paquete;
import producto.ProductoBase;

public class ReporteCSV implements ReporteVisitor {
	 private StringBuilder sb = new StringBuilder();

	    public ReporteCSV() {
	        // Fila de encabezado del CSV.
	        sb.append("Nombre,UnidadesVendidas,PrecioPromedio\n");
	    }

	    @Override
	    public void visitProductoBase(ProductoBase producto) {
	    	agregarLinea(producto.getNombre(),
	                    producto.getUnidadesVendidas(),
	                    producto.precioPromedio());
	    }

	    @Override
	    public void visitPaquete(Paquete paquete) {
	    	agregarLinea(paquete.getNombre(),
	                    paquete.getUnidadesVendidas(),
	                    paquete.precioPromedio());
	    }

	    private void agregarLinea(String nombre, int unidades, double precioPromedio) {
	        sb.append(nombre).append(",")
	          .append(unidades).append(",")
	          .append(String.format(java.util.Locale.US, "%.2f", precioPromedio))
	          .append("\n");
	    }

	    @Override
	    public String obtenerResultado() {
	        return sb.toString();
	    }
	}