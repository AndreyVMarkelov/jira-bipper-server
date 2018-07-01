package ru.andreymarkelov.atlas.plugins.jirabipperserver.manager;

import com.atlassian.jira.issue.Issue;

public interface MessageFormatter {
    String formatMessage(String messageTemplate, Issue issue);
}
