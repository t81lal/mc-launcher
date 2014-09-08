package eu.bibl.launcher;

import java.awt.Toolkit;
import java.io.File;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import eu.bibl.launcher.ui.LauncherFrame;

public class McLauncher {
	
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
	
	// private static void unpackNatives(File targetDir, List<Library> toExtract) throws IOException {
	// // u want me to rewrite whole method? yes this just copied it wont work
	// for (Library lib : toExtract) {
	// Map<String, String> natives = lib.getNatives();
	// if (natives == null) {
	// System.out.println("Null natives: " + lib.getName());
	// continue;
	// }
	//
	// if (natives.size() == 0) {
	// System.out.println("No natives: " + lib.getName());
	// continue;
	// }
	//
	// OperatingSystem os = OperatingSystem.getCurrentPlatform();
	// String name = natives.get(os.getName());
	// boolean is64bit = false;
	// if (os == OperatingSystem.WINDOWS) {
	// is64bit = (System.getenv("ProgramFiles(x86)") != null);
	// } else {
	// is64bit = (System.getProperty("os.arch").indexOf("64") != -1);
	// }
	// name = name.replace("${arch}", is64bit ? "64" : "32");
	// File file = lib.getNativeLibraryFile(name);
	// ZipFile zip = new ZipFile(file);
	// ExtractionInfo extractRules = lib.getExtract();
	// try {
	// Enumeration<? extends ZipEntry> entries = zip.entries();
	// while (entries.hasMoreElements()) {
	// ZipEntry entry = entries.nextElement();
	// if ((extractRules == null) || (extractRules.shouldExtract(entry.getName()))) {
	// File targetFile = new File(targetDir, entry.getName());
	// if (targetFile.getParentFile() != null)
	// targetFile.getParentFile().mkdirs();
	// if (!entry.isDirectory()) {
	// BufferedInputStream inputStream = new BufferedInputStream(zip.getInputStream(entry));
	// byte[] buffer = new byte[2048];
	// FileOutputStream outputStream = new FileOutputStream(targetFile);
	// BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(outputStream);
	// try {
	// int length;
	// while ((length = inputStream.read(buffer, 0, buffer.length)) != -1)
	// bufferedOutputStream.write(buffer, 0, length);
	// } finally {
	// bufferedOutputStream.close();
	// outputStream.close();
	// inputStream.close();
	// }
	// }
	// }
	// }
	// } finally {
	// zip.close();
	// }
	// }
	// }
}