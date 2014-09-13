package eu.bibl.launcher;

import java.awt.Toolkit;
import java.io.File;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import eu.bibl.launcher.ui.LauncherFrame;

public class MinecraftLauncher {
	
	public static void main(String[] args) {
		for (File file : FileConstants.REQUIRED_DIRS) {
			if (!file.exists()) {
				file.mkdirs();
			}
		}
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		Toolkit.getDefaultToolkit().setDynamicLayout(true);
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				new LauncherFrame();
			}
		});
		// VersionsProvider provider = new MinecraftDirectoryVersionsProvider(FileConstants.VERSIONS_DIR);
		// provider.load();
		//
		// for (Library lib : provider.getLibraries()) {
		// if (lib.getNatives() != null) {
		// toExtract.add(lib);
		// } else {
		// libCp += lib.getLibraryFile().getAbsolutePath() + ";";
		// }
		// }
	}
}