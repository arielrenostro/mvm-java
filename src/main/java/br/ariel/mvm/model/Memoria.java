package br.ariel.mvm.model;

import br.ariel.mvm.exception.PosicaoMemoriaInvalida;

/**
 * @author ariel
 */
public class Memoria {

	private final byte[] memoria;

	public Memoria(short tamanho) {
		super();
		this.memoria = new byte[tamanho];
	}

	public byte getData(short idx) throws PosicaoMemoriaInvalida {
		if (0 > idx || memoria.length <= idx) {
			throw new PosicaoMemoriaInvalida(memoria.length, idx);
		}

		return memoria[idx];
	}

	public void setData(short idx, byte data) throws PosicaoMemoriaInvalida {
		if (0 > idx || memoria.length <= idx) {
			throw new PosicaoMemoriaInvalida(memoria.length, idx);
		}
		this.memoria[idx] = data;
	}

	public short getTamanho() {
		return (short) memoria.length;
	}
}
