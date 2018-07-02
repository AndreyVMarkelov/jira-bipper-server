package ru.andreymarkelov.atlas.plugins.jirabipperserver.manager;

import java.util.List;

import com.atlassian.jira.issue.Issue;

public interface NumberExtractor {
    String USER_FIELD = "1";
    String GROUP_FIELD = "2";
    String USER = "3";
    String PHONE = "4";

    String getUserPhone(String user);
    List<String> getUserFieldPhones(Issue issue, String field);
    List<String> getGroupFieldPhones(Issue issue, String field);
}
