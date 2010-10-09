package agentes;

import java.io.Serializable;

public class Movimento implements Serializable {

	private static final long serialVersionUID = -1252883246028458140L;

	private Posicao origem;
	private Posicao destino;
	
	public Movimento() {
		super();
	}
	
	public Movimento(Posicao origem, Posicao destino) {
		this.origem = origem;
		this.destino = destino;
	}

	public Posicao getOrigem() {
		return origem;
	}

	public void setOrigem(Posicao origem) {
		this.origem = origem;
	}

	public Posicao getDestino() {
		return destino;
	}

	public void setDestino(Posicao destino) {
		this.destino = destino;
	}
	
}
