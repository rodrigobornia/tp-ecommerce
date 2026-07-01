package visitor;
import java.util.List;

public class ExportadorHTML implements ExportadorReporte {
    @Override
    public String exportar(List<ItemReporteDTO> datos) {
        StringBuilder sb = new StringBuilder();
        sb.append("<!DOCTYPE html>\n<html>\n<head><title>Reporte</title></head>\n<body>\n");
        sb.append("<h1>Productos Más Vendidos</h1>\n<table border='1'>\n");
        sb.append("<tr><th>Ítem</th><th>Unidades</th><th>Precio Prom.</th></tr>\n");

        for (ItemReporteDTO item : datos) {
            sb.append("<tr>")
              .append("<td>").append(item.getNombreItem()).append("</td>")
              .append("<td>").append(item.getUnidadesVendidas()).append("</td>")
              .append("<td>$").append(String.format("%.2f", item.getPrecioPromedioCobrado())).append("</td>")
              .append("</tr>\n");
        }
        sb.append("</table>\n</body>\n</html>");
        return sb.toString();
    }
}