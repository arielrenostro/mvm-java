package br.ariel.mvm.model;

public class Monitor {

	private StringBuffer buffer = new StringBuffer();

	public void append(String str) {
		buffer.append(str);
	}

	public void append(char c) {
		buffer.append(c);
	}

	public void append(byte b) {
		buffer.append((char) b);
	}

	public String getConteudo() {
		return buffer.toString();
	}

	public void limpar() {
		buffer = new StringBuffer();
	}

}
