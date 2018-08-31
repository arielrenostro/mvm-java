package br.ariel.mvm.test;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import br.ariel.mvm.controller.ProcessadorController;
import br.ariel.mvm.exception.InstrucaoInvalidaException;
import br.ariel.mvm.exception.MemoriaSemTamanhoException;
import br.ariel.mvm.exception.PosicaoMemoriaInvalida;
import br.ariel.mvm.exception.SemMemoriaException;
import br.ariel.mvm.exception.SemProcessadorException;
import br.ariel.mvm.model.Instrucao;
import br.ariel.mvm.model.Memoria;
import br.ariel.mvm.model.Monitor;
import br.ariel.mvm.model.Processador;

public class ProcessadorTest {

	private Monitor monitor;
	private Processador processador;
	private Memoria memoria;

	private ProcessadorController processadorController;

	@Before
	public void prepararPerifericos() {
		monitor = new Monitor();
		processador = new Processador();
		memoria = new Memoria(Short.MAX_VALUE);
		processadorController = new ProcessadorController();

		processador.setIp((short) 0);
	}

	@Test
	public void testeMovAxBx() throws SemProcessadorException, SemMemoriaException, MemoriaSemTamanhoException, PosicaoMemoriaInvalida, InterruptedException, InstrucaoInvalidaException {
		processador.setBx((short) 20);
		processador.setAx((short) 0);

		memoria.setData((short) 0, Instrucao.MOV_AX_BX.getCode());
		adicionarHalt(1);

		processadorController.processar(processador, memoria, monitor);

		Assert.assertEquals(20, processador.getAx());
		Assert.assertEquals(20, processador.getBx());
	}

	@Test
	public void testeMovAxCx() throws SemProcessadorException, SemMemoriaException, MemoriaSemTamanhoException, PosicaoMemoriaInvalida, InterruptedException, InstrucaoInvalidaException {
		processador.setCx((short) 20);
		processador.setAx((short) 0);

		memoria.setData((short) 0, Instrucao.MOV_AX_CX.getCode());
		adicionarHalt(1);

		processadorController.processar(processador, memoria, monitor);

		Assert.assertEquals(20, processador.getAx());
		Assert.assertEquals(20, processador.getCx());
	}

	@Test
	public void testePopAx() throws SemProcessadorException, SemMemoriaException, MemoriaSemTamanhoException, PosicaoMemoriaInvalida, InterruptedException, InstrucaoInvalidaException {
		processador.setAx((short) 0);

		memoria.setData((short) (0x7FFE), (byte) 0x0A); // High
		memoria.setData((short) (0x7FFD), (byte) 0xF0); // Low

		memoria.setData((short) 0, Instrucao.POP_AX.getCode());
		adicionarHalt(1);

		processador.setSp((short) (processador.getSp() - 2));

		processadorController.processar(processador, memoria, monitor);

		Assert.assertEquals((short) 0x0AF0, processador.getAx());
	}

	@Test
	public void testPushAx() throws SemProcessadorException, SemMemoriaException, MemoriaSemTamanhoException, PosicaoMemoriaInvalida, InterruptedException, InstrucaoInvalidaException {
		processador.setAx((short) 0xF00A);

		memoria.setData((short) 0, Instrucao.PUSH_AX.getCode());
		adicionarHalt(1);

		processadorController.processar(processador, memoria, monitor);

		Assert.assertEquals((byte) 0x0A, memoria.getData((short) (0x7FFD)));
		Assert.assertEquals((byte) 0xF0, memoria.getData((short) (0x7FFE)));
		Assert.assertEquals((short) 0x7FFC, processador.getSp());
	}

	@Test
	public void testPushPopAx() throws PosicaoMemoriaInvalida, SemProcessadorException, SemMemoriaException, MemoriaSemTamanhoException, InterruptedException, InstrucaoInvalidaException {
		processador.setAx((short) 0xF00A);

		memoria.setData((short) 0, Instrucao.PUSH_AX.getCode());
		memoria.setData((short) 1, Instrucao.POP_AX.getCode());
		adicionarHalt(2);

		processadorController.processar(processador, memoria, monitor);

		Assert.assertEquals((short) 0xF00A, processador.getAx());
		Assert.assertEquals((short) 0x7FFE, processador.getSp());
	}

	private void adicionarHalt(int idx) throws PosicaoMemoriaInvalida {
		memoria.setData((short) idx, Instrucao.HALT.getCode());
	}
}
