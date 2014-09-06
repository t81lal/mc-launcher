package eu.bibl.launcher.profile.providers;

import java.util.ArrayList;
import java.util.List;

import eu.bibl.launcher.profile.MinecraftProfile;

public abstract class ProfileProvider {
	
	protected List<MinecraftProfile> loadedProfiles;
	
	public ProfileProvider() {
		loadedProfiles = new ArrayList<MinecraftProfile>();
	}
	
	public abstract void load() throws Exception;
	
	public abstract void clear();
	
	public abstract void saveProfile(MinecraftProfile profile) throws Exception;
	
	public abstract void removeProfile(MinecraftProfile profile) throws Exception;
	
	public final List<MinecraftProfile> getLoadedProfiles() {
		return loadedProfiles;
	}
}
