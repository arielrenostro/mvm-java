package br.ariel.mvm.test;

import org.junit.Assert;
import org.junit.Test;

import br.ariel.mvm.exception.PosicaoMemoriaInvalida;
import br.ariel.mvm.model.Memoria;

public class MemoriaTest {

	@Test
	public void testeTamanho() throws PosicaoMemoriaInvalida {
		Memoria memoria = new Memoria((short) 16);
		memoria.getData((short) 15);
	}

	@Test
	public void testeTamanhoError() throws PosicaoMemoriaInvalida {
		Memoria memoria = new Memoria((short) 16);
		try {
			memoria.getData((short) 16);
			Assert.fail();
		} catch (Exception e) {
		}
	}

	@Test
	public void testeData() throws PosicaoMemoriaInvalida {
		Memoria memoria = new Memoria((short) 16);
		memoria.setData((short) 0, (byte) 20);
		Assert.assertEquals(20, memoria.getData((short) 0));
	}

	@Test
	public void testeTamanhoMaximo() throws PosicaoMemoriaInvalida {
		Memoria memoria = new Memoria(Short.MAX_VALUE);
		memoria.setData((short) (Short.MAX_VALUE - 1), (byte) 52);
		Assert.assertEquals(0, memoria.getData((short) 0));
		Assert.assertEquals(52, memoria.getData((short) (Short.MAX_VALUE - 1)));
	}
}
