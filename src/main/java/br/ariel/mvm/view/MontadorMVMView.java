package br.ariel.mvm.view;

import java.awt.BorderLayout;
import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import br.ariel.mvm.controller.BiosController;
import br.ariel.mvm.montador.MontadorMVM;
import br.ariel.mvm.utils.DialogUtils;
import br.ariel.mvm.view.components.TextLineNumber;

/**
 * @author ariel
 */
public class MontadorMVMView extends JFrame {

	private static final long serialVersionUID = 1319430476026433172L;

	private JPanel contentPane;
	private JButton btnMontar;
	private JTextArea txtCodigo;

	public MontadorMVMView() {
		setTitle("Montador");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 807, 652);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		JLabel lblNewLabel = new JLabel("Montador MVM");
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 24));
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		contentPane.add(lblNewLabel, BorderLayout.NORTH);

		JScrollPane scrollPane = new JScrollPane();
		contentPane.add(scrollPane, BorderLayout.CENTER);

		txtCodigo = new JTextArea();
		scrollPane.setViewportView(txtCodigo);

		JPanel panel = new JPanel();
		contentPane.add(panel, BorderLayout.SOUTH);

		btnMontar = new JButton("Montar");
		btnMontar.setFont(new Font("Tahoma", Font.PLAIN, 20));
		panel.add(btnMontar);

		scrollPane.setRowHeaderView(new TextLineNumber(txtCodigo));

		criarEventosBotoes();
	}

	private void criarEventosBotoes() {
		btnMontar.addActionListener((args) -> {
			try {
				byte[] codigoMontado = new MontadorMVM().montar(txtCodigo.getText());
				String caminho = getCaminhoSalvarArquivo();
				if (!caminho.isEmpty()) {
					new BiosController().gerarArquivoBios(caminho, codigoMontado);
					DialogUtils.showDialogInfo(this, "Arquivo salvo!");
				}
			} catch (Exception e) {
				e.printStackTrace();
				DialogUtils.showDialogErro(this, e);
			}
		});
	}

	private String getCaminhoSalvarArquivo() {
		JFileChooser jFileChooser = new JFileChooser();
		jFileChooser.setFileFilter(new FileNameExtensionFilter("Bin�rio", "bin"));
		int resultado = jFileChooser.showSaveDialog(this);
		if (resultado == JFileChooser.APPROVE_OPTION) {
			String caminho = jFileChooser.getSelectedFile().getAbsolutePath();
			if (!caminho.endsWith(".bin")) {
				if (caminho.contains(".")) {
					caminho = caminho.substring(0, caminho.indexOf("."));
				}
				caminho = caminho.concat(".bin");
			}
			return caminho;
		}
		return "";
	}

}
