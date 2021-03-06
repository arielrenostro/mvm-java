package br.ariel.mvm.test;

import org.junit.Before;

import br.ariel.mvm.controller.MVMController;
import br.ariel.mvm.exception.PosicaoMemoriaInvalidaException;
import br.ariel.mvm.model.InstrucaoProcessador;
import br.ariel.mvm.model.Memoria;
import br.ariel.mvm.model.Monitor;
import br.ariel.mvm.model.Processador;

public class MVMBaseTest {

	protected Monitor monitor;
	protected Processador processador;
	protected Memoria memoria;

	protected short idx;

	protected MVMController mvmController;

	@Before
	public void prepararPerifericos() {
		monitor = new Monitor();
		processador = new Processador();
		memoria = new Memoria((short) 512);

		mvmController = new MVMController();

		processador.setIp((short) 0);
	}

	protected void adicionarInstrucao(int b) throws PosicaoMemoriaInvalidaException {
		memoria.setData(idx++, (byte) b);
	}

	protected void adicionarInstrucao(InstrucaoProcessador instrucao) throws PosicaoMemoriaInvalidaException {
		adicionarInstrucao(instrucao.getCode());
	}
}
