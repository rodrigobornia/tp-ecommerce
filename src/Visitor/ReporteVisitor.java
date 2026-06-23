package Visitor;

import Producto.Paquete;
import Producto.Producto;

public interface ReporteVisitor {

	public void visitProducto(Producto producto);
	public void visitPaquete (Paquete paquete);

}
