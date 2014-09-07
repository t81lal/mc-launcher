package eu.bibl.launcher.ui.components.profiles;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
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
		
		final JPopupMenu popupMenu = new JPopupMenu();
		JMenuItem deleteItem = new JMenuItem("Remove");
		deleteItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int row = getSelectedRow();
				MinecraftProfile profile = ((ProfileTableModel) getModel()).getProfileAtRow(row);
				if (profile != null) {
					try {
						if (provider.isSelectedProfile(profile)) {
							provider.setSelectedProfile(null);
						}
						provider.removeProfile(profile);
						((ProfileTableModel) getModel()).remove(profile);
					} catch (Exception e1) {
						e1.printStackTrace();
						JOptionPane.showMessageDialog(AccountsJTable.this, "Error deleting: " + e1.getMessage(), "Error removing profile", JOptionPane.ERROR_MESSAGE);
					}
				}
				
			}
		});
		JMenuItem selectItem = new JMenuItem("Set selected");
		selectItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int row = getSelectedRow();
				MinecraftProfile profile = ((ProfileTableModel) getModel()).getProfileAtRow(row);
				provider.setSelectedProfile(profile);
			}
		});
		popupMenu.add(selectItem);
		popupMenu.add(deleteItem);
		setComponentPopupMenu(popupMenu);
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
		if ((row >= 0) && (row < getRowCount())) {
			setRowSelectionInterval(row, row);
		} else {
			clearSelection();
		}
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