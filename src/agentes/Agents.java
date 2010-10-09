package agentes;

import jade.MicroBoot;
import jade.core.MicroRuntime;

public class Agents {

	public static void main(String[] args) throws Exception {
		MicroBoot.main(args);
		MicroRuntime.startAgent("taxi-1", TaxiAgent.class.getName() , new String[]{});
		MicroRuntime.startAgent("taxi-2", TaxiAgent.class.getName() , new String[]{});
		MicroRuntime.startAgent("taxi-3", TaxiAgent.class.getName() , new String[]{});
		MicroRuntime.startAgent("cliente", ClienteAgent.class.getName() , new String[]{});
		Thread.sleep(1000);
		MicroRuntime.startAgent("central", CentralAgent.class.getName() , new String[]{});
	}

}
