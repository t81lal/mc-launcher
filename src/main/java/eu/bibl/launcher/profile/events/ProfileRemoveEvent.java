package eu.bibl.launcher.profile.events;

import eu.bibl.eventbus.Event;
import eu.bibl.launcher.profile.MinecraftProfile;

public class ProfileRemoveEvent implements Event {
	
	private MinecraftProfile profile;
	
	public ProfileRemoveEvent(MinecraftProfile profile) {
		this.profile = profile;
	}
	
	public MinecraftProfile getProfile() {
		return profile;
	}
}