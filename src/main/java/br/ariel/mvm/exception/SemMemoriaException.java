package br.ariel.mvm.exception;

public class SemMemoriaException extends Exception {

	private static final long serialVersionUID = -8981896155834405452L;

	public SemMemoriaException() {
		super("Sem memória!");
	}
}
