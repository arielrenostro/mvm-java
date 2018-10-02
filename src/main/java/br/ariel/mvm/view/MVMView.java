package br.ariel.mvm.view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.filechooser.FileNameExtensionFilter;

import br.ariel.mvm.utils.DialogUtils;
import br.ariel.mvm.utils.Utils;

/**
 * @author ariel
 */
public class MVMView extends JFrame {

	private static final long serialVersionUID = 8056153562726655087L;

	MontadorMVMView montadorMVMView;

	public MVMView() {
		setResizable(false);
		setTitle("MVM - Mattos Virtual Machine");
		setBounds(100, 100, 419, 307);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JPanel panel = new JPanel();
		FlowLayout flowLayout = (FlowLayout) panel.getLayout();
		flowLayout.setVgap(10);
		flowLayout.setHgap(500);
		getContentPane().add(panel, BorderLayout.CENTER);

		JButton btnMontador = new JButton("Montador");
		btnMontador.setFont(new Font("Tahoma", Font.PLAIN, 20));
		panel.add(btnMontador);

		JButton btnExecutarBIOS = new JButton("Executar Bios");
		btnExecutarBIOS.setFont(new Font("Tahoma", Font.PLAIN, 20));
		panel.add(btnExecutarBIOS);

		JButton btnExecutarBIOSIncremento = new JButton("Executar Bios c/ carga");
		btnExecutarBIOSIncremento.setFont(new Font("Dialog", Font.PLAIN, 20));
		panel.add(btnExecutarBIOSIncremento);

		JPanel panel_1 = new JPanel();
		getContentPane().add(panel_1, BorderLayout.NORTH);
		panel_1.setLayout(new GridLayout(0, 1, 0, 0));

		JLabel lblNewLabel = new JLabel("Mattos Virtual Machine");
		panel_1.add(lblNewLabel);
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 24));
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);

		JLabel lblNewLabel_1 = new JLabel("<html>\r\n\t<div style=\"text-align: center\">\r\n\t\tDesenvolvedor: Ariel Adonai Souza<br>\r\n\t\tIdealizador: Prof. Mauro Marcelo Mattos<br>\r\n\t\tVers\u00E3o: Alpha 1.0\r\n\t</div>\r\n</html>");
		lblNewLabel_1.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblNewLabel_1.setHorizontalAlignment(SwingConstants.CENTER);
		panel_1.add(lblNewLabel_1);

		btnMontador.addActionListener((args) -> {
			if (null != montadorMVMView) {
				montadorMVMView.dispose();
			}
			montadorMVMView = new MontadorMVMView();
			montadorMVMView.setVisible(true);
		});

		btnExecutarBIOS.addActionListener((args) -> {
			String caminho = getCaminhoArquivo();
			if (!caminho.isEmpty()) {
				ExecucaoMVMView execucaoMVMView = new ExecucaoMVMView();
				execucaoMVMView.executar(caminho, null);
			} else {
				DialogUtils.showDialogErro(this, "Nenhum arquivo de BIOS selecionado");
			}
		});

		btnExecutarBIOSIncremento.addActionListener((args) -> {
			String caminho = getCaminhoArquivo();
			if (!caminho.isEmpty()) {
				String enderecoCargaStr = JOptionPane.showInputDialog(this, "Informe o endereço de carga: ");
				if (Utils.isNotNumber(enderecoCargaStr)) {
					DialogUtils.showDialogErro(this, "Endereço inválido");
				}
				int enderecoCarga = Integer.parseInt(enderecoCargaStr);
				if (enderecoCarga < 0) {
					DialogUtils.showDialogErro(this, "Endereço inválido");
				}

				ExecucaoMVMView execucaoMVMView = new ExecucaoMVMView();
				execucaoMVMView.executar(caminho, enderecoCarga);
			} else {
				DialogUtils.showDialogErro(this, "Nenhum arquivo de BIOS selecionado");
			}
		});
	}

	private String getCaminhoArquivo() {
		JFileChooser jFileChooser = new JFileChooser();
		jFileChooser.setFileFilter(new FileNameExtensionFilter("Bin�rio", "bin"));
		int resultado = jFileChooser.showOpenDialog(this);
		if (resultado == JFileChooser.APPROVE_OPTION) {
			return jFileChooser.getSelectedFile().getAbsolutePath();
		}
		return "";
	}

}
