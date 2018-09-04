package br.ariel.mvm.exception;

public class InstrucaoInvalidaException extends MVMException {

	private static final long serialVersionUID = 7181444248445951127L;

	public InstrucaoInvalidaException(byte instrucao) {
		super("Instrução inválida [" + Byte.toString(instrucao) + "]");
	}

}
