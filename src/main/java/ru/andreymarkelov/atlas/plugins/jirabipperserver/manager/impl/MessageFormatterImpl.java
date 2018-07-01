package ru.andreymarkelov.atlas.plugins.jirabipperserver.manager.impl;

import com.atlassian.jira.config.properties.ApplicationProperties;
import com.atlassian.jira.issue.Issue;
import ru.andreymarkelov.atlas.plugins.jirabipperserver.manager.MessageFormatter;

import static org.apache.commons.lang3.StringUtils.replace;

public class MessageFormatterImpl implements MessageFormatter {
    private final ApplicationProperties applicationProperties;

    public MessageFormatterImpl(ApplicationProperties applicationProperties) {
        this.applicationProperties = applicationProperties;
    }

    @Override
    public String formatMessage(String messageTemplate, Issue issue) {
        return transformStandardFields(messageTemplate, issue);
    }

    private String transformStandardFields(String message, Issue issue) {
        message = replace(message, "{{issue_key}}", issue.getKey());
        message = replace(message, "{{issue_link}}", applicationProperties.getString("jira.baseurl") + "/browse/" + issue.getKey());
        message = replace(message, "{{issue_type}}", issue.getIssueType().getName());
        message = replace(message, "{{issue_summary}}", issue.getSummary());
        message = replace(message, "{{issue_description}}", issue.getDescription());
        message = replace(message, "{{issue_status}}", issue.getStatus().getName());
        message = replace(message, "{{issue_priority}}", issue.getPriority().getName());
        message = replace(message, "{{issue_project_key}}", issue.getProjectObject().getKey());
        message = replace(message, "{{issue_project_name}}", issue.getProjectObject().getName());
        message = replace(message, "{{issue_resolution}}", issue.getResolution() != null ? issue.getResolution().getName() : "Unresolved");
        message = replace(message, "{{issue_assignee_id}}", issue.getAssignee() != null ? issue.getAssignee().getUsername() : "nobody");
        message = replace(message, "{{issue_reporter_id}}", issue.getReporter() != null ? issue.getReporter().getUsername() : "nobody");
        message = replace(message, "{{issue_creator_id}}", issue.getCreator() != null ? issue.getCreator().getUsername() : "nobody");
        message = replace(message, "{{issue_assignee_name}}", issue.getAssignee() != null ? issue.getCreator().getDisplayName() : "nobody");
        message = replace(message, "{{issue_reporter_name}}", issue.getReporter() != null ? issue.getReporter().getDisplayName() : "nobody");
        message = replace(message, "{{issue_creator_name}}", issue.getCreator() != null ? issue.getCreator().getDisplayName() : "nobody");
        return message;
    }
}
