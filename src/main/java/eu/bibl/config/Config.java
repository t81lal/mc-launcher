package eu.bibl.config;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import eu.bibl.banalysis.AnalysisCore;
import eu.bibl.launcher.FileConstants;

public class Config {
	
	public static final Config GLOBAL_CONFIG;
	protected static List<Config> configCache;
	
	static {
		configCache = new ArrayList<Config>();
		GLOBAL_CONFIG = new Config(new File(FileConstants.DATA_DIR, "global.config"));
		Runtime.getRuntime().addShutdownHook(new Thread(GLOBAL_CONFIG.new ConfigSaverRunnable()));
	}
	
	protected File file;
	protected Map<String, Object> values;
	
	public Config(File file, Map<String, Object> values) {
		this.file = file;
		this.values = values;
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		try {
			load();
			configCache.add(this);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public Config(File file) {
		this(file, new HashMap<String, Object>());
	}
	
	public void clear() {
		values.clear();
	}
	
	@SuppressWarnings("unchecked")
	public void load() throws IOException {
		if (!file.exists())
			file.createNewFile();
		BufferedReader br = new BufferedReader(new FileReader(file));
		String total = "";
		String line;
		while ((line = br.readLine()) != null) {
			total += line;
		}
		br.close();
		values = AnalysisCore.GSON_INSTANCE.fromJson(total, HashMap.class);
		if (values == null) {
			values = new HashMap<String, Object>();
		}
	}
	
	public void save() throws IOException {
		BufferedWriter bw = new BufferedWriter(new FileWriter(file));
		String gsonString = AnalysisCore.GSON_INSTANCE.toJson(values);
		bw.write(gsonString);
		bw.close();
	}
	
	public <T> T getValue(String key, T defaultValue) {
		try {
			@SuppressWarnings("unchecked")
			T val = (T) values.get(key);
			if (val == null) {
				values.put(key, defaultValue);
			}
			return val;
		} catch (Exception e) {
			return defaultValue;
		}
	}
	
	public <T> T getValue(String key) {
		try {
			@SuppressWarnings("unchecked")
			T val = (T) values.get(key);
			return val;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public void setValue(String key, Object val) {
		values.put(key, val);
	}
	
	class ConfigSaverRunnable implements Runnable {
		@Override
		public void run() {
			System.out.println("Saving configs.");
			for (Config config : configCache) {
				try {
					config.save();
				} catch (IOException e) {
					System.out.println("Error saving config " + config.file.getAbsolutePath());
					e.printStackTrace();
				}
			}
		}
	}
}