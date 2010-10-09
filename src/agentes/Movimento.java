package agentes;

import jade.core.AID;

import java.io.Serializable;

public class Movimento implements Serializable {

	private static final long serialVersionUID = -1252883246028458140L;

	private AID aid;
	private Posicao origem;
	private Posicao destino;
	
	public Movimento() {
		super();
	}
	
	public Movimento(Posicao origem, Posicao destino) {
		this.origem = origem;
		this.destino = destino;
	}

	public Movimento(AID aid, Posicao origem, Posicao destino) {
		this.aid = aid;
		this.origem = origem;
		this.destino = destino;
	}

	public AID getAid() {
		return aid;
	}
	
	public void setAid(AID aid) {
		this.aid = aid;
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
	
	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((aid == null) ? 0 : aid.hashCode());
		result = prime * result + ((destino == null) ? 0 : destino.hashCode());
		result = prime * result + ((origem == null) ? 0 : origem.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Movimento other = (Movimento) obj;
		if (aid == null) {
			if (other.aid != null)
				return false;
		} else if (!aid.equals(other.aid))
			return false;
		if (destino == null) {
			if (other.destino != null)
				return false;
		} else if (!destino.equals(other.destino))
			return false;
		if (origem == null) {
			if (other.origem != null)
				return false;
		} else if (!origem.equals(other.origem))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return String.format( "Origem: %s | Destino: %s", this.origem, this.destino );
	}
	
}
