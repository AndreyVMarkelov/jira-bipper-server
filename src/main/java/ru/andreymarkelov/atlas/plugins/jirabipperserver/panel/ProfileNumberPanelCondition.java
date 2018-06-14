package ru.andreymarkelov.atlas.plugins.jirabipperserver.panel;

import java.util.Map;

import com.atlassian.jira.security.GlobalPermissionManager;
import com.atlassian.jira.user.ApplicationUser;
import com.atlassian.plugin.PluginParseException;
import com.atlassian.plugin.web.Condition;

import static com.atlassian.jira.permission.GlobalPermissionKey.ADMINISTER;

public class ProfileNumberPanelCondition implements Condition {
    private final GlobalPermissionManager globalPermissionManager;

    public ProfileNumberPanelCondition(GlobalPermissionManager globalPermissionManager) {
        this.globalPermissionManager = globalPermissionManager;
    }

    @Override
    public void init(Map<String, String> params) throws PluginParseException {
    }

    @Override
    public boolean shouldDisplay(Map<String, Object> context) {
        ApplicationUser profileUser = (ApplicationUser) context.get("profileUser");
        ApplicationUser currentUser = (ApplicationUser) context.get("currentUser");

        if  (currentUser != null && (globalPermissionManager.hasPermission(ADMINISTER, currentUser) || currentUser.equals(profileUser))) {
            return true;
        }
        return false;
    }
}
