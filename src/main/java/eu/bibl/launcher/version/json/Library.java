package eu.bibl.launcher.version.json;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Library {
	
	private String name;
	private List<Rule> rules = new ArrayList<Rule>();
	private Map<String, String> natives;
	private Extract extract;
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public List<Rule> getRules() {
		return rules;
	}
	
	public void setRules(List<Rule> rules) {
		this.rules = rules;
	}
	
	public Map<String, String> getNatives() {
		return natives;
	}
	
	public Extract getExtract() {
		return extract;
	}
	
	public void setExtract(Extract extract) {
		this.extract = extract;
	}
}
