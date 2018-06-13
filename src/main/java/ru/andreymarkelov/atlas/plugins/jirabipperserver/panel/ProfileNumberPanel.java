package ru.andreymarkelov.atlas.plugins.jirabipperserver.panel;

import java.util.HashMap;
import java.util.Map;

import com.atlassian.jira.user.ApplicationUser;
import com.atlassian.plugin.PluginParseException;
import com.atlassian.plugin.web.ContextProvider;
import ru.andreymarkelov.atlas.plugins.jirabipperserver.manager.ContactManager;

public class ProfileNumberPanel implements ContextProvider {
    private final ContactManager contactManager;

    public ProfileNumberPanel(ContactManager contactManager) {
        this.contactManager = contactManager;
    }

    @Override
    public void init(Map<String, String> params) throws PluginParseException {
    }

    @Override
    public Map<String, Object> getContextMap(Map<String, Object> context) {
        ApplicationUser currentUser = (ApplicationUser) context.get("currentUser");

        Map<String, Object> result = new HashMap<>(context);
        result.put("phone", contactManager.getPhoneByName(currentUser.getName()));
        return result;
    }
}
