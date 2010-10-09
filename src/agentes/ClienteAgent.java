package agentes;

import jade.core.AID;
import jade.lang.acl.ACLMessage;

public class ClienteAgent extends CyclicAgent {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8460376681891029264L;

	private Posicao origem;
	private Posicao destino;

	private boolean sentRequest;

	public ClienteAgent() {
		super(TYPE_CLIENTE);
		this.origem = Posicao.random();
		this.destino = Posicao.random();
	}

	@Override
	public void action() throws Exception {
		if (!this.sentRequest) {
			this.callTaxi();
		}
		
		ACLMessage message = receive();
		if ( message != null ) {
			switch( message.getPerformative() ) {
			case Messages.DELIVERED_CLIENT : {
				Movimento m = (Movimento) message.getContentObject();
				this.origem = m.getDestino();
			}
			}
		}
		
	}

	private void callTaxi() {
		AID central = this.getCentral();
		if (central != null) {
			System.out.printf("Enviando mensagem de %s pra central %s%n",
					this.getLocalName(), central);
			this.sentRequest = true;
			this.sendTaxiRequest();
		} else {
			System.out.printf("Ainda não há uma central visível para %s%n",
					this.getLocalName());
		}		
	}
	
	@Override
	public boolean continueLoop() {
		return !this.isAtDestination();
	}
	
	private void sendTaxiRequest() {
		sendMessage(Messages.REQUEST_TAXI_FROM_CENTRAL, "pedido-de-taxi", new Movimento( this.getAID(), origem, destino), this.getCentral());
	}

	public boolean isAtDestination() {
		return this.origem == this.destino;
	}
	
}
