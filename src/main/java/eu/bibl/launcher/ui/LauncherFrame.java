package eu.bibl.launcher.ui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import eu.bibl.launcher.ui.components.ProfilesTab;

public class LauncherFrame extends JFrame implements ActionListener {
	
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
		
		tabbedPane.add("Profiles", new ProfilesTab());
		
		getContentPane().add(panel);
		
		JMenuBar menuBar = new JMenuBar();
		JMenu fileMenu = new JMenu("File");
		JMenuItem exitItem = new JMenuItem("Exit");
		exitItem.setActionCommand("Exit");
		exitItem.addActionListener(this);
		fileMenu.add(exitItem);
		menuBar.add(fileMenu);
		
		setJMenuBar(menuBar);
	}
	
	@Override
	public void dispose() {
		super.dispose();
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("Exit")) {
			dispose();
		}
	}
}