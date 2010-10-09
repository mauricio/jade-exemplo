package agentes;

import java.io.Serializable;
import java.security.SecureRandom;
import java.util.LinkedList;
import java.util.List;

public class Posicao implements Serializable {

	private static final int HEIGHT = 60;
	private static final int WIDTH = 100;

	private static final long serialVersionUID = 6097932789402673806L;

	private static final SecureRandom RANDOM = new SecureRandom();
	
	private int linha;
	private int coluna;
	
	private Posicao(int linha, int coluna) {
		this.linha = linha;
		this.coluna = coluna;
	}

	public int getLinha() {
		return linha;
	}
	
	public int getColuna() {
		return coluna;
	}
	
	@Override
	public int hashCode() {
		return this.toLabel().hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		boolean resultado = false;
		if ( obj instanceof Posicao ) {
			Posicao p = (Posicao) obj;
			resultado = this.toLabel().equals( p.toLabel() );
		}
		return resultado;
	}
	
	public List<Posicao> calcularPossibilidades() {
		List<Posicao> possibilidades = new LinkedList<Posicao>();
		
		if ( this.hasNorte() ) {
			possibilidades.add( norte() );
		}
		
		if ( this.hasSul() ) {
			possibilidades.add( sul() );
		}
		
		if ( this.hasLeste() ) {
			possibilidades.add( leste() );
		}
		
		if ( this.hasOeste() ) {
			possibilidades.add( oeste() );
		}
		
		if ( possibilidades.isEmpty() ) {
			throw new IllegalStateException("Não foi possível encontrar nenhum movimento válido para posição " + this);
		}
		
		return possibilidades;
	}
	
	public Posicao movimentoRandomico() {
		List<Posicao> possibilidades = this.calcularPossibilidades();
		return possibilidades.get( RANDOM.nextInt( possibilidades.size() ) );
	}
	
	public Posicao norte() {
		return get( this.linha + 1, this.coluna );
	}
	
	public boolean hasNorte() {
		return this.linha < HEIGHT;
	}
	
	public Posicao sul() {
		return get( this.linha - 1, this.coluna );
	}
	
	public boolean hasSul() {
		return this.linha > 0;
	}
	
	public Posicao oeste() {
		return get( this.linha, this.coluna - 1 );
	}
	
	public boolean hasOeste() {
		return this.coluna > 0;
	}
	
	public Posicao leste() {
		return get( this.linha, this.coluna + 1 );
	}
	
	public boolean hasLeste() {
		return this.coluna < WIDTH;
	}
	
	@Override
	public String toString() {
		return String.format( "Linha: %s Coluna: %s", this.linha, this.coluna );
	}
	
	public String toLabel() {
		return String.format( "%s-%s", this.linha, this.coluna );
	}
	
	public static final Posicao get( int linha, int coluna ) {
		return new Posicao(linha, coluna);
	}
	
	public static final Posicao random() {
		return get( RANDOM.nextInt(HEIGHT), RANDOM.nextInt( WIDTH ) );
	}
	
	public static final Posicao load( String posicao ) {
		String[] resultado = posicao.split("-");
		return get( Integer.valueOf( resultado[0] ), Integer.valueOf( resultado[1] ) );
	}
	
}
