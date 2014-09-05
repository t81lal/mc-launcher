package eu.bibl.launcher.ui;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

public class LauncherFrame extends JFrame {
	
	private static final long serialVersionUID = 2250029339405240593L;
	
	public LauncherFrame() {
		super("McLauncher - #Bibl");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setResizable(false);
		setLayout(new BorderLayout());
		
		initUI();
		
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
	}
	
	private void initUI() {
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(1, 1));
		JTabbedPane tabbedPane = new JTabbedPane();
		tabbedPane.setSize(600, 400);
		tabbedPane.setPreferredSize(tabbedPane.getSize());
		panel.add(tabbedPane);
		
		tabbedPane.addTab("Tab", new JPanel());
		
		getContentPane().add(panel);
	}
}