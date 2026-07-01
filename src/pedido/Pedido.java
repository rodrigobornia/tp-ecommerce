package pedido;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import envio.MetodoDeEnvio;
import metodoDePago.MetodoDePago;
import producto.Producto;

public class Pedido {
	private String direccionDestino;
	private List <ObservadorPedido> observers = new ArrayList<>(); 
	private EstadoDePedido estado = new Borrador();

    List<Producto> productos = new ArrayList<>();
	private MetodoDeEnvio metodoEnvio;
	private MetodoDePago metodoPago;

	private LocalDate fecha ;

	 
	public Pedido(String direccionDestino, List<Producto> productos, MetodoDeEnvio metodoEnvio,
					MetodoDePago metodoPago) {
		this.direccionDestino = direccionDestino;
		this.productos = productos;
		this.metodoEnvio = metodoEnvio;
		this.metodoPago = metodoPago;
	}
	
	public void procesarPago() {
		this.metodoPago.setMonto(this.getMontoTotal());
		this.metodoPago.procesarPago();
	}
	
	private double getMontoTotal() {
		// TODO Auto-generated method stub
		return productos.stream().mapToDouble(p ->p.precioFinal()).sum();
	}

	public void setMetodoPago(MetodoDePago metodoPago) {
		this.metodoPago = metodoPago;
	}
	
	public void agregarItem(Producto p) {
		estado.agregarItem(this, p);
	}
	
	public void quitarItem(Producto p) {
		estado.quitarItem(this, p);
	}
	
	protected void agregarProducto(Producto p) {
		// TODO Auto-generated method stub
		productos.add(p);
	}

	protected void quitarProducto(Producto p) {
		// TODO Auto-generated method stub
		productos.remove(p);
	}
	
	protected void setEstado(EstadoDePedido e) {
		this.estado = e;
	}

	public void decrementarStock() {
		// TODO Auto-generated method stub
		productos.stream().forEach(p -> p.setStock(p.getStock() -1));
	}

	public void incrementarStock() {
		// TODO Auto-generated method stub
		productos.stream().forEach(p -> p.setStock(p.getStock() +1));
	}


	public void reembolsarCostoEnvio() {
		// A quien hay que reembolsar? 
	}
	
	public void reembolsarCostoProductos() {
		// A quien hay que reembolsar? 
	}

	public void preparar() {
		EstadoDePedido anterior = this.estado;
		estado.preparar(this);
		this.notificarObservers(anterior, this.estado);
	}

	public EstadoDePedido getEstado() {
		
		return this.estado;
	}

	public void cancelar() {
		EstadoDePedido anterior = this.estado;
		estado.cancelar(this);
		this.notificarObservers(anterior, this.estado);
	}

	public void confirmar() {
		EstadoDePedido anterior = this.estado;
		estado.confirmar(this);
		this.notificarObservers(anterior, this.estado);
	}

	public void enviar() {
		EstadoDePedido anterior = this.estado;
		estado.enviar(this);
		this.notificarObservers(anterior, this.estado);
	}

	public void entregar() {
		EstadoDePedido anterior = this.estado;
		estado.entregar(this);
		this.notificarObservers(anterior, this.estado);
	}

	public Double valorTotal() {
		return this.productos.stream()
                .mapToDouble(Producto::precioFinal)
                .sum();
	}

	
	public String getDireccionDestino() {
		return direccionDestino;
	}

	public double getPesoTotal() {
		
		   return this.productos.stream().mapToDouble(Producto::getPeso).sum();
	} 
	
	public void notificarObservers(EstadoDePedido anterior, EstadoDePedido nuevo) {
		observers.forEach(o -> o.notificar(anterior, nuevo, this));
    }
    public void agregarObserver(ObservadorPedido obs) {
        observers.add(obs);
    }
    public void quitarObserver(ObservadorPedido obs) {
        observers.remove(obs);
    }

	public LocalDate getFecha() {
		return fecha;
	}

	public List<Producto> getProductos() {
		return productos;
	}

}
