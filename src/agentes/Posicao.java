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
		
		if ( this.hasNordeste() ) {
			possibilidades.add( nordeste() );
		}
		
		if ( this.hasNoroeste() ) {
			possibilidades.add( noroeste() );
		}
		
		if ( this.hasSul() ) {
			possibilidades.add( sul() );
		}
		
		if ( this.hasSudeste() ) {
			possibilidades.add( this.sudeste() );
		}
		
		if ( this.hasSudoeste() ) {
			possibilidades.add( this.sudoeste() );
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
		return this.linha < (HEIGHT - 1 ) ;
	}
	
	public boolean hasNordeste() {
		return this.hasNorte() && this.hasLeste();
	}
	
	public Posicao nordeste() {
		return get( this.linha + 1, this.coluna + 1 );
	}
	
	public boolean hasNoroeste() {
		return this.hasNorte() && this.hasOeste();
	}
	
	public Posicao noroeste() {
		return get( this.linha + 1, this.coluna - 1 );
	}
	
	public Posicao sul() {
		return get( this.linha - 1, this.coluna );
	}
	
	public boolean hasSul() {
		return this.linha > 0;
	}
	
	public boolean hasSudeste() {
		return this.hasSul() && this.hasLeste();
	}
	
	public Posicao sudeste() {
		return get( this.linha - 1, this.coluna + 1 );
	}
	
	public boolean hasSudoeste() {
		return this.hasSul() && this.hasOeste();
	}
	
	public Posicao sudoeste() {
		return get( this.linha - 1, this.coluna - 1 );
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
		return this.coluna < ( WIDTH - 1);
	}
	
	@Override
	public String toString() {
		return String.format( "Linha: %s Coluna: %s", this.linha, this.coluna );
	}
	
	public String toLabel() {
		return String.format( "%s-%s", this.linha, this.coluna );
	}
	
	public Posicao melhorMovimentoPara( Posicao p ) {
		List<Posicao> possibilidades  = this.calcularPossibilidades();
		Posicao destino = null;
		Double distancia = null;
		
		for ( Posicao possibilidade : possibilidades ) {
			double distanciaAtual = possibilidade.distancia(p);
			System.out.printf( "De %s para %s Distância: %s Distância atual: %s%n", this, p, distancia, distanciaAtual );
			if ( distancia == null || distanciaAtual < distancia ) {
				destino = possibilidade;
				distancia = distanciaAtual;
			}
		}
		
		return destino;
	}
	
   public double distancia( Posicao p ) {
	   double resutlado = Math.pow( this.linha -  p.linha, 2) + Math.pow( this.coluna - p.coluna, 2 ); 
	   return Math.sqrt(resutlado);
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
