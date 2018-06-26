package ru.andreymarkelov.atlas.plugins.jirabipperserver.workflow.function;

import java.util.HashMap;
import java.util.Map;

import com.atlassian.activeobjects.external.ActiveObjects;
import com.atlassian.beehive.ClusterLockService;
import com.atlassian.crowd.embedded.api.User;
import com.atlassian.jira.config.properties.ApplicationProperties;
import com.atlassian.jira.issue.CustomFieldManager;
import com.atlassian.jira.issue.Issue;
import com.atlassian.jira.issue.vote.IssueVoterAccessor;
import com.atlassian.jira.issue.watchers.IssueWatcherAccessor;
import com.atlassian.jira.workflow.function.issue.AbstractJiraFunctionProvider;
import com.opensymphony.module.propertyset.PropertySet;
import com.opensymphony.workflow.WorkflowException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SendSmsFunction extends AbstractJiraFunctionProvider {
    private static final Logger log = LoggerFactory.getLogger(SendSmsFunction.class);

    private ApplicationProperties applicationProperties;
    private IssueWatcherAccessor watcherAccessor;
    private IssueVoterAccessor voterAccessor;
    private CustomFieldManager customFieldManager;
    private ClusterLockService clusterLockService;
    private ActiveObjects ao;

    public SendSmsFunction(
            ApplicationProperties applicationProperties,
            IssueWatcherAccessor watcherAccessor,
            IssueVoterAccessor voterAccessor,
            CustomFieldManager customFieldManager,
            ClusterLockService clusterLockService,
            ActiveObjects ao
    ) {
        this.applicationProperties = applicationProperties;
        this.watcherAccessor = watcherAccessor;
        this.voterAccessor = voterAccessor;
        this.customFieldManager = customFieldManager;
        this.clusterLockService = clusterLockService;
        this.ao = ao;
    }

    public void execute(Map transientVars, Map args, PropertySet ps) throws WorkflowException {
        Issue issue = getIssue(transientVars);
        User user = getCallerUserFromArgs(transientVars, args).getDirectoryUser();
        HashMap<String, String> parameters = new HashMap();
    }
}
