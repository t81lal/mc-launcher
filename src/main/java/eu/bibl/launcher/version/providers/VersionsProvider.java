package eu.bibl.launcher.version.providers;

import java.util.ArrayList;
import java.util.List;

import eu.bibl.launcher.version.json.MinecraftVersion;

public abstract class VersionsProvider {
	
	protected List<MinecraftVersion> loadedVersions;
	
	public VersionsProvider() {
		loadedVersions = new ArrayList<MinecraftVersion>();
	}
	
	public abstract void load() throws Exception;
	
	public MinecraftVersion getByName(String name) {
		if (name == null)
			return null;
		for (MinecraftVersion version : loadedVersions) {
			if (name.equals(version.getId()))
				return version;
		}
		return null;
	}
	
	public final List<MinecraftVersion> getLoadedVersions() {
		return loadedVersions;
	}
}