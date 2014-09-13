package eu.bibl.launcher.version.json;

import java.util.HashMap;
import java.util.Map;

import eu.bibl.launcher.OperatingSystem;

public class LibraryRules {
	
	private String action;
	private JsonOsObject os;
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();
	
	public String getAction() {
		return action;
	}
	
	public void setAction(String action) {
		this.action = action;
	}
	
	public OperatingSystem getOperatingSystem() {
		return OperatingSystem.valueOf(os.name);
	}
	
	public Map<String, Object> getAdditionalProperties() {
		return additionalProperties;
	}
	
	public void setAdditionalProperty(String name, Object value) {
		additionalProperties.put(name, value);
	}
	
	private class JsonOsObject {
		String name;
	}
}