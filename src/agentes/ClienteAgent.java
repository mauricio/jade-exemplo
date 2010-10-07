package agentes;

import jade.core.AID;
import jade.lang.acl.ACLMessage;

public class ClienteAgent extends CyclicAgent {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8460376681891029264L;

	private Posicao posicao;

	private boolean sentRequest;

	public ClienteAgent() {
		super(TYPE_CENTRAL);
		this.posicao = Posicao.random();
	}

	@Override
	public void action() {
		if (!this.sentRequest) {
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
	}

	public Posicao getPosicao() {
		return posicao;
	}

	private void sendTaxiRequest() {
		ACLMessage mensagem = new ACLMessage( Messages.REQUEST_TAXI_FROM_CENTRAL );
		mensagem.addReceiver(this.getCentral());
		mensagem.setContent(this.posicao.toLabel());
		mensagem.setConversationId("pedido-de-taxi");
		mensagem.setReplyWith("pedido-" + System.currentTimeMillis());
		this.send(mensagem);
	}
}
