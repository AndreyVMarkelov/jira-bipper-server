package ru.andreymarkelov.atlas.plugins.jirabipperserver.manager.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.atlassian.crowd.embedded.api.Group;
import com.atlassian.jira.issue.CustomFieldManager;
import com.atlassian.jira.issue.Issue;
import com.atlassian.jira.issue.fields.CustomField;
import com.atlassian.jira.security.groups.GroupManager;
import com.atlassian.jira.user.ApplicationUser;
import com.atlassian.jira.user.util.UserManager;
import org.apache.commons.lang3.StringUtils;
import ru.andreymarkelov.atlas.plugins.jirabipperserver.manager.ContactManager;
import ru.andreymarkelov.atlas.plugins.jirabipperserver.manager.NumberExtractor;

import static java.util.Collections.emptySet;

public class NumberExtractorImpl implements NumberExtractor {
    private final ContactManager contactManager;
    private final UserManager userManager;
    private final GroupManager groupManager;
    private final CustomFieldManager customFieldManager;

    public NumberExtractorImpl(
            ContactManager contactManager,
            UserManager userManager,
            GroupManager groupManager,
            CustomFieldManager customFieldManager) {
        this.contactManager = contactManager;
        this.userManager = userManager;
        this.groupManager = groupManager;
        this.customFieldManager = customFieldManager;
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
    public Set<String> getUserFieldPhones(Issue issue, String field) {
        return null;
    }

    @Override
    public Set<String> getGroupFieldPhones(Issue issue, String field) {
        CustomField customField = customFieldManager.getCustomFieldObject(field);
        if (customField == null) {
            return emptySet();
        }

        Object customFieldValue = issue.getCustomFieldValue(customField);
        if (!(customFieldValue instanceof List)) {
            return emptySet();
        }

        Set<String> phones = new HashSet<>();
        for (Object group: (List) customFieldValue) {
            if (group instanceof Group) {
                for (ApplicationUser applicationUser : groupManager.getUsersInGroup((Group) group)) {
                    String phone = contactManager.getPhoneByKey(applicationUser.getKey());
                    if (StringUtils.isNotBlank(phone)) {
                        phones.add(phone);
                    }
                }
            }
        }
        return phones;
    }
}
