package br.ariel.mvm.model;

/**
 * @author ariel
 */
public class ContextoMVM {

	private boolean dispositivoSelecionado = false;
	private TipoDispositivo tipoDispositivo;

	private InstrucaoProcessador instrucaoAtual;

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

	public InstrucaoProcessador getInstrucaoAtual() {
		return instrucaoAtual;
	}

	public void setInstrucaoAtual(InstrucaoProcessador instrucaoAtual) {
		this.instrucaoAtual = instrucaoAtual;
	}

}
