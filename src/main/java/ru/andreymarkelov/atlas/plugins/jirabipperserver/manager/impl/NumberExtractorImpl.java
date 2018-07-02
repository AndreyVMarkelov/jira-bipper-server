package ru.andreymarkelov.atlas.plugins.jirabipperserver.manager.impl;

import java.util.List;

import com.atlassian.jira.issue.Issue;
import com.atlassian.jira.user.ApplicationUser;
import com.atlassian.jira.user.util.UserManager;
import ru.andreymarkelov.atlas.plugins.jirabipperserver.manager.ContactManager;
import ru.andreymarkelov.atlas.plugins.jirabipperserver.manager.NumberExtractor;

public class NumberExtractorImpl implements NumberExtractor {
    private final ContactManager contactManager;
    private final UserManager userManager;

    public NumberExtractorImpl(
            ContactManager contactManager,
            UserManager userManager) {
        this.contactManager = contactManager;
        this.userManager = userManager;
    }

    @Override
    public String getUserPhone(String user) {
        ApplicationUser applicationUser = userManager.getUserByName(user);
        if (applicationUser == null) {
            applicationUser = userManager.getUserByKey(user);
        }

        if (applicationUser == null) {
            throw new RuntimeException("No user found by name: " + user);
        }

        return contactManager.getPhoneByKey(applicationUser.getKey());
    }

    @Override
    public List<String> getUserFieldPhones(Issue issue, String field) {
        return null;
    }

    @Override
    public List<String> getGroupFieldPhones(Issue issue, String field) {
        return null;
    }
}
