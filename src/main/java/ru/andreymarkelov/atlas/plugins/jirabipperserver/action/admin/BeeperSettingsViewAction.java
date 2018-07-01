package ru.andreymarkelov.atlas.plugins.jirabipperserver.action.admin;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import com.atlassian.jira.web.action.JiraWebActionSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.andreymarkelov.atlas.plugins.jirabipperserver.manager.AuthManager;

import static java.util.Optional.ofNullable;

import static com.atlassian.jira.permission.GlobalPermissionKey.ADMINISTER;

public class BeeperSettingsViewAction extends JiraWebActionSupport {
    private static final Logger log = LoggerFactory.getLogger(BeeperSettingsViewAction.class);

    private static final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private final AuthManager authManager;

    private String senderName;
    private String apiKey;
    private String generationTime;

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
        generationTime = ofNullable(authManager.getGenerationTime()).map(dateFormat::format).orElse(null);
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

    public String getGenerationTime() {
        return generationTime;
    }
}
