package eu.bibl.launcher.ui.components.launch;

import eu.bibl.launcher.profile.MinecraftProfile;
import eu.bibl.launcher.profile.providers.ProfileProvider;
import eu.bibl.launcher.ui.components.img.ImagePanel;
import eu.bibl.launcher.version.json.MinecraftVersion;
import eu.bibl.launcher.version.providers.VersionsProvider;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.List;

public class LaunchTab extends JPanel {
	
	private static final long serialVersionUID = 7139353926454146541L;
	
	private ProfileProvider profileProvider;
	private VersionsProvider versionsProvider;
	
	public LaunchTab(ProfileProvider profileProvider, VersionsProvider versionsProvider) {
		super(new GridLayout(2, 1));
		this.profileProvider = profileProvider;
		this.versionsProvider = versionsProvider;
		
		initUI();
	}
	
	private void initUI() {
		List<MinecraftProfile> profiles = profileProvider.getLoadedProfiles();
		List<MinecraftVersion> versions = versionsProvider.getLoadedVersions();
		JComboBox<MinecraftProfile> profileComboBox = new JComboBox<MinecraftProfile>(profiles.toArray(new MinecraftProfile[profiles.size()]));
		JComboBox<MinecraftVersion> versionComboBox = new JComboBox<MinecraftVersion>(versions.toArray(new MinecraftVersion[versions.size()]));

		JButton btn = new JButton();
        for (JComponent jc : new JComponent[] { profileComboBox, versionComboBox, btn }) {
            jc.setFocusable(false);
        }
		btn.setText("Launch!");
		ImagePanel imgPanel = null;
		try {
			imgPanel = new ImagePanel(this.getClass().getClassLoader().getResourceAsStream("dank.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		JPanel tempPanel = new JPanel(new GridBagLayout());
		
		Dimension d = new Dimension(120, 25);
		
		GridBagConstraints gbc = new GridBagConstraints();
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
		tempPanel.add(btn, gbc);
		
		gbc.gridheight = 2;
		if (imgPanel != null) {
			add(imgPanel);
		}
		add(tempPanel);
	}
}