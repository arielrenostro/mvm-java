package br.ariel.mvm.test.compilador;

import org.junit.Assert;
import org.junit.Test;

import br.ariel.mvm.compilador.CompiladorAssemblyMVM;
import br.ariel.mvm.controller.MVMController;
import br.ariel.mvm.exception.MVMException;
import br.ariel.mvm.model.Memoria;
import br.ariel.mvm.model.Monitor;
import br.ariel.mvm.model.Processador;

public class CompiladorAssemblyMVMTest {

	@Test
	public void test01() throws MVMException, InterruptedException {
		StringBuilder sb = new StringBuilder();
		sb.append("INIT AX").append("\n\r");
		sb.append("MOV AX, {40}").append("\n\r");
		sb.append("MOV BX, AX").append("\n\r");
		sb.append("HALT").append("\n\r");

		byte[] codigo = new CompiladorAssemblyMVM().compilar(sb.toString());

		Processador processador = executar(codigo);

		Assert.assertEquals(40, processador.getAx());
		Assert.assertEquals(40, processador.getBx());
	}

	@Test
	public void test02() throws MVMException, InterruptedException {
		StringBuilder sb = new StringBuilder();
		sb.append("INIT AX").append("\n\r");
		sb.append("MOV AX, {40}").append("\n\r");
		sb.append("MOV BX, AX").append("\n\r");
		sb.append("JMP TESTE").append("\n\r");
		sb.append("HALT").append("\n\r");
		sb.append("TESTE:").append("\n\r");
		sb.append("MOV CX, AX").append("\n\r");
		sb.append("HALT").append("\n\r");

		byte[] codigo = new CompiladorAssemblyMVM().compilar(sb.toString());

		Processador processador = executar(codigo);

		Assert.assertEquals(40, processador.getAx());
		Assert.assertEquals(40, processador.getBx());
		Assert.assertEquals(40, processador.getCx());
	}

	private Processador executar(byte[] codigo) throws MVMException, InterruptedException {
		MVMController mvmController = new MVMController();

		Processador processador = new Processador();
		Memoria memoria = mvmController.criarMemoriaPorArray(codigo);
		Monitor monitor = new Monitor();

		mvmController.iniciar(processador, memoria, monitor);

		return processador;
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
