package agentes;

import jade.core.AID;
import jade.lang.acl.ACLMessage;

import java.util.HashMap;
import java.util.Map;


public class CentralAgent extends CyclicAgent implements ActionPerformer {

	/**
	 * 
	 */
	private static final long serialVersionUID = 64425490648521428L;

	private Map<AID, Posicao> taxis = new HashMap<AID, Posicao>();
	
	public CentralAgent() {
		super( TYPE_CENTRAL );
	}
	
	@Override
	public void action() {
		ACLMessage message = receive();
		
		switch( message.getPerformative() ) {
		case Messages.TAXI_MOVED :
			break;
		case Messages.REQUEST_TAXI_FROM_CENTRAL :
			break;
			default :
				log("Não é possível processar a mensagem - %s", message);
		}
		
	}
	
	@Override
	public long getSleepTime() {
		return 100;
	}
	
}
