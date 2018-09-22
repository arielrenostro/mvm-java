package br.ariel.mvm.view;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import br.ariel.mvm.controller.MVMController;
import br.ariel.mvm.controller.MemoriaController;
import br.ariel.mvm.exception.MVMException;
import br.ariel.mvm.exception.PosicaoMemoriaInvalidaException;
import br.ariel.mvm.model.ContextoMVM;
import br.ariel.mvm.model.Memoria;
import br.ariel.mvm.model.Monitor;
import br.ariel.mvm.model.Processador;
import br.ariel.mvm.utils.DialogUtils;

/**
 * @author ariel
 */
public class ExecucaoMVMView extends JFrame {

	private static final long serialVersionUID = 663163476765601916L;

	private static final int TIMEOUT_ATUALIZACAO = 1000;

	private ExecutorService executor = Executors.newFixedThreadPool(1);

	private JPanel contentPane;
	private JLabel lblAX;
	private JLabel lblAL;
	private JLabel lblAH;
	private JLabel lblBX;
	private JLabel lblBL;
	private JLabel lblBH;
	private JLabel lblCX;
	private JLabel lblCL;
	private JLabel lblCH;
	private JLabel lblBP;
	private JLabel lblBPL;
	private JLabel lblBPH;
	private JLabel lblSP;
	private JLabel lblIP;
	private JComboBox<String> cbTipoVisualizacaoRegistrador;
	private JComboBox<String> cbTipoVisualizacaoMemoria;

	private MonitorView monitorView = new MonitorView();
	private ContextoMVM contextoMVM;
	private Processador processador;
	private Memoria memoria;
	private Monitor monitor;
	private JTable table;

	public ExecucaoMVMView() {
		setResizable(false);
		setTitle("Execu\u00E7\u00E3o MVM");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 665, 527);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		JPanel panel_1 = new JPanel();
		panel_1.setBorder(null);
		contentPane.add(panel_1, BorderLayout.NORTH);
		panel_1.setLayout(new BorderLayout(5, 0));

		JPanel panel = new JPanel();
		panel_1.add(panel, BorderLayout.CENTER);
		panel.setLayout(new GridLayout(5, 6, 0, 0));

		JLabel lblNewLabel = new JLabel("AX");
		panel.add(lblNewLabel);

		lblAX = new JLabel("0");
		panel.add(lblAX);

		JLabel lblNewLabel_1 = new JLabel("AL");
		panel.add(lblNewLabel_1);

		lblAL = new JLabel("0");
		panel.add(lblAL);

		JLabel lblNewLabel_2 = new JLabel("AH");
		panel.add(lblNewLabel_2);

		lblAH = new JLabel("0");
		panel.add(lblAH);

		JLabel lblNewLabel_3 = new JLabel("BX");
		panel.add(lblNewLabel_3);

		lblBX = new JLabel("0");
		panel.add(lblBX);

		JLabel lblNewLabel_4 = new JLabel("BL");
		panel.add(lblNewLabel_4);

		lblBL = new JLabel("0");
		panel.add(lblBL);

		JLabel lblNewLabel_5 = new JLabel("BH");
		panel.add(lblNewLabel_5);

		lblBH = new JLabel("0");
		panel.add(lblBH);

		JLabel lblNewLabel_6 = new JLabel("CX");
		panel.add(lblNewLabel_6);

		lblCX = new JLabel("0");
		panel.add(lblCX);

		JLabel lblNewLabel_7 = new JLabel("CL");
		panel.add(lblNewLabel_7);

		lblCL = new JLabel("0");
		panel.add(lblCL);

		JLabel lblNewLabel_8 = new JLabel("CH");
		panel.add(lblNewLabel_8);

		lblCH = new JLabel("0");
		panel.add(lblCH);

		JLabel lblNewLabel_9 = new JLabel("BP");
		panel.add(lblNewLabel_9);

		lblBP = new JLabel("0");
		panel.add(lblBP);

		JLabel lblNewLabel_10 = new JLabel("BPL");
		panel.add(lblNewLabel_10);

		lblBPL = new JLabel("0");
		panel.add(lblBPL);

		JLabel lblNewLabel_11 = new JLabel("BPH");
		panel.add(lblNewLabel_11);

		lblBPH = new JLabel("0");
		panel.add(lblBPH);

		JLabel lblNewLabel_12 = new JLabel("IP");
		panel.add(lblNewLabel_12);

		lblIP = new JLabel("0");
		panel.add(lblIP);

		JLabel lblNewLabel_13 = new JLabel("SP");
		panel.add(lblNewLabel_13);

		lblSP = new JLabel("0");
		panel.add(lblSP);

		JPanel pnlTipoDadoMemoria = new JPanel();
		panel_1.add(pnlTipoDadoMemoria, BorderLayout.NORTH);
		pnlTipoDadoMemoria.setLayout(new GridLayout(0, 2, 0, 0));

		JLabel lblNewLabel_14 = new JLabel("Mem\u00F3ria");
		pnlTipoDadoMemoria.add(lblNewLabel_14);

		cbTipoVisualizacaoMemoria = new JComboBox<>();
		cbTipoVisualizacaoMemoria.setMaximumRowCount(3);
		cbTipoVisualizacaoMemoria.setModel(new DefaultComboBoxModel<>(new String[] {"Decimal", "Hexadecimal", "Bin\u00E1rio"}));
		cbTipoVisualizacaoMemoria.setSelectedIndex(2);
		pnlTipoDadoMemoria.add(cbTipoVisualizacaoMemoria);

		JLabel lblNewLabel_15 = new JLabel("Registradores");
		pnlTipoDadoMemoria.add(lblNewLabel_15);

		cbTipoVisualizacaoRegistrador = new JComboBox<>();
		cbTipoVisualizacaoRegistrador.setMaximumRowCount(3);
		cbTipoVisualizacaoRegistrador.setModel(new DefaultComboBoxModel<>(new String[] {"Decimal", "Hexadecimal", "Bin\u00E1rio"}));
		cbTipoVisualizacaoRegistrador.setSelectedIndex(2);
		pnlTipoDadoMemoria.add(cbTipoVisualizacaoRegistrador);

		table = new JTable();
		table.setModel(new DefaultTableModel(new Object[1][2], new String[] {"Índice", "Valor"}));

		JScrollPane scrollPane = new JScrollPane(table);
		contentPane.add(scrollPane, BorderLayout.CENTER);


		criarEventos();

		try {
			memoria = new Memoria((short) 50);
			memoria.setData((short) 0, (byte) 65);
			memoria.setData((short) 1, (byte) 123);
			memoria.setData((short) 2, (byte) 128);
			memoria.setData((short) 3, (byte) 146);
			memoria.setData((short) 4, (byte) 125);
			memoria.setData((short) 5, (byte) -124);
		} catch (PosicaoMemoriaInvalidaException e) {
			e.printStackTrace();
		}
	}

	public void executar(String caminhoBios) {
		Runnable run = () -> {
			try {
				processador = new Processador();
				contextoMVM = new ContextoMVM();
				memoria = new MemoriaController().criarMemoriaPorBios(caminhoBios);

				monitor = new Monitor();
				monitor.adicionarListener(monitorView);

				new MVMController().iniciar(processador, memoria, monitor, contextoMVM);
			} catch (MVMException e) {
				e.printStackTrace();
				DialogUtils.showDialogErro(this, e);
			} catch (Exception e) {
				e.printStackTrace();
			}
		};

		executor.execute(run);
		setVisible(true);
	}

	private TimerTask criarTask() {
		return new TimerTask() {

			@Override
			public void run() {
				try {
					atualizarCamposTela();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};
	}

	protected void atualizarCamposTela() throws PosicaoMemoriaInvalidaException {
		atualizarLabelsRegistradores();
		atualizarGridMemoria();
	}

	private void atualizarGridMemoria() throws PosicaoMemoriaInvalidaException {
		if (null != memoria) {
			int idxLinhaSelecionada = table.getSelectedRow();
			int idxColunaSelecionada = table.getSelectedColumn();

			if (-1 == idxColunaSelecionada) {
				idxColunaSelecionada = 0;
			}
			if (-1 == idxLinhaSelecionada) {
				idxLinhaSelecionada = 0;
			}

			TipoVisualizacaoNumero tipoVisualizacaoMemoria = getTipoVisualizacaoNumero(cbTipoVisualizacaoMemoria);

			short tamanho = memoria.getTamanho();
			String[][] dados = new String[tamanho][2];

			for (int idx = 0; idx < tamanho; idx++) {
				dados[idx][0] = getValorFormatado(tipoVisualizacaoMemoria, idx);
				dados[idx][1] = getValorFormatado(tipoVisualizacaoMemoria, memoria.getData((short) idx));
			}

			table.setModel(new DefaultTableModel(dados, new String[] {"Índice", "Valor"}));
			table.setRowSelectionInterval(idxLinhaSelecionada, idxLinhaSelecionada);
			table.setColumnSelectionInterval(idxColunaSelecionada, idxColunaSelecionada);
		}
	}

	private void atualizarLabelsRegistradores() {
		if (null != processador) {
			TipoVisualizacaoNumero tipoVisualizacaoRegistrador = getTipoVisualizacaoNumero(cbTipoVisualizacaoRegistrador);
			lblAX.setText(getValorFormatado(tipoVisualizacaoRegistrador, processador.getAx()));
			lblAH.setText(getValorFormatado(tipoVisualizacaoRegistrador, processador.getAh()));
			lblAL.setText(getValorFormatado(tipoVisualizacaoRegistrador, processador.getAl()));
			lblBX.setText(getValorFormatado(tipoVisualizacaoRegistrador, processador.getBx()));
			lblBH.setText(getValorFormatado(tipoVisualizacaoRegistrador, processador.getBh()));
			lblBL.setText(getValorFormatado(tipoVisualizacaoRegistrador, processador.getBl()));
			lblCX.setText(getValorFormatado(tipoVisualizacaoRegistrador, processador.getCx()));
			lblCL.setText(getValorFormatado(tipoVisualizacaoRegistrador, processador.getCl()));
			lblCH.setText(getValorFormatado(tipoVisualizacaoRegistrador, processador.getCh()));
			lblBP.setText(getValorFormatado(tipoVisualizacaoRegistrador, processador.getBp()));
			lblBPL.setText(getValorFormatado(tipoVisualizacaoRegistrador, processador.getBpl()));
			lblBPH.setText(getValorFormatado(tipoVisualizacaoRegistrador, processador.getBph()));
			lblIP.setText(getValorFormatado(tipoVisualizacaoRegistrador, processador.getIp()));
			lblSP.setText(getValorFormatado(tipoVisualizacaoRegistrador, processador.getSp()));
		}
	}

	private String getValorFormatado(TipoVisualizacaoNumero tipoVisualizacaoNumero, int valor) {
		StringBuilder stringBuilder = new StringBuilder();

		switch (tipoVisualizacaoNumero) {
			case HEXADECIMAL:
				stringBuilder.append("0x");
				stringBuilder.append(Integer.toHexString(valor).toUpperCase());
				corrigirValorFormatado(stringBuilder, 6);
				break;
			case DECIMAL:
				stringBuilder.append(valor);
				break;
			default:
				stringBuilder.append("Bx");
				stringBuilder.append(Integer.toBinaryString(valor));
				corrigirValorFormatado(stringBuilder, 10);
				stringBuilder.insert(6, " ");
		}

		return stringBuilder.toString();
	}

	private void corrigirValorFormatado(StringBuilder stringBuilder, int tamanho) {
		if (stringBuilder.length() > tamanho) {
			do {
				stringBuilder.deleteCharAt(2);
			} while (stringBuilder.length() > tamanho);
		} else if (stringBuilder.length() < tamanho) {
			do {
				stringBuilder.insert(2, "0");
			} while (stringBuilder.length() < tamanho);
		}
	}

	private TipoVisualizacaoNumero getTipoVisualizacaoNumero(JComboBox<String> cbTipoVisualizacao) {
		Object selectedItem = cbTipoVisualizacao.getSelectedItem();
		if (null != selectedItem) {
			if ("Decimal".equals(selectedItem)) {
				return TipoVisualizacaoNumero.DECIMAL;
			} else if ("Hexadecimal".equals(selectedItem)) {
				return TipoVisualizacaoNumero.HEXADECIMAL;
			}
		}
		return TipoVisualizacaoNumero.BINARIO;
	}

	private void criarEventos() {
		Timer timer = new Timer();

		addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent e) {
				executor.shutdown();
				timer.cancel();

				monitorView.setVisible(false);
				monitorView.dispose();
				setVisible(false);
				dispose();
			}
		});

		TimerTask task = criarTask();
		timer.scheduleAtFixedRate(task, 100, TIMEOUT_ATUALIZACAO);
	}

}
