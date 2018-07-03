package ru.andreymarkelov.atlas.plugins.jirabipperserver.manager;

import java.util.Set;

import com.atlassian.jira.issue.Issue;

public interface NumberExtractor {
    String USER_FIELD = "1";
    String GROUP_FIELD = "2";
    String USER = "3";
    String PHONE = "4";

    String CREATOR_FIELD_CLASS = "com.atlassian.jira.issue.fields.CreatorSystemField";
    String REPORTER_FIELD_CLASS = "com.atlassian.jira.issue.fields.ReporterSystemField";
    String ASSIGNEE_FIELD_CLASS = "com.atlassian.jira.issue.fields.AssigneeSystemField";
    String WATCHES_FIELD_CLASS = "com.atlassian.jira.issue.fields.WatchesSystemField";
    String VOTES_FIELD_CLASS = "com.atlassian.jira.issue.fields.VotesSystemField";

    String getUserPhone(String user);
    Set<String> getUserFieldPhones(Issue issue, String field);
    Set<String> getGroupFieldPhones(Issue issue, String field);
}
