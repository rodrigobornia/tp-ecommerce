package visitor;

import producto.Paquete;
import producto.ProductoBase;



public class ReporteTxtVisitor implements ReporteVisitor {
    private StringBuilder sb;

    public ReporteTxtVisitor() {
        this.sb = new StringBuilder();
        this.sb.append("=== REPORTE DE CATÁLOGO ===\n\n");
    }

    @Override
    public void visitProductoBase(ProductoBase producto) {
        sb.append("[PRODUCTO INDIVIDUAL]\n")
          .append("Nombre: ").append(producto.getNombre()).append("\n")
          .append("Precio: $").append(producto.precioFinal()).append("\n")
          .append("---------------------------\n");
    }

    @Override
    public void visitPaquete(Paquete paquete) {
        sb.append("[PAQUETE PROMOCIONAL]\n")
          .append("Nombre: ").append(paquete.getNombre()).append("\n")
          .append("Precio Final: $").append(paquete.precioFinal()).append("\n")
          .append("---------------------------\n");
          
        // paquete.getProductos().forEach(p -> p.accept(this));
    }

    @Override
    public String getReporteGenerado() {
        return sb.toString();
    }
}