package br.ariel.mvm.utils;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import br.ariel.mvm.view.MVMView;

/**
 * @author ariel
 */
public class DialogUtils {

	public static void showDialogErro(JFrame frame, Throwable e) {
		StringBuilder erro = new StringBuilder();

		if (e.getSuppressed().length > 0) {
			for (Throwable throwable : e.getSuppressed()) {
				erro.append(throwable.getMessage());
				erro.append("\n");
			}
		} else {
			erro.append(e.getMessage());
		}

		JOptionPane.showMessageDialog(frame, erro.toString(), "Erro", JOptionPane.ERROR_MESSAGE);
	}

	public static void showDialogInfo(JFrame frame, String msg) {
		JOptionPane.showMessageDialog(frame, msg, "Informação", JOptionPane.INFORMATION_MESSAGE);
	}

	public static void showDialogErro(MVMView frame, String mensagem) {
		JOptionPane.showMessageDialog(frame, mensagem, "Erro", JOptionPane.ERROR_MESSAGE);
	}

}
