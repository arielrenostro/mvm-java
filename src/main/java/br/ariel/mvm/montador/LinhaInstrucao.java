package br.ariel.mvm.montador;

import java.util.Arrays;

import br.ariel.mvm.model.InstrucaoProcessador;

public class LinhaInstrucao {

	private final String linha;
	private final int idxLinha;
	private InstrucaoProcessador instrucaoProcessador;
	private byte[] instrucao = new byte[0];

	private int idxByte;

	private String label = "";
	private boolean ehLabel = false;

	private int idxOrg;
	private boolean ehOrg = false;

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

	public int getIdxByte() {
		return idxByte;
	}

	public void setIdxByte(int idxByte) {
		this.idxByte = idxByte;
	}

	public boolean isEhOrg() {
		return ehOrg;
	}

	public void setEhOrg(boolean ehOrg) {
		this.ehOrg = ehOrg;
	}

	public int getIdxOrg() {
		return idxOrg;
	}

	public void setIdxOrg(int idxOrg) {
		this.idxOrg = idxOrg;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Linha [");
		sb.append(linha);
		sb.append("], idxLinha [");
		sb.append(idxLinha);
		sb.append("], idxByte [");
		sb.append(idxByte);
		sb.append("], Label [");
		sb.append(label);
		sb.append("], ehLabel [");
		sb.append(ehLabel);
		sb.append("], idxOrg [");
		sb.append(idxOrg);
		sb.append("], ehOrg [");
		sb.append(ehOrg);
		sb.append("]");
		return sb.toString();
	}

	public InstrucaoProcessador getInstrucaoProcessador() {
		return instrucaoProcessador;
	}

	public void setInstrucaoProcessador(InstrucaoProcessador instrucaoProcessador) {
		this.instrucaoProcessador = instrucaoProcessador;
	}

}
