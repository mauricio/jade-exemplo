package agentes;

import org.junit.Test;


public class PosicaoTest {

	@Test
	public void testMovimento() {
		Posicao p = Posicao.get( 59 , 99);
		System.out.println( p.calcularPossibilidades() );
	}
	
}
