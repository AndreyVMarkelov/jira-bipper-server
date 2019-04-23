package ru.andreymarkelov.atlas.plugins.jirabipperserver.workflow.function;

import com.atlassian.jira.plugin.workflow.AbstractWorkflowPluginFactory;
import com.atlassian.jira.plugin.workflow.WorkflowPluginFunctionFactory;
import com.atlassian.jira.util.I18nHelper;
import com.opensymphony.workflow.loader.AbstractDescriptor;
import com.opensymphony.workflow.loader.FunctionDescriptor;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static ru.andreymarkelov.atlas.plugins.jirabipperserver.manager.NumberExtractor.GROUP_FIELD;
import static ru.andreymarkelov.atlas.plugins.jirabipperserver.manager.NumberExtractor.PHONE;
import static ru.andreymarkelov.atlas.plugins.jirabipperserver.manager.NumberExtractor.TEXT_FIELD;
import static ru.andreymarkelov.atlas.plugins.jirabipperserver.manager.NumberExtractor.USER;
import static ru.andreymarkelov.atlas.plugins.jirabipperserver.manager.NumberExtractor.USER_FIELD;

public class SendSmsFunctionFactory extends AbstractWorkflowPluginFactory implements WorkflowPluginFunctionFactory {
    private final I18nHelper i18nHelper;

    public SendSmsFunctionFactory(I18nHelper i18nHelper) {
        this.i18nHelper = i18nHelper;
    }

    protected void getVelocityParamsForInput(Map<String, Object> velocityParams) {
        velocityParams.put("messageText", "The issue {{issue_key}} requires your immediate attention! See, {{issue_link}}");
        velocityParams.put("recipientType", USER_FIELD);
    }

    protected void getVelocityParamsForEdit(Map<String, Object> velocityParams, AbstractDescriptor descriptor) {
        getVelocityParamsForInput(velocityParams);
        getVelocityParamsForView(velocityParams, descriptor);
    }

    protected void getVelocityParamsForView(Map<String, Object> velocityParams, AbstractDescriptor descriptor) {
        if (!(descriptor instanceof FunctionDescriptor)) {
            throw new IllegalArgumentException("Descriptor must be a FunctionDescriptor.");
        }

        FunctionDescriptor functionDescriptor = (FunctionDescriptor)descriptor;
        String recipientType = Objects.toString(functionDescriptor.getArgs().get("recipientType"), "No destination defined.");
        String userFieldValue = (String) functionDescriptor.getArgs().get("userFieldValue");
        String groupFieldValue = (String) functionDescriptor.getArgs().get("groupFieldValue");
        String userValue = (String) functionDescriptor.getArgs().get("userValue");
        String phoneValue = (String) functionDescriptor.getArgs().get("phoneValue");
        String textFieldValue = (String) functionDescriptor.getArgs().get("textFieldValue");

        String recipientTypeStr = null;
        String recipientTypeValue = null;
        switch (recipientType) {
            case USER_FIELD: {
                recipientTypeStr = i18nHelper.getText("ru.andreymarkelov.atlas.plugins.jira-bipper-server.postfunction.sendto.type.userfield");
                recipientTypeValue = userFieldValue;
                break;
            } case GROUP_FIELD: {
                recipientTypeStr = i18nHelper.getText("ru.andreymarkelov.atlas.plugins.jira-bipper-server.postfunction.sendto.type.groupfield");
                recipientTypeValue = groupFieldValue;
                break;
            } case USER: {
                recipientTypeStr = i18nHelper.getText("ru.andreymarkelov.atlas.plugins.jira-bipper-server.postfunction.sendto.type.user");
                recipientTypeValue = userValue;
                break;
            } case PHONE: {
                recipientTypeStr = i18nHelper.getText("ru.andreymarkelov.atlas.plugins.jira-bipper-server.postfunction.sendto.type.phone");
                recipientTypeValue = phoneValue;
                break;
            } case TEXT_FIELD: {
                recipientTypeStr = i18nHelper.getText("ru.andreymarkelov.atlas.plugins.jira-bipper-server.postfunction.sendto.type.textfield");
                recipientTypeValue = textFieldValue;
            }
        }

        velocityParams.put("messageText", Objects.toString(functionDescriptor.getArgs().get("messageText"), "No Message"));
        velocityParams.put("recipientType", recipientType);
        velocityParams.put("userFieldValue", userFieldValue);
        velocityParams.put("groupFieldValue", groupFieldValue);
        velocityParams.put("userValue", userValue);
        velocityParams.put("phoneValue", phoneValue);
        velocityParams.put("textFieldValue", textFieldValue);
        velocityParams.put("recipientTypeStr", recipientTypeStr);
        velocityParams.put("recipientTypeValue", recipientTypeValue);
    }

    public Map<String, Object> getDescriptorParams(Map<String, Object> formParams) {
        String messageText = extractSingleParam(formParams, "messageText");
        String recipientType = extractSingleParam(formParams, "recipientType");
        String userFieldValue = extractSingleParam(formParams, "userFieldValue");
        String groupFieldValue = extractSingleParam(formParams, "groupFieldValue");
        String userValue = extractSingleParam(formParams, "userValue");
        String phoneValue = extractSingleParam(formParams, "phoneValue");
        String textFieldValue = extractSingleParam(formParams, "textFieldValue");

        switch (recipientType) {
            case USER_FIELD: {
                groupFieldValue = null;
                userValue = null;
                phoneValue = null;
                textFieldValue = null;
                break;
            } case GROUP_FIELD: {
                userFieldValue = null;
                userValue = null;
                phoneValue = null;
                textFieldValue = null;
                break;
            } case USER: {
                userFieldValue = null;
                groupFieldValue = null;
                phoneValue = null;
                textFieldValue = null;
                break;
            } case PHONE: {
                userFieldValue = null;
                groupFieldValue = null;
                userValue = null;
                textFieldValue = null;
                break;
            } case TEXT_FIELD: {
                userFieldValue = null;
                groupFieldValue = null;
                userValue = null;
                phoneValue = null;
                break;
            }
        }

        Map<String, Object> params = new HashMap<>();
        params.put("messageText", messageText);
        params.put("recipientType", recipientType);
        params.put("userFieldValue", userFieldValue);
        params.put("groupFieldValue", groupFieldValue);
        params.put("userValue", userValue);
        params.put("phoneValue", phoneValue);
        params.put("textFieldValue", textFieldValue);
        return params;
    }
}
