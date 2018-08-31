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

	@Test
	public void testeMonitorAppend() {
		Monitor monitor = new Monitor();
		monitor.append("Oi");
		monitor.append('a');
		monitor.append((byte) 65);

		Assert.assertEquals("OiaA", monitor.getConteudo());
	}

	@Test
	public void testeMonitorLimpar() {
		Monitor monitor = new Monitor();
		monitor.append("asdaisd");
		monitor.limpar();

		Assert.assertEquals("", monitor.getConteudo());
	}

}
