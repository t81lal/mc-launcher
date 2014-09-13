package eu.bibl.launcher.version.json;

import java.util.List;

public final class ExtractionInfo {
	
	private List<String> exclude;
	
	public boolean shouldExtract(String name) {
		return !exclude.contains(name);
	}
	
	public List<String> getExclusions() {
		return exclude;
	}
}
