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

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(getClass().getSimpleName());
		sb.append(" Tamanho [");
		sb.append(getTamanho());
		sb.append("] [");

		for (short idx = 0; idx < memoria.length; idx++) {
			sb.append(idx);
			sb.append("=");
			sb.append(memoria[idx]);
			if (idx < memoria.length - 1) {
				sb.append(", ");
			}
		}

		sb.append("]");
		return sb.toString();
	}
}
