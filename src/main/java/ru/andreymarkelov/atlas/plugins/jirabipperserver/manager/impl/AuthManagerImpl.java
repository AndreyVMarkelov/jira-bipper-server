package ru.andreymarkelov.atlas.plugins.jirabipperserver.manager.impl;

import java.util.Objects;

import com.atlassian.sal.api.pluginsettings.PluginSettings;
import com.atlassian.sal.api.pluginsettings.PluginSettingsFactory;
import ru.andreymarkelov.atlas.plugins.jirabipperserver.manager.AuthManager;

public class AuthManagerImpl implements AuthManager {
    private final PluginSettings pluginSettings;

    public AuthManagerImpl(PluginSettingsFactory pluginSettingsFactory) {
        this.pluginSettings = pluginSettingsFactory.createSettingsForKey("PLUGIN_BIPPER_SERVER");
    }

    @Override
    public String getSenderName() {
        return Objects.toString(getPluginSettings().get("senderName"), null);
    }

    @Override
    public void setSenderName(String senderName) {
        getPluginSettings().put("senderName", senderName);
    }

    @Override
    public String getApiKey() {
        return Objects.toString(getPluginSettings().get("apiKey"), null);
    }

    @Override
    public void setApiKey(String apiKey) {
        getPluginSettings().put("apiKey", apiKey);
    }

    @Override
    public Long getGenerationTime() {
        Object storedValue = getPluginSettings().get("generationTime");
        return storedValue != null ? Long.valueOf(storedValue.toString()) : null;
    }

    @Override
    public void setGenerationTime(Long generationTime) {
        getPluginSettings().put("generationTime", Objects.toString(generationTime, null));
    }

    private synchronized PluginSettings getPluginSettings() {
        return pluginSettings;
    }
}
