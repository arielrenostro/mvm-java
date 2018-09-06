package br.ariel.mvm.main;

import java.io.IOException;

import br.ariel.mvm.controller.MVMController;
import br.ariel.mvm.exception.MVMException;
import br.ariel.mvm.model.Memoria;
import br.ariel.mvm.model.Processador;

public class TesteBIOS {

	public static void main(String[] args) throws IOException, MVMException, InterruptedException {
		MVMController mvmController = new MVMController();

		Memoria memoria = mvmController.criarMemoriaPorBios("/home/ariel/Temp/bios.bin");
		Processador processador = new Processador();

		mvmController.iniciar(processador, memoria, null);

		System.out.println(processador);
		System.out.println(memoria);
	}
}
