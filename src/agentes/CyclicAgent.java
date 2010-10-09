package agentes;

import jade.core.AID;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;

import java.util.ArrayList;
import java.util.List;

public abstract class CyclicAgent extends Agent implements ActionPerformer {

	public static final String TYPE_TAXI = "taxi";
	public static final String TYPE_CENTRAL = "central-de-taxi";
	public static final String TYPE_CLIENTE = "cliente";
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2204510596161062736L;

	private String type;

	public CyclicAgent(String type) {
		this.type = type;
	}

	@Override
	public long getSleepTime() {
		return 1000;
	}
	
	@Override
	protected final void setup() {
		this.addBehaviour(new ActionPerformerCyclicBehaviorAdapter(this));
		this.register(this.type);
		this.afterSetup();
	}

	public void afterSetup() {
	}

	@Override
	protected void takeDown() {
		try {
			DFService.deregister(this);
		} catch (FIPAException fe) {
			fe.printStackTrace();
		}
	}

	public void log( String format, Object... variables ) {
		System.out.printf( this.getLocalName() + " - " + format + "%n", variables);
	}
	
	public List<AID> getTaxis() {
		return this.getAllByType(TYPE_TAXI);
	}

	public AID getCentral() {
		return this.getByType(TYPE_CENTRAL);
	}

	public AID getCliente() {
		return this.getByType(TYPE_CLIENTE);
	}
	
	public AID getByType( String type ) {
		List<AID> result = this.getAllByType(type);
		if ( result.isEmpty() ) {
			return null;
		} else {
			return result.get(0);
		}		
	}
	
	public List<AID> getAllByType( String type ) {
		DFAgentDescription template = new DFAgentDescription();
		ServiceDescription sd = new ServiceDescription();
		sd.setType( type );
		template.addServices(sd);
		try {
			DFAgentDescription[] result = DFService.search(this, template);
			List<AID> aids = new ArrayList<AID>();
			for ( DFAgentDescription d : result ) {
				aids.add( d.getName() );
			}
			return aids;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}		
	}
	
	public void register(String type) {
		DFAgentDescription dfd = new DFAgentDescription();
		dfd.setName(getAID());
		ServiceDescription sd = new ServiceDescription();
		sd.setType(type);
		sd.setName("jade-" + type);
		dfd.addServices(sd);
		try {
			DFService.register(this, dfd);
		} catch (FIPAException fe) {
			fe.printStackTrace();
		}
	}

}
