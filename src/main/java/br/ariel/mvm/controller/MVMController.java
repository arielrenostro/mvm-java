package br.ariel.mvm.controller;

import java.io.IOException;

import br.ariel.mvm.exception.MVMException;
import br.ariel.mvm.exception.PosicaoMemoriaInvalidaException;
import br.ariel.mvm.exception.SemMemoriaException;
import br.ariel.mvm.exception.SemProcessadorException;
import br.ariel.mvm.model.ContextoMVM;
import br.ariel.mvm.model.Memoria;
import br.ariel.mvm.model.Monitor;
import br.ariel.mvm.model.Processador;

public class MVMController {

	private ProcessadorController processadorController = new ProcessadorController();
	private MemoriaController memoriaController = new MemoriaController();

	public void iniciar(Processador processador, Memoria memoria, Monitor monitor) throws MVMException, InterruptedException {
		iniciar(processador, memoria, monitor, new ContextoMVM());
	}

	public void iniciar(Processador processador, Memoria memoria, Monitor monitor, ContextoMVM contextoMVM) throws MVMException, InterruptedException {
		iniciar(processador, memoria, monitor, contextoMVM, null);
	}

	public void iniciar(Processador processador, Memoria memoria, Monitor monitor, ContextoMVM contextoMVM, Integer enderecoCarga) throws MVMException, InterruptedException {
		validarProcessar(processador, memoria);
		memoriaController.tratarEnderecoCarga(memoria, enderecoCarga);
		processadorController.processar(processador, memoria, monitor, contextoMVM);
	}

	private void validarProcessar(Processador processador, Memoria memoria) throws MVMException {
		if (null == processador) {
			throw new SemProcessadorException();
		}

		if (null == memoria) {
			throw new SemMemoriaException();
		}
	}

	public Memoria criarMemoriaPorBios(String bios) throws IOException, MVMException {
		return memoriaController.criarMemoriaPorBios(bios);
	}

	public Memoria criarMemoriaPorArray(byte[] array) throws PosicaoMemoriaInvalidaException {
		return memoriaController.criarMemoriaPorArray(array);
	}

}
