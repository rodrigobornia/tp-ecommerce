package visitor;

import producto.Paquete;
import producto.ProductoBase;

public interface ReporteVisitor {

	public void visitProductoBase(ProductoBase productoBase);
	public void visitPaquete (Paquete paquete);

}
