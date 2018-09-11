package br.ariel.mvm.compilador;

import java.util.Arrays;

public class LinhaInstrucao {

	private final String linha;
	private final int idxLinha;
	private byte[] instrucao = new byte[0];

	private String label;
	private boolean ehLabel = false;

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

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public boolean isEhLabel() {
		return ehLabel;
	}

	public void setEhLabel(boolean ehLabel) {
		this.ehLabel = ehLabel;
	}
}
