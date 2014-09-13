package eu.bibl.launcher.version.json;

import java.util.HashMap;

import eu.bibl.launcher.OperatingSystem;

public class LibraryNatives extends HashMap<String, String> {
	
	private static final long serialVersionUID = -3157314978578156719L;
	
	public String getValue(OperatingSystem os) {
		String val = get(os.getName());
		if (val != null)
			return val;
		
		for (String alias : os.getAliases()) {
			val = get(alias);
			if (val != null)
				return val;
		}
		return null;
	}
}