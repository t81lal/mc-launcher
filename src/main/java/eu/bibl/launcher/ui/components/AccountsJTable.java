package eu.bibl.launcher.ui.components;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import eu.bibl.launcher.profile.MinecraftProfile;
import eu.bibl.launcher.profile.providers.ProfileProvider;

public class AccountsJTable extends JTable implements MouseListener {
	
	private static final long serialVersionUID = -6135315095393752023L;
	
	private ProfilesTab tab;
	private ProfileProvider provider;
	
	public AccountsJTable(ProfilesTab profilesTab, ProfileProvider provider) {
		super(new DefaultTableModel(new Object[] {
				"Username",
		"Password" }, provider.getLoadedProfiles().size()));
		tab = profilesTab;
		this.provider = provider;
		init();
	}
	
	private void init() {
		ProfileTableModel model = new ProfileTableModel(provider);
		model.setTable(this);
		setModel(model);
		for (MinecraftProfile profile : provider.getLoadedProfiles()) {
			addProfile(profile);
		}
		setFillsViewportHeight(true);
		addMouseListener(this);
	}
	
	public void addProfile(MinecraftProfile profile) {
		ProfileTableModel model = (ProfileTableModel) getModel();
		model.addProfile(profile);
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
	}
	
	@Override
	public void mousePressed(MouseEvent e) {
		AccountsJTable table = (AccountsJTable) e.getSource();
		Point p = e.getPoint();
		int row = table.rowAtPoint(p);
		if (e.getClickCount() == 2) {
			MinecraftProfile profile = ((ProfileTableModel) table.getModel()).getProfileAtRow(row);
			tab.select(profile);
		}
	}
	
	@Override
	public void mouseReleased(MouseEvent e) {
	}
	
	@Override
	public void mouseEntered(MouseEvent e) {
	}
	
	@Override
	public void mouseExited(MouseEvent e) {
	}
}