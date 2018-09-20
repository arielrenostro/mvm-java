package br.ariel.mvm.test;

import org.junit.Assert;
import org.junit.Test;

import br.ariel.mvm.model.Monitor;

public class MonitorTest {

	@Test
	public void testeMonitorLimpo() {
		Monitor monitor = new Monitor();
		Assert.assertEquals("", monitor.getConteudo());
	}

}
