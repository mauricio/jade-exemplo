package agentes;

import jade.core.AID;
import jade.lang.acl.ACLMessage;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.swing.JFrame;
import javax.swing.JLabel;

public class CentralAgent extends CyclicAgent {

	private static final long serialVersionUID = 64425490648521428L;
	private static final Color STREET_COLOR = new Color(238, 238, 238);

	private Map<AID, Taxi> taxis = Collections
			.synchronizedMap(new HashMap<AID, Taxi>());

	private JLabel[][] tiles = new JLabel[60][];

	private Set<Movimento> clientesEmEspera = Collections
			.synchronizedSet(new HashSet<Movimento>());

	public CentralAgent() {
		super(TYPE_CENTRAL);

		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createAndShowGUI();
			}
		});

		log("Central iniciada");

	}

	@Override
	public void action() throws Exception {

		for (Movimento m : this.clientesEmEspera) {
			this.handleTaxiRequest(m);
		}

		ACLMessage message = receive();

		if (message != null) {

			switch (message.getPerformative()) {
			case Messages.TAXI_MOVED:
				this.handleTaxiMoved(message.getSender(), (Movimento) message.getContentObject());
				break;
			case Messages.REQUEST_TAXI_FROM_CENTRAL:
				this.handleTaxiRequest((Movimento) message.getContentObject());
				break;
			case Messages.GOT_CLIENT: {
				Movimento movimento = (Movimento) message.getContentObject();
				paint(movimento.getOrigem(), STREET_COLOR, null);
			}
				break;
			case Messages.DELIVERED_CLIENT: {
				Movimento movimento = (Movimento) message.getContentObject();
				paint(movimento.getDestino(), Color.BLACK, null);
				marcarTaxi( message.getSender(), false );
			}
				break;
			default:
				log("Não é possível processar a mensagem - %s", message);
			}

		}

	}

	@Override
	public long getSleepTime() {
		return 100;
	}

	private void handleTaxiMoved(AID sender, Movimento movimento) {

		Taxi taxi = this.taxis.get(sender);
		if (taxi == null) {
			taxi = new Taxi();
			this.taxis.put(sender, taxi);
		}
		taxi.setPosicao(movimento.getDestino());

		if ( !this.getLabel(movimento.getDestino()).getBackground().equals( Color.BLACK ) ) {
			// Recuperando estado anterior
			paint(movimento.getOrigem(), STREET_COLOR, null);
			// Atualizando estado do tile
			marcarTaxi( sender, taxi.isOcupado() );			
		}

	}

	private void handleTaxiRequest(Movimento movimento) {

		paint(movimento.getOrigem(), Color.BLACK);

		if (!this.requestCloserTaxi(movimento)) {
			log("Taxi não encontrado - %s", this.taxis);
			this.clientesEmEspera.add(movimento);
		} else {
			log("Taxi encontrado");
		}

	}

	private void marcarTaxi( AID aid, boolean ocupado ) {
		Taxi taxi = this.taxis.get( aid );
		taxi.setOcupado(ocupado);
		Color taxiColor = taxi.isOcupado() ? Color.RED : Color.BLUE;
		paint( taxi.getPosicao(), taxiColor, aid.getName() );		
	}
	
	private boolean requestCloserTaxi(Movimento movimento) {

		Entry<AID, Taxi> closerTaxi = null;
		Double distancia = null;

		for (Entry<AID, Taxi> taxi : this.taxis.entrySet()) {

			double novaDistancia = taxi.getValue().getPosicao()
					.distancia(movimento.getOrigem());

			if (!taxi.getValue().isOcupado()
					&& (distancia == null || novaDistancia < distancia)) {
				distancia = novaDistancia;
				closerTaxi = taxi;
			}

		}

		if (closerTaxi != null) {
			closerTaxi.getValue().setOcupado(true);
			sendTaxiToClient(closerTaxi.getKey(), movimento);
			this.clientesEmEspera.remove(movimento);
		}

		return closerTaxi != null;
	}

	private void sendTaxiToClient(AID taxi, Movimento movimento) {

		this.sendMessage(Messages.SEND_TAXI_TO_CLIENT, "taxi-to-client",
				movimento, taxi);

	}

	private void paint(Posicao posicao, Color color) {
		getLabel(posicao).setBackground(color);
	}

	private void paint(Posicao posicao, Color color, String tooltip) {
		getLabel(posicao).setBackground(color);
		getLabel(posicao).setToolTipText(tooltip);
	}

	private JLabel getLabel(Posicao posicao) {
		return this.tiles[posicao.getLinha()][posicao.getColuna()];
	}

	private void createAndShowGUI() {
		JFrame frame = new JFrame("Monitor Central");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		Container pane = frame.getContentPane();
		final GridLayout layout = new GridLayout(0, 100);
		pane.setLayout(layout);

		for (int i = 0; i < this.tiles.length; i++) {
			this.tiles[i] = new JLabel[100];
			for (int j = 0; j < this.tiles[i].length; j++) {
				JLabel tile = new JLabel();
				tile.setOpaque(true);
				tile.setBackground(STREET_COLOR);
				// tile.setBorder(new LineBorder(Color.BLACK));
				tile.setPreferredSize(new Dimension(10, 10));
				pane.add(tile);

				this.tiles[i][j] = tile;
			}
		}

		frame.setResizable(false);
		frame.pack();
		frame.setVisible(true);
	}

}
