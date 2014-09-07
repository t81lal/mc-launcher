package eu.bibl.launcher.ui.components.launch;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JPanel;

import eu.bibl.launcher.profile.MinecraftProfile;
import eu.bibl.launcher.profile.providers.ProfileProvider;
import eu.bibl.launcher.version.json.MinecraftVersion;
import eu.bibl.launcher.version.providers.VersionsProvider;

public class LaunchTab extends JPanel {
	
	private static final long serialVersionUID = 7139353926454146541L;
	
	private ProfileProvider profileProvider;
	private VersionsProvider versionsProvider;
	
	public LaunchTab(ProfileProvider profileProvider, VersionsProvider versionsProvider) {
		super(new GridLayout(4, 1));
		this.profileProvider = profileProvider;
		this.versionsProvider = versionsProvider;
		
		initUI();
	}
	
	private void initUI() {
		List<MinecraftProfile> profiles = profileProvider.getLoadedProfiles();
		List<MinecraftVersion> versions = versionsProvider.getLoadedVersions();
		JComboBox<MinecraftProfile> profileComboBox = new JComboBox<MinecraftProfile>(profiles.toArray(new MinecraftProfile[profiles.size()]));
		JComboBox<MinecraftVersion> versionComboBox = new JComboBox<MinecraftVersion>(versions.toArray(new MinecraftVersion[versions.size()]));
		
		JPanel tempPanel = new JPanel(new GridBagLayout());
		
		GridBagConstraints gbc = new GridBagConstraints();
		
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		tempPanel.add(profileComboBox, gbc);
		gbc.gridx = 0;
		gbc.gridy = 1;
		tempPanel.add(versionComboBox, gbc);
		
		add(tempPanel);
	}
}