package visitor;

import producto.ProductoBase;
import producto.Paquete;

public class ReporteHTMLVisitor implements ReporteVisitor {
    private StringBuilder sb;

    public ReporteHTMLVisitor() {
        this.sb = new StringBuilder();
        // Inicio del documento HTML
        this.sb.append("<html><body><h1>Reporte de Catálogo</h1>\n<ul>\n");
    }

    @Override
    public void visitProductoBase(ProductoBase producto) {
        sb.append("  <li><b>Producto:</b> ").append(producto.getNombre())
          .append(" | <i>Categoria:</i> ").append(producto.getCategoria())
          .append(" | Precio: $").append(producto.precioFinal())
          .append("</li>\n");
    }

    @Override
    public void visitPaquete(Paquete paquete) {
        sb.append("  <li><b>Paquete:</b> ").append(paquete.getNombre())
          .append(" | Precio Combo: $").append(paquete.precioFinal())
          .append("</li>\n");
          
        
    }

    @Override
    public String getReporteGenerado() {
        // Cierre del documento
        return sb.toString() + "</ul>\n</body></html>";
    }
}