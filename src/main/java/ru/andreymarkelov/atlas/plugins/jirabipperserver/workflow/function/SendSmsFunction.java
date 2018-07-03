package ru.andreymarkelov.atlas.plugins.jirabipperserver.workflow.function;

import java.util.ArrayList;
import java.util.List;
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

import static ru.andreymarkelov.atlas.plugins.jirabipperserver.manager.NumberExtractor.GROUP_FIELD;
import static ru.andreymarkelov.atlas.plugins.jirabipperserver.manager.NumberExtractor.PHONE;
import static ru.andreymarkelov.atlas.plugins.jirabipperserver.manager.NumberExtractor.USER;
import static ru.andreymarkelov.atlas.plugins.jirabipperserver.manager.NumberExtractor.USER_FIELD;

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

        List<String> phones = new ArrayList<>();
        switch ((String) args.get("recipientType")) {
            case USER_FIELD:
                phones.addAll(numberExtractor.getUserFieldPhones(issue, (String) args.get("userFieldValue")));
                break;
            case GROUP_FIELD:
                phones.addAll(numberExtractor.getGroupFieldPhones(issue, (String) args.get("groupFieldValue")));
                break;
            case USER:
                phones.add(numberExtractor.getUserPhone((String) args.get("userValue")));
                break;
            case PHONE:
                phones.add((String) args.get("phoneValue"));
                break;
        }

        if (phones.isEmpty()) {
            return;
        }

        try {
            senderService.sendMessage(messageFormatter.formatMessage(message, issue), phones);
        } catch (Exception ex) {
            log.error("Cannot send Infobip message", ex);
        }
    }
}
