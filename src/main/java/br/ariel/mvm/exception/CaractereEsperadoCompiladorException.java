package br.ariel.mvm.exception;

public class CaractereEsperadoCompiladorException extends CompiladorException {

	private static final long serialVersionUID = -2019352632735663490L;

	public CaractereEsperadoCompiladorException(int idxLinha, String string) {
		super("Linha [" + (idxLinha + 1) + "], caractere esperado [" + string + "]");
	}
}
