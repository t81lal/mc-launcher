package eu.bibl.launcher.ui.components.profiles;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextField;

import com.mojang.authlib.exceptions.AuthenticationException;

import eu.bibl.launcher.profile.MinecraftProfile;
import eu.bibl.launcher.profile.providers.ProfileProvider;

public class ProfilesTab extends JPanel implements ActionListener, Runnable {
	
	private static final long serialVersionUID = -6507666299757482416L;
	
	private ProfileProvider provider;
	private JTextField usernameField;
	private JPasswordField passwordField;
	private JButton loginButton;
	private AccountsJTable table;
	
	public ProfilesTab(ProfileProvider provider) {
		super(new GridLayout(1, 2));
		this.provider = provider;
		initUI();
	}
	
	private void initUI() {
		table = new AccountsJTable(this, provider);
		
		JPanel textFieldPanel = new JPanel(new GridBagLayout());
		
		GridBagConstraints gbc = new GridBagConstraints();
		
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.ipadx = 20;
		gbc.ipady = 3;
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = 1;
		textFieldPanel.add(new JLabel("Username:"), gbc);
		
		gbc.gridx = 1;
		gbc.gridy = 0;
		gbc.gridwidth = 2;
		usernameField = new JTextField(20);
		textFieldPanel.add(usernameField, gbc);
		
		gbc.insets = new Insets(5, 0, 0, 0);
		
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.gridwidth = 1;
		textFieldPanel.add(new JLabel("Password:"), gbc);
		
		gbc.gridx = 1;
		gbc.gridy = 1;
		gbc.gridwidth = 2;
		passwordField = new JPasswordField(20);
		textFieldPanel.add(passwordField, gbc);
		
		loginButton = new JButton("Login");
		loginButton.setFocusable(false);
		gbc.gridx = 0;
		gbc.gridy = 2;
		gbc.gridwidth = 3;
		textFieldPanel.add(loginButton, gbc);
		
		loginButton.addActionListener(this);
		usernameField.addActionListener(this);
		passwordField.addActionListener(this);
		
		// TODO: ADD PROPER BUTTONS SIZE AND POSITION
		JPanel buttonPanel = new JPanel(new GridBagLayout());
		
		gbc.anchor = GridBagConstraints.SOUTHWEST;
		gbc.ipadx = 30;
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = 1;
		// buttonPanel.add(loginButton, gbc);
		JButton removeButton = new JButton("Remove");
		removeButton.setFocusable(false);
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.gridwidth = 1;
		// buttonPanel.add(removeButton, gbc);
		
		JSplitPane mainSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		mainSplitPane.setResizeWeight(0.45D);
		mainSplitPane.add(new JScrollPane(table));
		
		JSplitPane vertSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		vertSplitPane.setResizeWeight(0.08D);
		vertSplitPane.add(textFieldPanel);
		vertSplitPane.add(buttonPanel);
		
		mainSplitPane.add(vertSplitPane);
		add(mainSplitPane);
	}
	
	private void attemptAddProfile() {
		new Thread(this).start();
	}
	
	@Override
	public void run() {
		usernameField.setEnabled(false);
		passwordField.setEnabled(false);
		loginButton.setEnabled(false);
		String username = usernameField.getText();
		String password = new String(passwordField.getPassword());
		MinecraftProfile profile = new MinecraftProfile(username, password);
		try {
			profile.login(provider);
			provider.saveProfile(profile);
			table.addProfile(profile);
			usernameField.setText("");
			passwordField.setText("");
			usernameField.requestFocus();
		} catch (AuthenticationException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(ProfilesTab.this, "Couldn't login: " + e.getMessage(), "Error logging in", JOptionPane.ERROR_MESSAGE);
			
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(ProfilesTab.this, "Couldn't save: " + e.getMessage(), "Error saving profile", JOptionPane.ERROR_MESSAGE);
		}
		
		usernameField.setEnabled(true);
		passwordField.setEnabled(true);
		loginButton.setEnabled(true);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(usernameField)) {
			attemptAddProfile();
		} else if (e.getSource().equals(passwordField)) {
			attemptAddProfile();
		} else if (e.getSource().equals(loginButton)) {
			attemptAddProfile();
		}
	}
	
	public void select(MinecraftProfile profile) {
		usernameField.setText(profile.getLoginUsername());
		passwordField.setText(profile.getPassword());
	}
}