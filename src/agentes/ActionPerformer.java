package agentes;

public interface ActionPerformer {

	void action() throws Exception;
	
	long getSleepTime();
	
	boolean continueLoop();
	
}
