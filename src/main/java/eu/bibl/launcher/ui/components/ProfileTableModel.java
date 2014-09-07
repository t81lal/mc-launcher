package eu.bibl.launcher.ui.components;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;

import eu.bibl.launcher.profile.MinecraftProfile;
import eu.bibl.launcher.profile.providers.ProfileProvider;

public class ProfileTableModel extends DefaultTableModel implements TableModelListener, PropertyChangeListener, Runnable {
	
	private static final long serialVersionUID = 6661607031345003734L;
	
	private ProfileProvider profileProvider;
	private JTable table;
	
	// private Map<Integer, String> nameMap;
	
	public ProfileTableModel(ProfileProvider profileProvider) {
		super(new Object[] {
				"Username",
		"Password" }, 0);
		this.profileProvider = profileProvider;
		// nameMap = new HashMap<Integer, String>();
		addTableModelListener(this);
	}
	
	public void setTable(JTable table) {
		this.table = table;
		table.addPropertyChangeListener(this);
	}
	
	public void addProfile(MinecraftProfile profile) {
		// prevent duplicates
		Vector<?> dataVector = getDataVector();
		List<Integer> toRemove = new ArrayList<Integer>();
		for (int i = 0; i < getRowCount(); i++) {
			Vector<?> userNameVector = (Vector<?>) dataVector.get(i);
			String val = (String) userNameVector.get(0);
			if (val.equals(profile.getGameUsername())) {
				toRemove.add(i);
			}
		}
		if (toRemove.size() > 0) {
			profileProvider.clear();
			try {
				profileProvider.load();
			} catch (Exception e) {
				e.printStackTrace();
			}
			for (int i : toRemove) {
				dataVector.remove(i);
			}
		}
		super.addRow(new Object[] {
				profile.getGameUsername(),
				createStars(profile.getPassword().length()) });
	}
	
	private String createStars(int length) {
		String s = "";
		for (int i = 0; i < length; i++) {
			s += "*";
		}
		return s;
	}
	
	public MinecraftProfile getProfileAtRow(int row) {
		String val = (String) getValueAt(row, 0);
		return getByName(val);
	}
	
	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return false;
	}
	
	// private boolean state = false;
	//
	// @Override
	// public void tableChanged(TableModelEvent e) {
	// if (e.getType() == TableModelEvent.UPDATE) {
	// if (state) {
	// state = false;
	// return;
	// }
	// int row = table.getSelectedRow();
	// if (row == -1) {
	// return;
	// }
	//
	// state = true;
	//
	// String selectedName = (String) getValueAt(row, 0);
	// MinecraftProfile profile = getByName(nameMap.get(row));
	// if (profile == null) {
	// return;
	// }
	//
	// String oldLoginName = profile.getLoginUsername();
	// profile.setLoginUsername(selectedName);
	// String oldPassword = profile.getPassword();
	// profile.setPassword((String) table.getValueAt(row, 1));
	// try {
	// setValueAt(createStars(profile.getPassword().length()), row, 1);
	// profile.login();
	// } catch (AuthenticationException e1) {
	// e1.printStackTrace();
	// JOptionPane.showMessageDialog(null, "Error: " + e1.getMessage(), "Error logging in", JOptionPane.ERROR_MESSAGE);
	// setValueAt(oldLoginName, row, 0);
	// setValueAt(createStars(oldPassword.length()), row, 1);
	// profile.setLoginUsername(oldLoginName);
	// profile.setPassword(oldPassword);
	// return;
	// }
	// try {
	// File oldFile = new File(FileConstants.PROFILE_DIR, oldLoginName + ".profile.json");
	// if (oldFile.exists()) {
	// oldFile.delete();
	// File newFile = new File(FileConstants.PROFILE_DIR, selectedName + ".profile.json");
	// if (newFile.exists())
	// newFile.delete();
	// newFile.createNewFile();
	// profileProvider.removeProfile(profile);
	// profileProvider.saveProfile(profile);
	// }
	// } catch (Exception e1) {
	// e1.printStackTrace();
	// }
	// }
	// }
	
	public MinecraftProfile getByName(String name) {
		for (MinecraftProfile profile : profileProvider.getLoadedProfiles()) {
			if (name.equals(profile.getGameUsername()))
				return profile;
		}
		return null;
	}
	
	@Override
	public void run() {
	}
	
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
	}
	
	@Override
	public void tableChanged(TableModelEvent e) {
	}
	
	// @Override
	// public void propertyChange(PropertyChangeEvent e) {
	// if (e.getPropertyName().equals("tableCellEditor")) {
	// if (table.isEditing()) {
	// SwingUtilities.invokeLater(this);
	// }
	// }
	// }
	//
	// @Override
	// public void run() {
	// int row = table.getEditingRow();
	// String cellValue = (String) getValueAt(row, 0);
	// nameMap.put(table.getEditingRow(), cellValue);
	// }
}