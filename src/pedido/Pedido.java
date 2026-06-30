package pedido;

import java.util.ArrayList;
import java.util.List;

import producto.Producto;

public class Pedido {
	
	private List <ObservadorPedido> observers = new ArrayList<>(); 
	private EstadoDePedido estado = new Borrador();
	List<Producto> productos = new ArrayList<>();

	
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
		
	}

	public void incrementarStock() {
		// TODO Auto-generated method stub
		
	}


	public void reembolsarCostoEnvio() {
		// TODO Auto-generated method stub
		
	}
	
	public void reembolsarCostoProductos() {
		
	}

	public void preparar() {
		EstadoDePedido anterior = this.estado;
		estado.preparar(this);
		this.notificarObservers(anterior, this.estado);
	}

	public EstadoDePedido getEstado() {
		// TODO Auto-generated method stub
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
		// TODO Auto-generated method stub
		return null;
	}

	public String getDireccionDestino() {
		// TODO Auto-generated method stub
		return null;
	}

	public double getPesoTotal() {
		// TODO Auto-generated method stub
		return  null;
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

}
