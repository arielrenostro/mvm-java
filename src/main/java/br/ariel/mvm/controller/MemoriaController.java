package br.ariel.mvm.controller;

import java.io.IOException;

import br.ariel.mvm.exception.EnderecoCargaSuperiorMemoriaException;
import br.ariel.mvm.exception.MVMException;
import br.ariel.mvm.exception.PosicaoMemoriaInvalidaException;
import br.ariel.mvm.model.Memoria;
import br.ariel.mvm.utils.Utils;

public class MemoriaController {

	private static final int IDX_INICIO_TABELA_REALOCACAO = 2;

	public Memoria criarMemoriaPorBios(String url) throws IOException, MVMException {
		return criarMemoriaPorBios(url, null);
	}

	public Memoria criarMemoriaPorArray(byte[] bios) throws PosicaoMemoriaInvalidaException {
		Memoria memoria = new Memoria(Short.MAX_VALUE);
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

	public Memoria criarMemoriaPorBios(String url, Short enderecoCarga) throws IOException, MVMException {
		byte[] bios = new BiosController().carregarArquivoBios(url);
		bios = tratarEnderecoCarga(bios, enderecoCarga);
		return criarMemoriaPorArray(bios);
	}

	private byte[] tratarEnderecoCarga(byte[] bios, Short enderecoCarga) throws EnderecoCargaSuperiorMemoriaException {
		if (null == enderecoCarga || 2 >= bios.length) {
			return bios;
		}

		byte[] novaBios = new byte[Short.MAX_VALUE];
		short tamanhoTabelaRealocacao = Utils.concatenarBytes(bios[1], bios[0]);

		popularNovaBios(bios, novaBios, tamanhoTabelaRealocacao, enderecoCarga);
		incrementarPorTabelaRealocacao(bios, novaBios, tamanhoTabelaRealocacao, enderecoCarga);

		return novaBios;
	}

	private void incrementarPorTabelaRealocacao(byte[] bios, byte[] novaBios, short tamanhoTabelaRealocacao, Short enderecoCarga) throws EnderecoCargaSuperiorMemoriaException {
		int idxTabela = IDX_INICIO_TABELA_REALOCACAO;

		while (idxTabela < tamanhoTabelaRealocacao) {
			byte low = bios[idxTabela++];
			byte high = bios[idxTabela++];

			short indiceDoIndice = Utils.concatenarBytes(high, low);
			low = bios[indiceDoIndice];
			high = bios[indiceDoIndice + 1];

			short indice = Utils.concatenarBytes(high, low);
			int novoIndice = indice + enderecoCarga;

			if (Short.MAX_VALUE >= novoIndice) {
				throw new EnderecoCargaSuperiorMemoriaException();
			}

			byte[] bytesNovoIndice = Utils.quebrarBytes((short) novoIndice);

			novaBios[indiceDoIndice + enderecoCarga] = bytesNovoIndice[0];
			novaBios[indiceDoIndice + enderecoCarga + 1] = bytesNovoIndice[1];
		}
	}

	private void popularNovaBios(byte[] bios, byte[] novaBios, short tamanhoTabelaRealocacao, int enderecoCarga) {
		int max = bios.length - tamanhoTabelaRealocacao;
		for (int idx = 0; idx < max; idx++) {
			novaBios[idx + enderecoCarga] = bios[idx + tamanhoTabelaRealocacao];
		}
	}
}
