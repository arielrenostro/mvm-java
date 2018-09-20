package br.ariel.mvm.model;

/**
 * @author ariel
 */
public class ContextoExecucao {

	private boolean dispositivoSelecionado = false;
	private TipoDispositivo tipoDispositivo;

	public boolean isDispositivoSelecionado() {
		return dispositivoSelecionado;
	}

	public void setDispositivoSelecionado(boolean dispositivoSelecionado) {
		this.dispositivoSelecionado = dispositivoSelecionado;
	}

	public TipoDispositivo getTipoDispositivo() {
		return tipoDispositivo;
	}

	public void setTipoDispositivo(TipoDispositivo tipoDispositivo) {
		this.tipoDispositivo = tipoDispositivo;
	}

}
