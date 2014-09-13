package eu.bibl.launcher.profile.events;

import eu.bibl.eventbus.Event;
import eu.bibl.launcher.profile.MinecraftProfile;

public class ProfileSelectEvent implements Event {
	
	private MinecraftProfile profile;
	
	public ProfileSelectEvent(MinecraftProfile profile) {
		this.profile = profile;
	}
	
	public MinecraftProfile getProfile() {
		return profile;
	}
}