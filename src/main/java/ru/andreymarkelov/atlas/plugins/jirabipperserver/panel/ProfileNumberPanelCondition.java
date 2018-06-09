package ru.andreymarkelov.atlas.plugins.jirabipperserver.panel;

import java.util.Map;

import com.atlassian.jira.user.ApplicationUser;
import com.atlassian.plugin.PluginParseException;
import com.atlassian.plugin.web.Condition;

public class ProfileNumberPanelCondition implements Condition {
    @Override
    public void init(Map<String, String> params) throws PluginParseException {
    }

    @Override
    public boolean shouldDisplay(Map<String, Object> context) {
        ApplicationUser profileUser = (ApplicationUser) context.get("profileUser");
        ApplicationUser currentUser = (ApplicationUser) context.get("currentUser");
        return profileUser != null && profileUser.equals(currentUser);
    }
}
