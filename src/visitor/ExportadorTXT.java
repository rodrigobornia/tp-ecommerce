package visitor;

import java.util.List;

public class ExportadorTXT implements ExportadorReporte {
    @Override
    public String exportar(List<ItemReporteDTO> datos) {
        StringBuilder sb = new StringBuilder();
        sb.append("=== REPORTE DE VENTAS ===\n\n");

        for (ItemReporteDTO item : datos) {
            sb.append("- ").append(item.getNombreItem()).append("\n")
              .append("  Unidades: ").append(item.getUnidadesVendidas()).append("\n")
              .append("  Precio Promedio: $").append(String.format("%.2f", item.getPrecioPromedioCobrado())).append("\n")
              .append("------------------------\n");
        }
        return sb.toString();
    }
}