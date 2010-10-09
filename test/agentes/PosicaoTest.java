package agentes;

import org.junit.Test;
import static org.junit.Assert.*;


public class PosicaoTest {

	@Test
	public void testMelhorPosicao() {
		Posicao origem = Posicao.get( 58 , 98);
		Posicao destino = Posicao.get( 48 , 88);
		
		assertEquals( origem.melhorMovimentoPara( destino ), Posicao.get( 57 , 97) );
	}
	
	
	
}
