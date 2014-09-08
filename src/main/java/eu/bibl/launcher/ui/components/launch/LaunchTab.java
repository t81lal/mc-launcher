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
		super(new GridLayout(1, 1));
		this.profileProvider = profileProvider;
		this.versionsProvider = versionsProvider;
		
		initUI();
	}
	
	private void initUI() {
		List<MinecraftProfile> profiles = profileProvider.getLoadedProfiles();
		List<MinecraftVersion> versions = versionsProvider.getLoadedVersions();
		JComboBox<MinecraftProfile> profileComboBox = new JComboBox<MinecraftProfile>(profiles.toArray(new MinecraftProfile[profiles.size()]));
		JComboBox<MinecraftVersion> versionComboBox = new JComboBox<MinecraftVersion>(versions.toArray(new MinecraftVersion[versions.size()]));
        ImagePanel imgPanel = null;
        try {
            imgPanel = new ImagePanel(this.getClass().getClassLoader().getResourceAsStream("rsrc/dank.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        JPanel tempPanel = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
        if (imgPanel != null) {
            tempPanel.add(imgPanel, gbc);
        } else {
            tempPanel.add(profileComboBox, gbc);
        }
		gbc.gridx = 0;
		gbc.gridy = 1;
        if (imgPanel != null) {
            tempPanel.add(profileComboBox, gbc);
        } else {
            tempPanel.add(versionComboBox, gbc);
        }

        if (imgPanel != null) {
            gbc.gridx = 0;
            gbc.gridy = 2;
            tempPanel.add(versionComboBox, gbc);
        }
		
		add(tempPanel);
	}
}