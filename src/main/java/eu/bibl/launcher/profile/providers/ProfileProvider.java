package eu.bibl.launcher.profile.providers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mojang.authlib.yggdrasil.YggdrasilUserAuthentication;

import eu.bibl.config.Config;
import eu.bibl.eventbus.BusRegistry;
import eu.bibl.launcher.profile.MinecraftProfile;
import eu.bibl.launcher.profile.events.ProfileSelectEvent;

public abstract class ProfileProvider {
	
	public static final String SELECTED_PROFILE_KEY = "profile.selected";
	protected List<MinecraftProfile> loadedProfiles;
	protected Map<MinecraftProfile, YggdrasilUserAuthentication> authKeys;
	
	public ProfileProvider() {
		loadedProfiles = new ArrayList<>();
		authKeys = new HashMap<>();
	}
	
	public abstract void load() throws Exception;
	
	public abstract void clear();
	
	public abstract void saveProfile(MinecraftProfile profile) throws Exception;
	
	public abstract void removeProfile(MinecraftProfile profile) throws Exception;
	
	public YggdrasilUserAuthentication getAuth(MinecraftProfile profile) {
		return authKeys.get(profile);
	}
	
	public void authenticated(MinecraftProfile profile, YggdrasilUserAuthentication auth) {
		authKeys.put(profile, auth);
	}
	
	public void setSelectedProfile(MinecraftProfile selectedProfile) {
		Config.GLOBAL_CONFIG.setValue(SELECTED_PROFILE_KEY, selectedProfile == null ? null : selectedProfile.getGameUsername());
		BusRegistry.getInstance().getGlobalBus().dispatch(new ProfileSelectEvent(selectedProfile));
	}
	
	public MinecraftProfile getSelectedProfile() {
		String name = Config.GLOBAL_CONFIG.getValue(SELECTED_PROFILE_KEY, null);
		MinecraftProfile selectedProfile = getByName(name);
		return selectedProfile;
	}
	
	public boolean isSelectedProfile(MinecraftProfile profile) {
		if (profile == null)
			return false;
		MinecraftProfile selectedProfile = getSelectedProfile();
		if (selectedProfile == null)
			return false;
		return profile.equals(selectedProfile);
	}
	
	public MinecraftProfile getByName(String name) {
		if (name == null)
			return null;
		for (MinecraftProfile profile : loadedProfiles) {
			if (name.equals(profile.getGameUsername()))
				return profile;
		}
		return null;
	}
	
	public final List<MinecraftProfile> getLoadedProfiles() {
		return loadedProfiles;
	}
}