package visitor;
import java.util.List;

public class ExportadorCSV implements ExportadorReporte {
    @Override
    public String exportar(List<ItemReporteDTO> datos) {
        StringBuilder sb = new StringBuilder();
        sb.append("Nombre_Item,Unidades_Vendidas,Precio_Promedio\n");
        
        for (ItemReporteDTO item : datos) {
            sb.append(item.getNombreItem()).append(",")
              .append(item.getUnidadesVendidas()).append(",")
              .append(String.format("%.2f", item.getPrecioPromedioCobrado())).append("\n");
        }
        return sb.toString();
    }
}