package br.ariel.mvm.exception;

public class MemoriaSemTamanhoException extends MVMException {

	private static final long serialVersionUID = -1103775197600101095L;

	public MemoriaSemTamanhoException() {
		super("Memória sem tamanho!");
	}

}
