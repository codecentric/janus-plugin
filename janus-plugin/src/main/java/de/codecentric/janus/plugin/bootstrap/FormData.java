package de.codecentric.janus.plugin.bootstrap;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.kohsuke.stapler.StaplerRequest;

import java.util.HashMap;
import java.util.Map;

/**
* @author Ben Ripkens <bripkens.dev@gmail.com>
*/
class FormData {
    private String name, description, pckg, scaffoldName, vcsConfigName,
            ciConfigName;
    private Map<String, String> contextParameters;

    FormData() {
        contextParameters = new HashMap<String, String>();
    }

    static FormData parse(JSONObject form) {
        FormData result = new FormData();

        result.name = form.getString("name");
        result.description = form.getString("description");
        result.pckg = form.getString("pckg");

        parseSelectValues(form.getJSONArray(""), result);
        parseContextParameters(form.getJSONObject("scaffold"), result);

        return result;
    }

    private static void parseSelectValues(JSONArray formData,
                                          FormData result) {
        for(int i = 0; i < formData.size(); i++) {
            String data = formData.getString(i);

            if (data != null) {
                if (data.startsWith("vcs-")) {
                    result.vcsConfigName = data.substring(4);
                } else if (data.startsWith("scaf-")) {
                    result.scaffoldName = data.substring(5);
                } else if (data.startsWith("ci-")) {
                    result.ciConfigName = data.substring(3);
                }
            }
        }
    }

    private static void parseContextParameters(JSONObject formData,
                                               FormData result) {
        for(Object key : formData.keySet()) {
            String keyString = (String) key;
            keyString = keyString.trim();

            if (!keyString.isEmpty()) {
                String value = formData.getString(keyString);
                value = value.trim();
                if (!value.isEmpty()) {
                    result.contextParameters.put(keyString, value);
                }
            }
        }
    }

    void setFormDataAsAttributesOn(StaplerRequest req) {
        req.setAttribute("name", name);
        req.setAttribute("description", description);
        req.setAttribute("pckg", pckg);
        req.setAttribute("selectedVCS", vcsConfigName);
        req.setAttribute("selectedCI", ciConfigName);
        req.setAttribute("selectedScaffold", scaffoldName);

        for(Map.Entry<String, String> entry : contextParameters.entrySet()) {
            req.setAttribute("param-" + entry.getKey(), entry.getValue());
        }
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getPckg() {
        return pckg;
    }

    public String getScaffoldName() {
        return scaffoldName;
    }

    public String getVcsConfigName() {
        return vcsConfigName;
    }

    public String getCiConfigName() {
        return ciConfigName;
    }

    public Map<String, String> getContextParameters() {
        return contextParameters;
    }
}
