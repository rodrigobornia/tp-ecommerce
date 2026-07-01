package visitor;

import java.util.List;

public interface ExportadorReporte {
	//interfaz Strategy para los formatos
	String exportar(List<ItemReporteDTO> datos);
}
