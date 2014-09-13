package eu.bibl.launcher.game;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import com.mojang.authlib.yggdrasil.YggdrasilUserAuthentication;

import eu.bibl.launcher.FileConstants;
import eu.bibl.launcher.profile.MinecraftProfile;
import eu.bibl.launcher.version.json.ExtractionInfo;
import eu.bibl.launcher.version.json.MinecraftLibrary;
import eu.bibl.launcher.version.json.MinecraftVersion;

public final class LaunchManager {
	
	private static LaunchManager latestManager;
	
	private List<File> libs;
	private List<String> args;
	
	private LaunchManager() {
		if (latestManager != null)
			throw new UnsupportedOperationException("Game already running");
		libs = new ArrayList<File>();
		args = new ArrayList<String>();
		
		args.add("java");
	}
	
	private void addLib(File file) {
		libs.add(file);
	}
	
	private void addCpArgs() {
		if (libs != null) {
			args.add("-Djava.library.path=" + FileConstants.CLIENT_NATIVES_DIR.getAbsolutePath());
			args.add("-cp");
			StringBuilder builder = new StringBuilder();
			for (int i = 0; i < libs.size(); i++) {
				File lib = libs.get(i);
				builder.append(lib.getAbsolutePath());
				if (!((i + 1) >= libs.size())) {
					builder.append(";");
				}
			}
			args.add(builder.toString());
			libs = null;
		}
	}
	
	private void addGameRunArg(String argName, String argVal) {
		args.add(argName);
		args.add(argVal);
	}
	
	private void add(String val) {
		args.add(val);
	}
	
	private Process startGame() throws IOException {
		System.out.println(Arrays.toString(args.toArray()));
		ProcessBuilder builder = new ProcessBuilder(args);
		builder.redirectErrorStream(true);
		return builder.start();
	}
	
	public static void launch(MinecraftVersion version, MinecraftProfile profile, YggdrasilUserAuthentication userAuth) throws Exception {
		LaunchManager launchManager = new LaunchManager();
		
		List<MinecraftLibrary> libs = version.getLibraries();
		for (MinecraftLibrary lib : libs) {
			File file = lib.getLibraryFile();
			if (!file.exists()) {
				System.out.println("Missing library: " + file.getAbsolutePath());
				continue;
			}
			if (lib.getNatives() != null) {
				unpackNatives(FileConstants.CLIENT_NATIVES_DIR, lib);
			} else {
				launchManager.addLib(file);
			}
		}
		launchManager.addLib(version.getJarDir());
		
		launchManager.addCpArgs();
		launchManager.add(version.getMainClass());
		
		String[] args = resolveArgs(version.getMinecraftArguments(), version, profile, userAuth);
		for (int i = 0; i < args.length; i += 2) {
			launchManager.addGameRunArg(args[i], args[i + 1]);
		}
		
		Process process = launchManager.startGame();
		ProcessMonitor monitor = new ProcessMonitor(process);
		monitor.start();
	}
	
	private static String[] resolveArgs(String template, MinecraftVersion version, MinecraftProfile profile, YggdrasilUserAuthentication auth) {
		String[] parts = template.split(" ");
		for (int i = 1; i < parts.length; i += 2) {
			String argVar = parts[i];
			switch (argVar) {
				case "${auth_player_name}":
					parts[i] = profile.getGameUsername();
					break;
				case "${version_name}":
					parts[i] = version.getId();
					break;
				case "${game_directory}":
					parts[i] = FileConstants.MC_DIR.getAbsolutePath();
					break;
				case "${assets_root}":
					parts[i] = FileConstants.ASSETS_DIR.getAbsolutePath();
					break;
				case "${assets_index_name}":
					parts[i] = version.getAssets();
					break;
				case "${auth_uuid}":
					parts[i] = profile.getClientToken();
					break;
				case "${auth_access_token}":
					parts[i] = profile.getAuthenticatedToken();
					break;
				case "${user_properties}":
					parts[i] = auth.getUserProperties().toString();
					break;
				case "${user_type}":
					parts[i] = auth.getUserType().getName();
					break;
			}
		}
		return parts;
	}
	
	private static void unpackNatives(File targetDir, MinecraftLibrary lib) throws IOException {
		File file = lib.getLibraryFile();
		ZipFile zip = new ZipFile(file);
		ExtractionInfo extractRules = lib.getExtractionInfo();
		try {
			Enumeration<? extends ZipEntry> entries = zip.entries();
			while (entries.hasMoreElements()) {
				ZipEntry entry = entries.nextElement();
				if ((extractRules == null) || (extractRules.shouldExtract(entry.getName()))) {
					File targetFile = new File(targetDir, entry.getName());
					if (targetFile.getParentFile() != null)
						targetFile.getParentFile().mkdirs();
					if (!entry.isDirectory()) {
						BufferedInputStream inputStream = new BufferedInputStream(zip.getInputStream(entry));
						byte[] buffer = new byte[2048];
						FileOutputStream outputStream = new FileOutputStream(targetFile);
						BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(outputStream);
						try {
							int length;
							while ((length = inputStream.read(buffer, 0, buffer.length)) != -1)
								bufferedOutputStream.write(buffer, 0, length);
						} finally {
							bufferedOutputStream.close();
							outputStream.close();
							inputStream.close();
						}
					}
				}
			}
		} finally {
			zip.close();
		}
	}
}