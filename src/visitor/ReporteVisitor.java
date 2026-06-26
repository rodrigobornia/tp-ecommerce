package visitor;

import producto.Paquete;
import producto.Producto;

public interface ReporteVisitor {

	public void visitProductoBase(Producto productoBase);
	public void visitPaquete (Paquete paquete);

}
