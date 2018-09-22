package br.ariel.mvm.view;

import java.awt.BorderLayout;
import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import br.ariel.mvm.model.listeners.IMonitorListener;

public class MonitorView extends JFrame implements IMonitorListener {

	private static final long serialVersionUID = 4497547218815620322L;

	private static final int LINHAS = 5;
	private static final int COLUNAS = 40;

	private JPanel contentPane;
	private JTable table;

	public MonitorView() {
		setResizable(false);
		setTitle("Monitor");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 946, 134);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		table = new JTable();
		table.setEnabled(false);
		table.setRowSelectionAllowed(false);
		table.setShowGrid(false);
		table.setFont(new Font("Tahoma", Font.PLAIN, 20));
		contentPane.add(table, BorderLayout.CENTER);

		DefaultTableModel tableModel = criarModeloTabela();
		table.setModel(tableModel);

		alinharCamposTabela();
	}

	private void alinharCamposTabela() {
		DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer();
		cellRenderer.setHorizontalAlignment(JLabel.CENTER);
		table.setDefaultRenderer(String.class, cellRenderer);

		TableModel model = table.getModel();
		for (int colIdx = 0; colIdx < model.getColumnCount(); colIdx++) {
			table.getColumnModel().getColumn(colIdx).setCellRenderer(cellRenderer);
		}
	}

	private DefaultTableModel criarModeloTabela() {
		DefaultTableModel model = new DefaultTableModel(LINHAS, COLUNAS);
		return model;
	}

	@Override
	public void notificar(short linha, short coluna, byte dado) {
		if (!isVisible()) {
			setVisible(true);
		}
		table.getModel().setValueAt((char) dado, linha, coluna);
	}

}
