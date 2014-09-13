package eu.bibl.launcher.version.json;

import java.io.File;
import java.util.List;

import eu.bibl.launcher.FileConstants;
import eu.bibl.launcher.OperatingSystem;

public final class MinecraftLibrary {
	
	private String name;
	private List<LibraryRules> libraryRules;
	private LibraryNatives natives;
	private ExtractionInfo extractionInfo;
	
	public String getName() {
		return name;
	}
	
	public List<LibraryRules> getRules() {
		return libraryRules;
	}
	
	public LibraryNatives getNatives() {
		return natives;
	}
	
	public ExtractionInfo getExtractionInfo() {
		return extractionInfo;
	}
	
	public File getLibraryFile() {
		String[] splitParts = name.split(":");
		StringBuilder sb = new StringBuilder();
		sb.append(splitParts[0].replace(".", "/")); // basedir
		sb.append("/");
		sb.append(splitParts[1]); // name of folder & file
		sb.append("/");
		sb.append(splitParts[2]); // version
		sb.append("/"); // seperator before the real file name
		sb.append(splitParts[1]);
		sb.append("-");
		sb.append(splitParts[2]);
		if (natives != null) {
			sb.append("-");
			OperatingSystem os = OperatingSystem.getCurrentPlatform();
			boolean is64bit = false;
			if (os == OperatingSystem.WINDOWS) {
				is64bit = (System.getenv("ProgramFiles(x86)") != null);
			} else {
				is64bit = (System.getProperty("os.arch").indexOf("64") != -1);
			}
			sb.append(natives.getValue(os).replace("${arch}", is64bit ? "64" : "32"));
		}
		sb.append(".jar");
		
		File file = new File(FileConstants.LIBRARIES_DIR, sb.toString());
		return file;
	}
}