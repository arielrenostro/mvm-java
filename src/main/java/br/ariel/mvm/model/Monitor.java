package br.ariel.mvm.model;

import java.util.ArrayList;
import java.util.List;

import br.ariel.mvm.model.listeners.IMonitorListener;

public class Monitor {

	private static final int LINHAS = 20;
	private static final int COLUNAS = 40;

	private char[] buffer = new char[200];
	private int cursor = 0;

	private List<IMonitorListener> listeners = new ArrayList<>();

	public void set(short linha, short coluna, byte dado) {
		if (linha > LINHAS || coluna > COLUNAS) {
			return;
		}

		if ((dado >= 65 && dado <= 90) || dado == 32) {
			buffer[(linha * LINHAS) + (coluna * COLUNAS)] = (char) dado;
			notificar(linha, coluna, dado);
		}
	}

	private void notificar(short linha, short coluna, byte dado) {
		listeners.forEach(listener -> listener.notificar(linha, coluna, dado));
	}

	public void append(char c) {
		buffer[cursor++] = c;
		notificar((short) 0, (short) 0, (byte) c);
	}

	public void append(byte b) {
		append((char) b);
	}

	public String getConteudo() {
		StringBuilder sb = new StringBuilder();
		for (char c : buffer) {
			sb.append(c);
		}
		return sb.toString();
	}

	public void limpar() {
		buffer = new char[200];
	}

	public void adicionarListener(IMonitorListener listener) {
		if (null != listener) {
			listeners.add(listener);
		}
	}
}
