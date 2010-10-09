package agentes;

public class Taxi {
	
	private Posicao posicao;
	
	private boolean ocupado;
	
	public Taxi() {
		super();
	}
	
	public Taxi(Posicao posicao) {
		this.posicao = posicao;
		this.ocupado = false;
	}

	public Posicao getPosicao() {
		return posicao;
	}

	public void setPosicao(Posicao posicao) {
		this.posicao = posicao;
	}

	public boolean isOcupado() {
		return ocupado;
	}

	public void setOcupado(boolean ocupado) {
		this.ocupado = ocupado;
	}

}
