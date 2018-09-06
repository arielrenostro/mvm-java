package br.ariel.mvm.test;

import java.io.IOException;
import java.net.URL;

import org.junit.Assert;
import org.junit.Test;

import br.ariel.mvm.controller.MVMController;
import br.ariel.mvm.exception.MVMException;

public class BIOSTest extends MVMBaseTest {

	private MVMController mvmController = new MVMController();

	@Test
	public void testeBiosEx6() throws InterruptedException, MVMException, IOException {
		URL resource = getClass().getResource("/bios/ex6.bin");
		memoria = mvmController.criarMemoriaPorBios(resource.getFile());
		mvmController.iniciar(processador, memoria, monitor);

		Assert.assertEquals(0x0020, processador.getIp());
		Assert.assertEquals(0x0004, processador.getAx());
		Assert.assertEquals(0x0000, processador.getBx());
		Assert.assertEquals(0x0000, processador.getCx());
		Assert.assertEquals(0x0009, processador.getSp());
		Assert.assertEquals(0x0000, processador.getBp());

		Assert.assertEquals(0x0003, memoria.getData((short) 0x0002));
		Assert.assertEquals(0x0004, memoria.getData((short) 0x0003));
	}

	@Test
	public void testeBiosMattos2() {

	}

}
