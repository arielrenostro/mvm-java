package br.ariel.mvm.exception;

public class HaltException extends MVMException {

	private static final long serialVersionUID = -7173865995333347619L;

	public HaltException() {
		super("HALT!");
	}

}
