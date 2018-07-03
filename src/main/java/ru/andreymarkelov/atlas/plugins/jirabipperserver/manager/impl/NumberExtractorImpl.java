package ru.andreymarkelov.atlas.plugins.jirabipperserver.manager.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.atlassian.crowd.embedded.api.Group;
import com.atlassian.jira.issue.CustomFieldManager;
import com.atlassian.jira.issue.Issue;
import com.atlassian.jira.issue.fields.CustomField;
import com.atlassian.jira.issue.fields.Field;
import com.atlassian.jira.issue.fields.FieldManager;
import com.atlassian.jira.issue.vote.VoteManager;
import com.atlassian.jira.issue.watchers.WatcherManager;
import com.atlassian.jira.security.groups.GroupManager;
import com.atlassian.jira.user.ApplicationUser;
import com.atlassian.jira.user.util.UserManager;
import ru.andreymarkelov.atlas.plugins.jirabipperserver.manager.ContactManager;
import ru.andreymarkelov.atlas.plugins.jirabipperserver.manager.NumberExtractor;

import static java.util.Collections.emptySet;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class NumberExtractorImpl implements NumberExtractor {
    private final ContactManager contactManager;
    private final UserManager userManager;
    private final GroupManager groupManager;
    private final CustomFieldManager customFieldManager;
    private final FieldManager fieldManager;
    private final WatcherManager watcherManager;
    private final VoteManager voteManager;

    public NumberExtractorImpl(
            ContactManager contactManager,
            UserManager userManager,
            GroupManager groupManager,
            CustomFieldManager customFieldManager,
            FieldManager fieldManager,
            WatcherManager watcherManager,
            VoteManager voteManager) {
        this.contactManager = contactManager;
        this.userManager = userManager;
        this.groupManager = groupManager;
        this.customFieldManager = customFieldManager;
        this.fieldManager = fieldManager;
        this.watcherManager = watcherManager;
        this.voteManager = voteManager;
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
        Set<String> phones = new HashSet<>();
        CustomField customField = customFieldManager.getCustomFieldObject(field);
        if (customField != null) {
            Object customFieldValue = issue.getCustomFieldValue(customField);
            if (customFieldValue instanceof List) {
                for (Object user: (List) customFieldValue) {
                    if (user instanceof ApplicationUser) {
                        String phone = contactManager.getPhoneByKey(((ApplicationUser) user).getKey());
                        if (isNotBlank(phone)) {
                            phones.add(phone);
                        }
                    }
                }
            } else if (customFieldValue instanceof ApplicationUser) {
                String phone = contactManager.getPhoneByKey(((ApplicationUser) customFieldValue).getKey());
                if (isNotBlank(phone)) {
                    phones.add(phone);
                }
            }
        } else {
            Field fieldObject = fieldManager.getField(field);
            if (fieldObject != null) {
                if (CREATOR_FIELD_CLASS.equals(fieldObject.getClass().getName())) {
                    String phone = contactManager.getPhoneByKey(issue.getCreator().getKey());
                    if (isNotBlank(phone)) {
                        phones.add(phone);
                    }
                } else if (REPORTER_FIELD_CLASS.equals(fieldObject.getClass().getName())) {
                    String phone = contactManager.getPhoneByKey(issue.getReporter().getKey());
                    if (isNotBlank(phone)) {
                        phones.add(phone);
                    }
                } else if (ASSIGNEE_FIELD_CLASS.equals(fieldObject.getClass().getName())) {
                    ApplicationUser assignee = issue.getAssignee();
                    if (assignee != null) {
                        String phone = contactManager.getPhoneByKey(assignee.getKey());
                        if (isNotBlank(phone)) {
                            phones.add(phone);
                        }
                    }
                } else if (WATCHES_FIELD_CLASS.equals(fieldObject.getClass().getName())) {
                    for (String watcherUserKey : watcherManager.getWatcherUserKeys(issue)) {
                        String phone = contactManager.getPhoneByKey(watcherUserKey);
                        if (isNotBlank(phone)) {
                            phones.add(phone);
                        }
                    }
                } else if (VOTES_FIELD_CLASS.equals(fieldObject.getClass().getName())) {
                    for (String voterUserKey : voteManager.getVoterUserkeys(issue)) {
                        String phone = contactManager.getPhoneByKey(voterUserKey);
                        if (isNotBlank(phone)) {
                            phones.add(phone);
                        }
                    }
                }
            }
        }

        return phones;
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
                    if (isNotBlank(phone)) {
                        phones.add(phone);
                    }
                }
            }
        }
        return phones;
    }
}
