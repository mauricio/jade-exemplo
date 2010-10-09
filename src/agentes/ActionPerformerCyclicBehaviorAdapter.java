package agentes;

import jade.core.behaviours.CyclicBehaviour;

public class ActionPerformerCyclicBehaviorAdapter extends CyclicBehaviour {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7847825656644181334L;
	
	private ActionPerformer performer;
	
	public ActionPerformerCyclicBehaviorAdapter(ActionPerformer performer) {
		this.performer = performer;
	}

	@Override
	public void action() {
		try {
			Thread.sleep( this.performer.getSleepTime() );
		} catch (InterruptedException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		this.performer.action();
	}
	
}
