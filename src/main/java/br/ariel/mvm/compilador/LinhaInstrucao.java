package br.ariel.mvm.compilador;

import java.util.Arrays;

public class LinhaInstrucao {

	private String linha;
	private int idxLinha;
	private byte[] instrucao = new byte[0];

	public LinhaInstrucao(String linha, int idxLinha) {
		super();
		this.linha = linha;
		this.idxLinha = idxLinha;
	}

	public String getLinha() {
		return linha;
	}

	public int getIdxLinha() {
		return idxLinha;
	}

	public byte[] getInstrucao() {
		return Arrays.copyOf(instrucao, instrucao.length);
	}

	public void setInstrucao(byte[] instrucao) {
		this.instrucao = instrucao;
	}
}
