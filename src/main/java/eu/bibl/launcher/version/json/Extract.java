package eu.bibl.launcher.version.json;

import java.util.ArrayList;
import java.util.List;

public class Extract {
	private List<String> exclude = new ArrayList<String>();
	
	public List<String> getExclude() {
		return exclude;
	}
	
	public void setExclude(List<String> exclude) {
		this.exclude = exclude;
	}
}
