package br.ariel.mvm.controller;

import java.io.IOException;

import br.ariel.mvm.exception.MVMException;
import br.ariel.mvm.exception.PosicaoMemoriaInvalidaException;
import br.ariel.mvm.model.Memoria;

public class MemoriaController {

	public Memoria criarMemoriaPorBios(String url) throws IOException, MVMException {
		byte[] bios = new BiosController().carregarArquivoBios(url);
		return criarMemoriaPorArray(bios);
	}

	public Memoria criarMemoriaPorArray(byte[] bios) throws PosicaoMemoriaInvalidaException {
		Memoria memoria = new Memoria((short) bios.length);
		for (short idx = 0; idx < bios.length; idx++) {
			memoria.setData(idx, bios[idx]);
		}
		return memoria;
	}

	public byte[] extrairMemoriaEmArray(Memoria memoria) throws PosicaoMemoriaInvalidaException {
		byte[] mem = new byte[memoria.getTamanho()];
		for (int idx = 0; idx < memoria.getTamanho(); idx++) {
			mem[idx] = memoria.getData((short) idx);
		}
		return mem;
	}
}
