package br.ariel.mvm.view;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;

public class MonitorView extends JFrame {

	private static final long serialVersionUID = 4497547218815620322L;

	private JPanel contentPane;
	private JTable table;

	public MonitorView() {
		setTitle("Monitor");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		table = new JTable();
		contentPane.add(table, BorderLayout.CENTER);
	}

}
