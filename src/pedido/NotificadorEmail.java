package pedido;

public class NotificadorEmail implements ObservadorPedido {
	
	public interface MailSender {
		void enviarMail(String direcciónDestino,String título,String mensaje,String adjunto);
    }
    
	private MailSender mailer;	
    
    
    public NotificadorEmail(MailSender mailer) {
        this.mailer = mailer;
    }


	@Override
	public void notificar(EstadoDePedido anterior, EstadoDePedido nuevo, Pedido p) {
		// TODO Auto-generated method stub
		if (this.transicionValida(nuevo)) {
			mailer.enviarMail("","","","");
		}
	}


	private boolean transicionValida(EstadoDePedido nuevo) {
		// TODO Auto-generated method stub
		return nuevo.estaConfirmado() || nuevo.estaEnviado() || nuevo.estaEntregado();
	}

}
