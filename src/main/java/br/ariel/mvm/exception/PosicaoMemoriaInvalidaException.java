package br.ariel.mvm.exception;

public class PosicaoMemoriaInvalidaException extends MVMException {

	private static final long serialVersionUID = 8432317558796267407L;

	public PosicaoMemoriaInvalidaException(int length, int idx) {
		super("Tamanho total [" + length + "], tentativa de acesso [" + idx + "]");
	}

}
