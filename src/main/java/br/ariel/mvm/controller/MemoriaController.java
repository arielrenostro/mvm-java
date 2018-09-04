package br.ariel.mvm.controller;

import br.ariel.mvm.exception.PosicaoMemoriaInvalida;
import br.ariel.mvm.model.Memoria;
import br.ariel.mvm.model.bios.IBIOSMVM;

public class MemoriaController {

	public void carregarBios(Memoria memoria, IBIOSMVM bios) throws PosicaoMemoriaInvalida {
		byte[] programaBios = bios.getBios();
		for (int idx = 0; idx < programaBios.length; idx++) {
			memoria.setData((short) idx, programaBios[idx]);
		}
	}
}
