
package eu.bibl.launcher.version.json;

import java.util.HashMap;
import java.util.Map;

public class Rule {
    private String action;
    private Os os;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public Os getOs() {
        return os;
    }

    public void setOs(Os os) {
        this.os = os;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
