package eu.bibl.launcher.ui.components.launch;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.IOException;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.mojang.authlib.exceptions.AuthenticationException;
import com.mojang.authlib.yggdrasil.YggdrasilUserAuthentication;

import eu.bibl.config.Config;
import eu.bibl.eventbus.BusRegistry;
import eu.bibl.eventbus.EventPriority;
import eu.bibl.eventbus.EventTarget;
import eu.bibl.launcher.game.LaunchManager;
import eu.bibl.launcher.game.events.GameShutdownEvent;
import eu.bibl.launcher.game.events.GameStartEvent;
import eu.bibl.launcher.profile.MinecraftProfile;
import eu.bibl.launcher.profile.events.ProfileAddEvent;
import eu.bibl.launcher.profile.events.ProfileRemoveEvent;
import eu.bibl.launcher.profile.events.ProfileSelectEvent;
import eu.bibl.launcher.profile.providers.ProfileProvider;
import eu.bibl.launcher.ui.components.img.ImagePanel;
import eu.bibl.launcher.version.json.MinecraftVersion;
import eu.bibl.launcher.version.providers.VersionsProvider;

public class LaunchTab extends JPanel implements ActionListener, Runnable, ItemListener {
	
	private static final long serialVersionUID = 7139353926454146541L;
	
	public static final String SELECTED_VERSION_KEY = "version.selected";
	
	private ProfileProvider profileProvider;
	private VersionsProvider versionsProvider;
	private JComboBox<MinecraftProfile> profileComboBox;
	private JComboBox<MinecraftVersion> versionComboBox;
	private JButton launchButton;
	
	public LaunchTab(ProfileProvider profileProvider, VersionsProvider versionsProvider) {
		super(new GridLayout(2, 1));
		this.profileProvider = profileProvider;
		this.versionsProvider = versionsProvider;
		
		initUI();
	}
	
	@EventTarget(priority = EventPriority.HIGHEST)
	public void onProfileAddEvent(ProfileAddEvent e) {
		MinecraftProfile profile = e.getProfile();
		if (profile == null)
			return;
		profileComboBox.addItem(profile);
		if (profileProvider.isSelectedProfile(profile)) {
			profileComboBox.setSelectedItem(profile);
		}
	}
	
	@EventTarget(priority = EventPriority.HIGHEST)
	public void onProfileRemoveEvent(ProfileRemoveEvent e) {
		MinecraftProfile profile = e.getProfile();
		if (profile == null)
			return;
		profileComboBox.removeItem(profile);
	}
	
	@EventTarget(priority = EventPriority.HIGHEST)
	public void onProfileSelectEvent(ProfileSelectEvent e) {
		MinecraftProfile profile = e.getProfile();
		if (profile == null)
			return;
		profileComboBox.setSelectedItem(profile);
	}
	
	private void initUI() {
		List<MinecraftProfile> profiles = profileProvider.getLoadedProfiles();
		List<MinecraftVersion> versions = versionsProvider.getLoadedVersions();
		profileComboBox = new JComboBox<MinecraftProfile>(profiles.toArray(new MinecraftProfile[profiles.size()]));
		versionComboBox = new JComboBox<MinecraftVersion>(versions.toArray(new MinecraftVersion[versions.size()]));
		versionComboBox.addItemListener(this);
		MinecraftVersion version = versionsProvider.getByName(Config.GLOBAL_CONFIG.getValue(SELECTED_VERSION_KEY));
		if (version != null) {
			versionComboBox.setSelectedItem(version);
		} else {
			Config.GLOBAL_CONFIG.setValue(SELECTED_VERSION_KEY, ((MinecraftVersion) versionComboBox.getSelectedItem()).getId());
		}
		
		profileComboBox.setFocusable(false);
		versionComboBox.setFocusable(false);
		
		BusRegistry.getInstance().getGlobalBus().register(this);
		
		launchButton = new JButton("Cheat!");
		// launchButton.setFont(launchButton.getFont().deriveFont(17F).deriveFont(Font.BOLD));
		launchButton.setFocusable(false);
		launchButton.addActionListener(this);
		
		ImagePanel imgPanel = null;
		try {
			imgPanel = new ImagePanel(this.getClass().getClassLoader().getResourceAsStream("dank.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		JPanel tempPanel = new JPanel(new GridBagLayout());
		
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.ipadx = 150;
		gbc.insets = new Insets(0, 0, 5, 0);
		tempPanel.add(profileComboBox, gbc);
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.insets = new Insets(0, 0, 5, 0);
		tempPanel.add(versionComboBox, gbc);
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		gbc.gridx = 0;
		gbc.gridy = 2;
		gbc.ipadx = 175;
		gbc.ipady = 30;
		gbc.insets = new Insets(5, 0, 0, 0);
		tempPanel.add(launchButton, gbc);
		
		gbc.gridheight = 2;
		if (imgPanel != null) {
			add(imgPanel);
		}
		add(tempPanel);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		new Thread(this).start();
	}
	
	@EventTarget(priority = EventPriority.HIGHEST)
	public void onGameStartEvent(GameStartEvent e) {
		launchButton.setText("Already cheating!");
		launchButton.setEnabled(false);
	}
	
	@EventTarget(priority = EventPriority.HIGHEST)
	public void onGameShutdownEvent(GameShutdownEvent e) {
		launchButton.setText("Cheat!");
		launchButton.setEnabled(true);
	}
	
	@Override
	public void run() {
		MinecraftProfile profile = (MinecraftProfile) profileComboBox.getSelectedItem();
		if (profile == null) {
			JOptionPane.showMessageDialog(this, "Invalid profile", "Launch error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		YggdrasilUserAuthentication auth = null;
		if (!profile.isLoggedIn(profileProvider)) {
			try {
				auth = profile.login(profileProvider);
			} catch (AuthenticationException e1) {
				e1.printStackTrace();
				JOptionPane.showMessageDialog(this, "Invalid profile: " + profile.getLoginUsername(), "Launch error", JOptionPane.ERROR_MESSAGE);
				return;
			}
		}
		
		MinecraftVersion version = (MinecraftVersion) versionComboBox.getSelectedItem();
		if (version == null) {
			JOptionPane.showMessageDialog(this, "Invalid version", "Launch error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		launchButton.setEnabled(false);
		
		try {
			LaunchManager.launch(version, profile, auth);
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, e.getMessage(), "Launch error", JOptionPane.ERROR_MESSAGE);
			BusRegistry.getInstance().getGlobalBus().dispatch(new GameShutdownEvent());
		}
	}
	
	@Override
	public void itemStateChanged(ItemEvent e) {
		if (e.getStateChange() == ItemEvent.SELECTED) {
			Config.GLOBAL_CONFIG.setValue(SELECTED_VERSION_KEY, ((MinecraftVersion) versionComboBox.getSelectedItem()).getId());
		}
	}
}