package br.ariel.mvm.test.montador;

import org.junit.Assert;
import org.junit.Test;

import br.ariel.mvm.controller.MVMController;
import br.ariel.mvm.exception.MVMException;
import br.ariel.mvm.model.InstrucaoProcessador;
import br.ariel.mvm.model.Memoria;
import br.ariel.mvm.model.Monitor;
import br.ariel.mvm.model.Processador;
import br.ariel.mvm.montador.MontadorMVM;

public class MontadorMVMTest {

	private Processador processador;
	private Memoria memoria;
	private Monitor monitor;

	@Test
	public void test01() throws MVMException, InterruptedException {
		StringBuilder sb = new StringBuilder();
		sb.append("INIT AX").append("\n\r");
		sb.append("MOV AX, {40}").append("\n\r");
		sb.append("MOV BX, AX").append("\n\r");
		sb.append("HALT").append("\n\r");

		byte[] codigo = new MontadorMVM().montar(sb.toString());

		executar(codigo);

		Assert.assertEquals(40, processador.getAx());
		Assert.assertEquals(40, processador.getBx());
	}

	@Test
	public void test02() throws MVMException, InterruptedException {
		StringBuilder sb = new StringBuilder();
		sb.append("INIT AX").append("\n\r");
		sb.append("").append("\n\r");
		sb.append("MOV AX, {40}").append("\n\r");
		sb.append("MOV BX, AX").append("\n\r");
		sb.append("JMP TESTE").append("\n\r");
		sb.append("").append("\n\r");
		sb.append("HALT").append("\n\r");
		sb.append("").append("\n\r");
		sb.append("TESTE:").append("\n\r");
		sb.append("MOV CX, AX").append("\n\r");
		sb.append("HALT").append("\n\r");

		byte[] codigo = new MontadorMVM().montar(sb.toString());

		executar(codigo);

		Assert.assertEquals(40, processador.getAx());
		Assert.assertEquals(40, processador.getBx());
		Assert.assertEquals(40, processador.getCx());
	}

	@Test
	public void test03() throws MVMException, InterruptedException {
		StringBuilder sb = new StringBuilder();
		sb.append("INIT AX").append("\n\r");
		sb.append("").append("\n\r");
		sb.append("ORG 5").append("\n\r");
		sb.append("INC AX").append("\n\r");
		sb.append("").append("\n\r");
		sb.append("ORG 1").append("\n\r");
		sb.append("INC BX").append("\n\r");
		sb.append("HALT").append("\n\r");

		byte[] codigo = new MontadorMVM().montar(sb.toString());

		executar(codigo);

		Assert.assertEquals(0, processador.getAx());
		Assert.assertEquals(1, processador.getBx());

		Assert.assertEquals(0, memoria.getData((short) 0));
		Assert.assertEquals(InstrucaoProcessador.INC_BX.getCode(), memoria.getData((short) 1));
		Assert.assertEquals(InstrucaoProcessador.HALT.getCode(), memoria.getData((short) 2));
		Assert.assertEquals(0, memoria.getData((short) 3));
		Assert.assertEquals(0, memoria.getData((short) 4));
		Assert.assertEquals(InstrucaoProcessador.INC_AX.getCode(), memoria.getData((short) 5));
	}

	private void executar(byte[] codigo) throws MVMException, InterruptedException {
		MVMController mvmController = new MVMController();

		processador = new Processador();
		memoria = mvmController.criarMemoriaPorArray(codigo);
		monitor = new Monitor();

		mvmController.iniciar(processador, memoria, monitor);
	}


	// sb.append("").append("\n\r");
	// sb.append("").append("\n\r");
	// sb.append("").append("\n\r");
	// sb.append("").append("\n\r");
	// sb.append("").append("\n\r");
	// sb.append("").append("\n\r");
	// sb.append("").append("\n\r");
	// sb.append("").append("\n\r");
	// sb.append("").append("\n\r");
	// sb.append("").append("\n\r");
}
