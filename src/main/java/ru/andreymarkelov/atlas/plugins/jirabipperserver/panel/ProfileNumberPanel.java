package ru.andreymarkelov.atlas.plugins.jirabipperserver.panel;

import java.util.HashMap;
import java.util.Map;

import com.atlassian.plugin.PluginParseException;
import com.atlassian.plugin.web.ContextProvider;

public class ProfileNumberPanel implements ContextProvider {
    @Override
    public void init(Map<String, String> params) throws PluginParseException {
    }

    @Override
    public Map<String, Object> getContextMap(Map<String, Object> context) {
        Map<String, Object> result = new HashMap<>(context);
        return result;
    }
}
