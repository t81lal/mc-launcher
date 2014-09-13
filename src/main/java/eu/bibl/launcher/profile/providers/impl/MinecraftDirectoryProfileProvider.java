package eu.bibl.launcher.profile.providers.impl;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import eu.bibl.banalysis.AnalysisCore;
import eu.bibl.eventbus.BusRegistry;
import eu.bibl.launcher.FileConstants;
import eu.bibl.launcher.profile.MinecraftProfile;
import eu.bibl.launcher.profile.events.ProfileAddEvent;
import eu.bibl.launcher.profile.events.ProfileRemoveEvent;
import eu.bibl.launcher.profile.providers.ProfileProvider;

public class MinecraftDirectoryProfileProvider extends ProfileProvider {
	
	private final File directory;
	
	public MinecraftDirectoryProfileProvider(final File directory) {
		this.directory = directory;
	}
	
	@Override
	public void load() throws Exception {
		if (!directory.exists()) {
			throw new IOException("Missing profile directory!");
		}
		
		File[] profileFolders = directory.listFiles();
		for (File jsonFile : profileFolders) {
			try {
				if (!jsonFile.exists()) {
					throw new IOException(jsonFile.getAbsolutePath() + " does not exist");
				}
			} catch (IOException exception) {
				System.out.println(exception.getMessage() + ", skipping.");
				continue;
			}
			
			try {
				String json = readFile(jsonFile);
				MinecraftProfile profile = AnalysisCore.GSON_INSTANCE.fromJson(json, MinecraftProfile.class);
				loadedProfiles.add(profile);
				BusRegistry.getInstance().getGlobalBus().dispatch(new ProfileAddEvent(profile));
			} catch (IOException e) {
				System.out.println("Unable to read json file: " + jsonFile.getAbsolutePath());
				e.printStackTrace();
			} catch (Exception e1) {
				System.out.println("Error parsing json file: " + jsonFile.getAbsolutePath());
				e1.printStackTrace();
			}
		}
	}
	
	private static String readFile(File file) throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(file));
		String total = "";
		String line;
		while ((line = reader.readLine()) != null) {
			total += line;
		}
		reader.close();
		return total;
	}
	
	@Override
	public void clear() {
		loadedProfiles.clear();
	}
	
	@Override
	public void saveProfile(MinecraftProfile profile) throws Exception {
		File newFile = new File(FileConstants.PROFILE_DIR, profile.getGameUsername() + ".profile.json");
		if (newFile.exists()) {
			newFile.delete();
		}
		
		newFile.createNewFile();
		BufferedWriter writer = new BufferedWriter(new FileWriter(newFile));
		String jsonString = AnalysisCore.GSON_INSTANCE.toJson(profile);
		writer.write(jsonString);
		writer.close();
		loadedProfiles.add(profile);
		BusRegistry.getInstance().getGlobalBus().dispatch(new ProfileAddEvent(profile));
	}
	
	@Override
	public void removeProfile(MinecraftProfile profile) throws Exception {
		File proFile = new File(FileConstants.PROFILE_DIR, profile.getGameUsername() + ".profile.json");
		if (proFile.exists()) {
			proFile.delete();
		}
		// fixed it
		loadedProfiles.remove(profile);
		// this
		BusRegistry.getInstance().getGlobalBus().dispatch(new ProfileRemoveEvent(profile));
	}
}
