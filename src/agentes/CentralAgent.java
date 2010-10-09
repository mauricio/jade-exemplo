package agentes;

import jade.core.AID;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;

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

	private Map<AID, Taxi> taxis = Collections
			.synchronizedMap(new HashMap<AID, Taxi>());

	private JLabel[][] tiles = new JLabel[60][];
	
	private Set<Posicao> clientesEmEspera = Collections.synchronizedSet( new HashSet<Posicao>() );

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
	public void action() {
		
		for ( Posicao p : this.clientesEmEspera ) {
			this.handleTaxiRequest(p);
		}
		
		ACLMessage message = receive();

		if (message != null) {
			
			switch (message.getPerformative()) {
			case Messages.TAXI_MOVED:
				try {
					Movimento movimento = (Movimento) message
							.getContentObject();
					this.handleTaxiMoved(message.getSender(), movimento);
				} catch (UnreadableException e) {
					log("Falha ao ler conteúdo da mensagem: %s", e.getMessage());
				}

				break;
			case Messages.REQUEST_TAXI_FROM_CENTRAL:
				try {
					Posicao posicaoCliente = (Posicao) message
							.getContentObject();
					this.handleTaxiRequest(posicaoCliente);
				} catch (UnreadableException e) {
					log("Falha ao ler conteúdo da mensagem: %s", e.getMessage());
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
		
		// Recuperando estado anterior
		this.tiles[movimento.getOrigem().getLinha()][movimento.getOrigem()
				.getColuna()].setBackground(new Color(238, 238, 238));
		this.tiles[movimento.getDestino().getLinha()][movimento.getDestino()
				.getColuna()].setToolTipText(null);

		// Atualizando estado do tile
		Color taxiColor = taxi.isOcupado() ? Color.RED : Color.BLUE;
		this.tiles[movimento.getDestino().getLinha()][movimento.getDestino()
				.getColuna()].setBackground(taxiColor);
		this.tiles[movimento.getDestino().getLinha()][movimento.getDestino()
				.getColuna()].setToolTipText(sender.getName());

	}

	private void handleTaxiRequest(Posicao posicaoCliente) {

		log("Procurando um taxi pra %s", posicaoCliente);
		
		this.tiles[posicaoCliente.getLinha()][posicaoCliente.getColuna()]
				.setBackground(Color.BLACK);

		if (  !this.requestCloserTaxi(posicaoCliente) ) {
			log("Taxi não encontrado - %s", this.taxis);
			this.clientesEmEspera.add( posicaoCliente );
		} else {
			log("Taxi encontrado");
		}

	}

	private boolean requestCloserTaxi(Posicao posicaoCliente) {

		Entry<AID, Taxi> closerTaxi = null;
		Double distancia = null;

		for (Entry<AID, Taxi> taxi : this.taxis.entrySet()) {

			double novaDistancia = taxi.getValue().getPosicao().distancia(posicaoCliente);

			if ( !taxi.getValue().isOcupado() && (distancia == null || novaDistancia < distancia) ) {
				distancia = novaDistancia;
				closerTaxi = taxi;
			}
						
		}

		if ( closerTaxi != null ) {
			closerTaxi.getValue().setOcupado(true);
			sendTaxiToClient( closerTaxi.getKey(), posicaoCliente );
			this.clientesEmEspera.remove( posicaoCliente );
		}
		
		return closerTaxi != null;
	}

	private void sendTaxiToClient(AID taxi, Posicao posicaoCliente) {
		
		this.sendMessage(Messages.SEND_TAXI_TO_CLIENT, "taxi-to-client", posicaoCliente, taxi);
		
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
