package agentes;

import jade.core.AID;
import jade.lang.acl.ACLMessage;

import java.io.IOException;

public class TaxiAgent extends CyclicAgent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5003136961225347902L;

	private Posicao posicao;
	
	private Posicao cliente;

	public TaxiAgent() {
		super(TYPE_TAXI);
		this.posicao = Posicao.random();
	}

	@Override
	public long getSleepTime() {
		return 2000;
	}
	
	@Override
	public void action() throws Exception {
		
		ACLMessage message = receive();
		if ( message != null ) {
			switch( message.getPerformative() ) {
			case Messages.SEND_TAXI_TO_CLIENT :
				log( "Indo buscar o cliente em -> %s", message );
				this.cliente = ( Posicao ) message.getContentObject();
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
		Posicao p  = this.posicao.melhorMovimentoPara(this.cliente);
		this.sendTaxiMoved(this.posicao, p);
		this.posicao = p;
	}
	
	private void movimentoRandomico() {
		Posicao novaPosicao = this.posicao.movimentoRandomico();
//		System.out.printf("%s andando em %s%n de %s para %s%n",
//				this.getLocalName(), new Date(), this.posicao, novaPosicao);
		this.sendTaxiMoved(this.posicao, novaPosicao);
		this.posicao = novaPosicao;
	}

	private void sendTaxiMoved( Posicao origem, Posicao destino ) {
		AID central = this.getCentral();
		if ( central != null ) {
			ACLMessage mensagem = new ACLMessage( Messages.TAXI_MOVED );
			mensagem.addReceiver(central);
			mensagem.setConversationId("taxi-moveu");
			Movimento m = new Movimento(origem, destino);
			log( "Moveu-se para %s", m );
			try {
				mensagem.setContentObject( m );
			} catch (IOException e) {
				log("Falha ao enviar conteúdo da mensagem: %s", e.getMessage());
			}
			this.send(mensagem);			
		} else {
			System.out.printf("Não há central pra enviar dados - %s%n", this.getLocalName());
		}

	}
	
}