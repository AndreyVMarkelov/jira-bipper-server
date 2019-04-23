package ru.andreymarkelov.atlas.plugins.jirabipperserver.rest;

import com.atlassian.jira.issue.fields.CustomField;
import com.atlassian.jira.issue.fields.FieldException;
import com.atlassian.jira.issue.fields.FieldManager;
import com.atlassian.jira.issue.fields.NavigableField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.andreymarkelov.atlas.plugins.jirabipperserver.rest.model.FieldModel;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static ru.andreymarkelov.atlas.plugins.jirabipperserver.manager.NumberExtractor.ASSIGNEE_FIELD_CLASS;
import static ru.andreymarkelov.atlas.plugins.jirabipperserver.manager.NumberExtractor.CREATOR_FIELD_CLASS;
import static ru.andreymarkelov.atlas.plugins.jirabipperserver.manager.NumberExtractor.REPORTER_FIELD_CLASS;
import static ru.andreymarkelov.atlas.plugins.jirabipperserver.manager.NumberExtractor.VOTES_FIELD_CLASS;
import static ru.andreymarkelov.atlas.plugins.jirabipperserver.manager.NumberExtractor.WATCHES_FIELD_CLASS;

@Path("/postfunction")
public class PostFunctionResource {
    private static final Logger log = LoggerFactory.getLogger(PostFunctionResource.class);

    private static final List<String> userFieldNames = asList(ASSIGNEE_FIELD_CLASS, REPORTER_FIELD_CLASS, CREATOR_FIELD_CLASS, WATCHES_FIELD_CLASS, VOTES_FIELD_CLASS);

    private static final List<String> userCustomFieldTypes = asList(
            "com.atlassian.jira.plugin.system.customfieldtypes:userpicker",
            "com.atlassian.jira.plugin.system.customfieldtypes:multiuserpicker",
            "com.atlassian.servicedesk:sd-request-participants"
    );

    private static final List<String> groupCustomFieldTypes = asList(
            "com.atlassian.jira.plugin.system.customfieldtypes:grouppicker",
            "com.atlassian.jira.plugin.system.customfieldtypes:multigrouppicker"
    );

    private static final List<String> textCustomFieldTypes = asList(
            "com.atlassian.jira.plugin.system.customfieldtypes:textfield"
    );

    private final FieldManager fieldManager;

    public PostFunctionResource(FieldManager fieldManager) {
        this.fieldManager = fieldManager;
    }

    @GET
    @Produces({"application/json", "application/xml"})
    @Path("/issueusers")
    public Response getUserFields() {
        return Response.ok(getFields(0)).build();
    }

    @GET
    @Produces({"application/json", "application/xml"})
    @Path("/issuegroups")
    public Response getGroupFields() {
        return Response.ok(getFields(1)).build();
    }

    @GET
    @Produces({"application/json", "application/xml"})
    @Path("/issuetextfields")
    public Response getTextFields() {
        return Response.ok(getFields(2)).build();
    }

    private Collection<FieldModel> getFields(int type) {
        try {
            Collection<FieldModel> userFields = new ArrayList<>();
            Collection<FieldModel> groupFields = new ArrayList<>();
            Collection<FieldModel> textFields = new ArrayList<>();

            Iterator<NavigableField> navigableFieldIterator = fieldManager.getAllAvailableNavigableFields().iterator();
            while (navigableFieldIterator.hasNext()) {
                NavigableField field = navigableFieldIterator.next();
                String className = field.getClass().getCanonicalName();
                if (userFieldNames.contains(className)) {
                    userFields.add(new FieldModel(field.getId(), field.getName()));
                } else if ("com.atlassian.jira.issue.fields.CustomFieldImpl".equals(className) || "com.atlassian.jira.issue.fields.ImmutableCustomField".equals(className)) {
                    CustomField cf = (CustomField) field;

                    if (userCustomFieldTypes.contains(cf.getCustomFieldType().getKey())) {
                        userFields.add(new FieldModel(cf.getId(), cf.getName()));
                    }

                    if (groupCustomFieldTypes.contains(cf.getCustomFieldType().getKey())) {
                        groupFields.add(new FieldModel(cf.getId(), cf.getName()));
                    }

                    if (textCustomFieldTypes.contains(cf.getCustomFieldType().getKey())) {
                        textFields.add(new FieldModel(cf.getId(), cf.getName()));
                    }
                }
            }

            if (type == 0) {
                return userFields;
            } else if (type == 1) {
                return groupFields;
            } else {
                return textFields;
            }
        } catch (FieldException ex) {
            log.error("Error reading custom fields", ex);
            return emptyList();
        }
    }
}
