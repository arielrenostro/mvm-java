package br.ariel.mvm.exception;

public class CaractereEsperadoMontadorException extends MontadorException {

	private static final long serialVersionUID = -2019352632735663490L;

	public CaractereEsperadoMontadorException(int idxLinha, String string) {
		super("Linha [" + (idxLinha + 1) + "], caractere esperado [" + string + "]");
	}
}
