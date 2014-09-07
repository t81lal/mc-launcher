package eu.bibl.launcher.version.json;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MinecraftVersion {
	private String id;
	private String time;
	private String releaseTime;
	private String type;
	private String minecraftArguments;
	private List<Library> libraries = new ArrayList<Library>();
	private String mainClass;
	private int minimumLauncherVersion;
	private String assets;
	private File jarDir;
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getTime() {
		return time;
	}
	
	public void setTime(String time) {
		this.time = time;
	}
	
	public String getReleaseTime() {
		return releaseTime;
	}
	
	public void setReleaseTime(String releaseTime) {
		this.releaseTime = releaseTime;
	}
	
	public String getType() {
		return type;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
	public String getMinecraftArguments() {
		return minecraftArguments;
	}
	
	public void setMinecraftArguments(String minecraftArguments) {
		this.minecraftArguments = minecraftArguments;
	}
	
	public List<Library> getLibraries() {
		return libraries;
	}
	
	public void setLibraries(List<Library> libraries) {
		this.libraries = libraries;
	}
	
	public String getMainClass() {
		return mainClass;
	}
	
	public void setMainClass(String mainClass) {
		this.mainClass = mainClass;
	}
	
	public int getMinimumLauncherVersion() {
		return minimumLauncherVersion;
	}
	
	public void setMinimumLauncherVersion(int minimumLauncherVersion) {
		this.minimumLauncherVersion = minimumLauncherVersion;
	}
	
	public String getAssets() {
		return assets;
	}
	
	public void setAssets(String assets) {
		this.assets = assets;
	}
	
	public File getJarDir() {
		return jarDir;
	}
	
	public void setJarDir(File directory) {
		jarDir = directory;
	}
	
	@Override
	public String toString() {
		return id;
	}
}