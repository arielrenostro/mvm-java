package br.ariel.mvm.view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.filechooser.FileNameExtensionFilter;

import br.ariel.mvm.utils.DialogUtils;

/**
 * @author ariel
 */
public class MVMView extends JFrame {

	private static final long serialVersionUID = 8056153562726655087L;

	CompiladorAssemblyMVMView compiladorAssemblyMVMView;

	public MVMView() {
		setResizable(false);
		setTitle("MVM - Mattos Virtual Machine");
		setBounds(100, 100, 424, 268);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JPanel panel = new JPanel();
		FlowLayout flowLayout = (FlowLayout) panel.getLayout();
		flowLayout.setVgap(10);
		flowLayout.setHgap(500);
		getContentPane().add(panel, BorderLayout.CENTER);

		JButton btnCompilar = new JButton("Compilador");
		btnCompilar.setFont(new Font("Tahoma", Font.PLAIN, 20));
		panel.add(btnCompilar);

		JButton btnExecutarBIOS = new JButton("Executar Bios");
		btnExecutarBIOS.setFont(new Font("Tahoma", Font.PLAIN, 20));
		panel.add(btnExecutarBIOS);

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

		btnCompilar.addActionListener((args) -> {
			if (null != compiladorAssemblyMVMView) {
				compiladorAssemblyMVMView.dispose();
			}
			compiladorAssemblyMVMView = new CompiladorAssemblyMVMView();
			compiladorAssemblyMVMView.setVisible(true);
		});

		btnExecutarBIOS.addActionListener((args) -> {
			String caminho = getCaminhoArquivo();
			if (!caminho.isEmpty()) {
				ExecucaoMVMView execucaoMVMView = new ExecucaoMVMView();
				execucaoMVMView.executar(caminho);
			} else {
				DialogUtils.showDialogErro(this, "Nenhum arquivo de BIOS selecionado");
			}
		});
	}

	private String getCaminhoArquivo() {
		JFileChooser jFileChooser = new JFileChooser();
		jFileChooser.setFileFilter(new FileNameExtensionFilter("Binário", "bin"));
		int resultado = jFileChooser.showOpenDialog(this);
		if (resultado == JFileChooser.APPROVE_OPTION) {
			return jFileChooser.getSelectedFile().getAbsolutePath();
		}
		return "";
	}

}
