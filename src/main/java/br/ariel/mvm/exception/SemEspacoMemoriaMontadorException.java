package br.ariel.mvm.exception;

public class SemEspacoMemoriaMontadorException extends MontadorException {

	private static final long serialVersionUID = 2054820118319294991L;

	public SemEspacoMemoriaMontadorException(int totalBytes) {
		super("O código ocupa mais espaço que a memória pode suportar! [" + totalBytes + "] bytes");
	}
}
