package br.ariel.mvm.main;

import java.awt.EventQueue;

import br.ariel.mvm.view.MonitorView;

public class MVM {

	public static void main(String[] args) {
		EventQueue.invokeLater(() -> {
			try {
				MonitorView frame = new MonitorView();
				frame.setVisible(true);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}
}
