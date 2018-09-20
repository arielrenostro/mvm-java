package br.ariel.mvm.model;

public enum TipoDispositivo {

	MONITOR(1025);

	private int code;

	private TipoDispositivo(int code) {
		this.code = code;
	}

	public int getCode() {
		return code;
	}

	public static TipoDispositivo getByCode(int code) {
		for (TipoDispositivo tipo : values()) {
			if (tipo.getCode() == code) {
				return tipo;
			}
		}

		return null;
	}
}
