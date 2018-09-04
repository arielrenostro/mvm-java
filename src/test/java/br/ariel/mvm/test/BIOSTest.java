package br.ariel.mvm.test;

import org.junit.Assert;
import org.junit.Test;

import br.ariel.mvm.controller.MVMController;
import br.ariel.mvm.exception.MVMException;
import br.ariel.mvm.model.bios.BIOSMattos1;
import br.ariel.mvm.model.bios.IBIOSMVM;

public class BIOSTest extends MVMBaseTest {

	private MVMController mvmController = new MVMController();

	@Test
	public void testeBiosMattos1() throws InterruptedException, MVMException {
		IBIOSMVM bios = new BIOSMattos1();
		mvmController.iniciar(processador, memoria, monitor, bios);

		Assert.assertEquals(9, processador.getIp());
		Assert.assertEquals(4, processador.getAx());
		Assert.assertEquals(0, processador.getBx());
		Assert.assertEquals(0, processador.getCx());
		Assert.assertEquals(0, processador.getSp());
		Assert.assertEquals(0, processador.getBp());
		Assert.assertEquals(4, memoria.getData((short) 2));
	}

	@Test
	public void testeBiosMattos2() {

	}

}
