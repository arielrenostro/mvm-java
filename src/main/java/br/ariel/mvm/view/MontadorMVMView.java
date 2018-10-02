package br.ariel.mvm.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

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
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;

import br.ariel.mvm.controller.BiosController;
import br.ariel.mvm.controller.MVMController;
import br.ariel.mvm.model.Memoria;
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
	private JButton btnTestar;

	private static final Set<String> HIGHLIGHT_INSTRUCOES = new HashSet<>();
	private static final DefaultHighlighter.DefaultHighlightPainter GREEN_PAINTER = new DefaultHighlighter.DefaultHighlightPainter(Color.GREEN);
	private static final DefaultHighlighter.DefaultHighlightPainter RED_PAINTER = new DefaultHighlighter.DefaultHighlightPainter(Color.RED);

	static {
		HIGHLIGHT_INSTRUCOES.add("MOV");
		HIGHLIGHT_INSTRUCOES.add("INT");
		HIGHLIGHT_INSTRUCOES.add("ADD");
		HIGHLIGHT_INSTRUCOES.add("SUB");
		HIGHLIGHT_INSTRUCOES.add("DEC");
		HIGHLIGHT_INSTRUCOES.add("INC");
		HIGHLIGHT_INSTRUCOES.add("TEST");
		HIGHLIGHT_INSTRUCOES.add("JMP");
		HIGHLIGHT_INSTRUCOES.add("INIT");
		HIGHLIGHT_INSTRUCOES.add("POP");
		HIGHLIGHT_INSTRUCOES.add("PUSH");
		HIGHLIGHT_INSTRUCOES.add("ORG");
		HIGHLIGHT_INSTRUCOES.add("IRET");
	}

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
		txtCodigo.addKeyListener(criarEventoClickCodigo());
		scrollPane.setViewportView(txtCodigo);

		JPanel panel = new JPanel();
		contentPane.add(panel, BorderLayout.SOUTH);

		btnMontar = new JButton("Montar e salvar");
		btnMontar.setFont(new Font("Tahoma", Font.PLAIN, 20));
		panel.add(btnMontar);

		btnTestar = new JButton("Montar e testar");
		btnTestar.setFont(new Font("Tahoma", Font.PLAIN, 20));
		panel.add(btnTestar);

		scrollPane.setRowHeaderView(new TextLineNumber(txtCodigo));

		criarEventosBotoes();
	}

	private KeyListener criarEventoClickCodigo() {
		return new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				txtCodigo.getHighlighter().removeAllHighlights();

				String text = txtCodigo.getText();
				Map<Integer, String> linhaPorIndice = getLinhasPorIndice(text);
				for (Entry<Integer, String> entry : linhaPorIndice.entrySet()) {
					Integer chave = entry.getKey();
					String valor = entry.getValue();
					if (valor.endsWith(":")) {
						try {
							txtCodigo.getHighlighter().addHighlight(chave, chave + valor.length(), RED_PAINTER);
						} catch (BadLocationException e1) {
							e1.printStackTrace();
						}
					}
					String instrucao = getInstrucao(valor);
					if (HIGHLIGHT_INSTRUCOES.contains(instrucao)) {
						try {
							txtCodigo.getHighlighter().addHighlight(chave, chave + instrucao.length(), GREEN_PAINTER);
						} catch (BadLocationException e1) {
							e1.printStackTrace();
						}
					}
				}
			}
		};
	}

	private Map<Integer, String> getLinhasPorIndice(String text) {
		Map<Integer, String> map = new HashMap<>();
		int idx = 0;
		int idxInicioLinha = 0;
		while (idx < text.length() && idx >= 0) {
			idx = text.indexOf('\n', idx + 1);
			if (idx > 0) {
				map.put(idxInicioLinha, text.substring(idxInicioLinha, idx).trim());
				idxInicioLinha = idx + 1;
			}
		}

		map.put(idxInicioLinha, text.substring(idxInicioLinha));
		return map;
	}

	private String getInstrucao(String text) {
		int indexOf = text.indexOf(' ');
		indexOf = indexOf > 0 ? indexOf : text.length();
		return text.substring(0, indexOf).trim();
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

		btnTestar.addActionListener((args) -> {
			try {
				byte[] codigoMontado = new MontadorMVM().montar(txtCodigo.getText());
				Memoria memoria = new MVMController().criarMemoriaPorArray(codigoMontado);

				ExecucaoMVMView execucaoMVMView = new ExecucaoMVMView();
				execucaoMVMView.executar(memoria, null);
			} catch (Exception e) {
				e.printStackTrace();
				DialogUtils.showDialogErro(this, e);
			}
		});
	}

	private String getCaminhoSalvarArquivo() {
		JFileChooser jFileChooser = new JFileChooser();
		jFileChooser.setFileFilter(new FileNameExtensionFilter("Binário", "bin"));
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
