package br.ariel.mvm.exception;

public class EnderecoCargaSuperiorMemoriaException extends MVMException {

	private static final long serialVersionUID = 2607505045995060269L;

	public EnderecoCargaSuperiorMemoriaException() {
		super("O endereço de carga informado é superior ao tamanho máximo da memória");
	}

}
