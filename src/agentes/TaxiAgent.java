package agentes;

import jade.core.AID;
import jade.lang.acl.ACLMessage;

public class TaxiAgent extends CyclicAgent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5003136961225347902L;

	private Posicao posicao;
	
	private Movimento cliente;
	
	private boolean levandoCliente;

	public TaxiAgent() {
		super(TYPE_TAXI);
		this.posicao = Posicao.random();
	}

	@Override
	public long getSleepTime() {
		return 500;
	}
	
	@Override
	public void action() throws Exception {
		
		ACLMessage message = receive();
		if ( message != null ) {
			switch( message.getPerformative() ) {
			case Messages.SEND_TAXI_TO_CLIENT :
				log( "Indo buscar o cliente em -> %s", message );
				this.cliente = ( Movimento ) message.getContentObject();
			}
		}
		
		if ( this.cliente != null ) {
			this.moverParaCliente();
		} else {
			this.movimentoRandomico();
		}
		
	}

	public Posicao getPosicao() {
		return posicao;
	}

	private void moverParaCliente() {
		if ( this.levandoCliente ) {
			Posicao p = this.posicao.melhorMovimentoPara( this.cliente.getDestino() );
			
			if ( p.equals( this.cliente.getDestino() ) ) {
				sendMessage( Messages.DELIVERED_CLIENT, "delivered-client", this.cliente, this.getCentral(), this.cliente.getAid() );
				this.cliente = null;
				this.levandoCliente = false;
			} else {
				mover(p);
			}
			
		} else {
			
			Posicao p = this.posicao.melhorMovimentoPara( this.cliente.getOrigem() );
			
			if ( p.equals( this.cliente.getOrigem() ) ) {
				sendMessage( Messages.GOT_CLIENT, "got-client", this.cliente, this.getCentral(), this.cliente.getAid() );
				this.levandoCliente = true;
			} else {
				mover(p);
			}
			
		}
	}
	
	private void movimentoRandomico() {
		mover( this.posicao.movimentoRandomico() );
	}

	private void mover( Posicao p ) {
		this.sendTaxiMoved(this.posicao, p);
		this.posicao = p;		
	}
	
	private void sendTaxiMoved( Posicao origem, Posicao destino ) {
		AID central = this.getCentral();
		if ( central != null ) {
			sendMessage( Messages.TAXI_MOVED, "taxi-moveu", new Movimento( origem, destino ), central );		
		} else {
			log("Não há central pra enviar dados - %s", this.getLocalName());
		}
	}
	
}