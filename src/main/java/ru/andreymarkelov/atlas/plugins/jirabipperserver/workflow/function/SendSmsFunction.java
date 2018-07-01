package ru.andreymarkelov.atlas.plugins.jirabipperserver.workflow.function;

import java.util.HashMap;
import java.util.Map;

import com.atlassian.jira.issue.Issue;
import com.atlassian.jira.user.ApplicationUser;
import com.atlassian.jira.workflow.function.issue.AbstractJiraFunctionProvider;
import com.opensymphony.module.propertyset.PropertySet;
import com.opensymphony.workflow.WorkflowException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.andreymarkelov.atlas.plugins.jirabipperserver.manager.MessageFormatter;
import ru.andreymarkelov.atlas.plugins.jirabipperserver.manager.NumberExtractor;
import ru.andreymarkelov.atlas.plugins.jirabipperserver.manager.SenderService;

public class SendSmsFunction extends AbstractJiraFunctionProvider {
    private static final Logger log = LoggerFactory.getLogger(SendSmsFunction.class);

    private final MessageFormatter messageFormatter;
    private final NumberExtractor numberExtractor;
    private final SenderService senderService;

    public SendSmsFunction(
            MessageFormatter messageFormatter,
            NumberExtractor numberExtractor,
            SenderService senderService) {
        this.messageFormatter = messageFormatter;
        this.numberExtractor = numberExtractor;
        this.senderService = senderService;
    }

    public void execute(Map transientVars, Map args, PropertySet ps) throws WorkflowException {
        Issue issue = getIssue(transientVars);
        ApplicationUser callerUser = getCallerUserFromArgs(transientVars, args);

        String message = (String) args.get("messageText");

        String recipientType = (String) args.get("recipientType");
        String value = null;
        switch (recipientType) {
            case "1":
                value = (String) args.get("userFieldValue");
                break;
            case "2":
                value = (String) args.get("groupFieldValue");
                break;
            case "3":
                value = (String) args.get("userValue");
                break;
            case "4":
                value = (String) args.get("phoneValue");
                break;
        }



        String text = messageFormatter.formatMessage(message, issue);

        HashMap<String, String> parameters = new HashMap();
    }


}
