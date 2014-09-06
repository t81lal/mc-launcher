package eu.bibl.launcher.version.providers.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import eu.bibl.banalysis.AnalysisCore;
import eu.bibl.launcher.version.json.MinecraftVersion;
import eu.bibl.launcher.version.providers.VersionsProvider;

public class MinecraftDirectoryVersionsProvider extends VersionsProvider {
	
	private final File dir;
	
	public MinecraftDirectoryVersionsProvider(File dir) {
		this.dir = dir;
	}
	
	@Override
	public void load() throws Exception {
		if (!dir.exists())
			throw new IOException("Missing versions directory!");
		File[] versionsFolders = dir.listFiles();
		for (File folder : versionsFolders) {
			String name = folder.getName();
			File jsonFile = new File(folder, name + ".json");
			File jarFile = new File(folder, name + ".jar");
			
			try {
				if (!jarFile.exists())
					throw new IOException(jarFile.getAbsolutePath() + " does not exist!");
			} catch (IOException e) {
				System.out.println(e.getMessage() + ", skipping.");
				continue;
			}
			
			try {
				String json = readFile(jsonFile);
				MinecraftVersion version = AnalysisCore.GSON_INSTANCE.fromJson(json, MinecraftVersion.class);
				version.setJarDir(jarFile);
				loadedVersions.add(version);
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
}