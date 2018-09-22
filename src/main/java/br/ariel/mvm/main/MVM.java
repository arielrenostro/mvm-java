package br.ariel.mvm.main;

import java.awt.EventQueue;

import br.ariel.mvm.view.MVMView;


/**
 * @author ariel
 */
public class MVM {

	public static void main(String[] args) {
		EventQueue.invokeLater(() -> {
			try {
				MVMView frame = new MVMView();
				frame.setVisible(true);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}
}
