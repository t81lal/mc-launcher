package eu.bibl.launcher.version.json;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public final class MinecraftVersion {
	
	private String id;
	private String time;
	private String releaseTime;
	private String type;
	private String minecraftArguments;
	private List<MinecraftLibrary> libraries = new ArrayList<MinecraftLibrary>();
	private String mainClass;
	private int minimumLauncherVersion;
	private String assets;
	private File jarDir;
	
	public String getId() {
		return id;
	}
	
	public String getTime() {
		return time;
	}
	
	public String getReleaseTime() {
		return releaseTime;
	}
	
	public String getType() {
		return type;
	}
	
	public String getMinecraftArguments() {
		return minecraftArguments;
	}
	
	public List<MinecraftLibrary> getLibraries() {
		return libraries;
	}
	
	public String getMainClass() {
		return mainClass;
	}
	
	public int getMinimumLauncherVersion() {
		return minimumLauncherVersion;
	}
	
	public String getAssets() {
		return assets;
	}
	
	public File getJarDir() {
		return jarDir;
	}
	
	public void setJarDir(File jarFile) {
		jarDir = jarFile;
	}
	
	@Override
	public String toString() {
		return id;
	}
}