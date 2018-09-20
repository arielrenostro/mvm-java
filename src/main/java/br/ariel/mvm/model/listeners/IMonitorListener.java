package br.ariel.mvm.model.listeners;

public interface IMonitorListener {

	void notificar(short linha, short coluna, byte dado);
}
