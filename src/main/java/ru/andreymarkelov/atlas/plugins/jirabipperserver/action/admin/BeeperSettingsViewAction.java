package ru.andreymarkelov.atlas.plugins.jirabipperserver.action.admin;

import com.atlassian.jira.web.action.JiraWebActionSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.andreymarkelov.atlas.plugins.jirabipperserver.manager.AuthManager;

import static java.util.Optional.ofNullable;

import static com.atlassian.jira.permission.GlobalPermissionKey.ADMINISTER;

public class BeeperSettingsViewAction extends JiraWebActionSupport {
    private static final Logger log = LoggerFactory.getLogger(BeeperSettingsViewAction.class);

    private final AuthManager authManager;

    private String senderName;
    private String apiKey;
    private Long generationTime;

    public BeeperSettingsViewAction(AuthManager authManager) {
        this.authManager = authManager;
    }

    @Override
    public String doDefault() {
        if (!hasAdminPermission()) {
            return PERMISSION_VIOLATION_RESULT;
        }
        senderName = authManager.getSenderName();
        apiKey = authManager.getApiKey();
        generationTime = authManager.getGenerationTime();
        return INPUT;
    }

    private boolean hasAdminPermission() {
        return ofNullable(getLoggedInUser())
                .map(x -> getGlobalPermissionManager().hasPermission(ADMINISTER, x))
                .orElse(false);
    }

    public String getSenderName() {
        return senderName;
    }

    public String getApiKey() {
        return apiKey;
    }

    public Long getGenerationTime() {
        return generationTime;
    }
}
