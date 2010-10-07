package agentes;

import jade.core.AID;
import jade.lang.acl.ACLMessage;

import java.util.Date;

public class TaxiAgent extends CyclicAgent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5003136961225347902L;

	private Posicao posicao;

	public TaxiAgent() {
		super(TYPE_TAXI);
		this.posicao = Posicao.random();
	}

	@Override
	public void action() {
		this.movimentoRandomico();
	}

	public Posicao getPosicao() {
		return posicao;
	}

	private void movimentoRandomico() {
		Posicao novaPosicao = this.posicao.movimentoRandomico();
		System.out.printf("%s andando em %s%n de %s para %s%n",
				this.getLocalName(), new Date(), this.posicao, novaPosicao);
		this.sendTaxiMoved(this.posicao, novaPosicao);
		this.posicao = novaPosicao;
	}

	private void sendTaxiMoved( Posicao origem, Posicao destino ) {
		AID central = this.getCentral();
		if ( central != null ) {
			ACLMessage mensagem = new ACLMessage( Messages.TAXI_MOVED );
			mensagem.addReceiver(this.getCentral());
			mensagem.setContent( origem.toLabel() + "|" + destino.toLabel() );
			mensagem.setConversationId("taxi-moveu");
			this.send(mensagem);			
		} else {
			System.out.printf("Não há central pra enviar dados - %s%n", this.getLocalName());
		}

	}
	
}