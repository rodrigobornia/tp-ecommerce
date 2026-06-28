package visitor;

import producto.Paquete;

import producto.ProductoBase;


public class ReporteCSVVisitor implements ReporteVisitor {
    private StringBuilder sb;

    public ReporteCSVVisitor() {
        this.sb = new StringBuilder();
        // Cabecera del archivo CSV
        this.sb.append("Tipo,Nombre,Categoria,PrecioBase,PrecioFinal\n");
    }

    @Override
    public void visitProductoBase(ProductoBase producto) {
        sb.append("Producto,")
          .append(producto.getNombre()).append(",")
          .append(producto.getCategoria()).append(",")
          .append(producto.getPrecioBase()).append(",")
          .append(producto.precioFinal()).append("\n");
    }

    @Override
    public void visitPaquete(Paquete paquete) {
        sb.append("Paquete,")
          .append(paquete.getNombre()).append(",")
          .append(paquete.getCategoria()).append(",")
          .append(paquete.getPrecioBase()).append(",")
          .append(paquete.precioFinal()).append("\n");
        
       
    }

    @Override
    public String getReporteGenerado() {
        return sb.toString();
    }
}