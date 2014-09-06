package eu.bibl.launcher;

import java.io.File;

/**
 * A bunch of constant directories and files needed to the run the client.
 * @author Bibl
 */
public interface FileConstants {
	
	public static final String CLIENT_NAME = "mclauncher";
	
	/** The base Minecraft directory. **/
	public static final File MC_DIR = new File(System.getProperty("user.home") + "/AppData/Roaming/.minecraft");
	
	/** The downloaded assets directory for Minecraft. **/
	public static final File ASSETS_DIR = new File(MC_DIR, "assets");
	
	/** The downloaded libraries directory for Minecraft. **/
	public static final File LIBRARIES_DIR = new File(MC_DIR, "libraries");
	
	/** The directory containing all the launch versions of the game. **/
	public static final File VERSIONS_DIR = new File(MC_DIR, "versions");
	// public static final File NATIVES_DIR = new File(MC_DIR, "natives-temp");
	
	/** Client main directory. **/
	public static final File CLIENT_DIR = new File(MC_DIR, CLIENT_NAME);
	/** Base data directory. **/
	public static final File DATA_DIR = new File(CLIENT_DIR, "data");
	
	/** Launch profile data directory. **/
	public static final File PROFILE_DIR = new File(DATA_DIR, "profiles");
	
	/** Hook data directory. **/
	public static final File HOOKS_DIR = new File(DATA_DIR, "hooks");
	
	/** Extracted native directories. **/
	public static final File CLIENT_NATIVES_DIR = new File(DATA_DIR, "natives");
	
	/** User installed game plugins. **/
	public static final File PLUGINS_DIR = new File(DATA_DIR, "plugins");
	
	/** Location of the global config file. **/
	public static final File CONFIG_FILE = new File(DATA_DIR, "global.config");
	
	public static final File[] REQUIRED_DIRS = new File[] {
		CLIENT_DIR,
		DATA_DIR,
		PROFILE_DIR,
		HOOKS_DIR,
		CLIENT_NATIVES_DIR,
		PLUGINS_DIR };
}