package ru.andreymarkelov.atlas.plugins.jirabipperserver.rest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import com.atlassian.jira.issue.fields.CustomField;
import com.atlassian.jira.issue.fields.FieldException;
import com.atlassian.jira.issue.fields.FieldManager;
import com.atlassian.jira.issue.fields.NavigableField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.andreymarkelov.atlas.plugins.jirabipperserver.rest.model.FieldModel;

import static java.util.Collections.emptyList;

@Path("/postfunction")
public class PostFunctionResource {
    private static final Logger log = LoggerFactory.getLogger(PostFunctionResource.class);

    private static final List<String> userFieldNames = Arrays.asList(
            "com.atlassian.jira.issue.fields.AssigneeSystemField",
            "com.atlassian.jira.issue.fields.ReporterSystemField",
            "com.atlassian.jira.issue.fields.CreatorSystemField",
            "com.atlassian.jira.issue.fields.WatchesSystemField",
            "com.atlassian.jira.issue.fields.VotesSystemField"
    );

    private static final List<String> userCustomFieldTypes = Arrays.asList(
            "com.atlassian.jira.plugin.system.customfieldtypes:userpicker",
            "com.atlassian.jira.plugin.system.customfieldtypes:multiuserpicker",
            "com.atlassian.servicedesk:sd-request-participants"
    );

    private static final List<String> groupCustomFieldTypes = Arrays.asList(
            "com.atlassian.jira.plugin.system.customfieldtypes:grouppicker",
            "com.atlassian.jira.plugin.system.customfieldtypes:multigrouppicker"
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

    private Collection<FieldModel> getFields(int type) {
        try {
            Collection<FieldModel> userFields = new ArrayList<>();
            Collection<FieldModel> groupFields = new ArrayList<>();

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
                }
            }

            return (type == 0) ? userFields : groupFields;
        } catch (FieldException ex) {
            log.error("Error reading custom fields", ex);
            return emptyList();
        }
    }
}
