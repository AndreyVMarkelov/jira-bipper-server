package ru.andreymarkelov.atlas.plugins.jirabipperserver.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import com.atlassian.jira.security.GlobalPermissionManager;
import com.atlassian.jira.security.JiraAuthenticationContext;
import com.atlassian.jira.user.ApplicationUser;
import com.atlassian.jira.user.util.UserManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.andreymarkelov.atlas.plugins.jirabipperserver.data.Contact;
import ru.andreymarkelov.atlas.plugins.jirabipperserver.manager.ContactManager;
import ru.andreymarkelov.atlas.plugins.jirabipperserver.rest.model.ContactModel;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toList;

import static com.atlassian.jira.permission.GlobalPermissionKey.ADMINISTER;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.Response.Status.FORBIDDEN;
import static javax.ws.rs.core.Response.Status.UNAUTHORIZED;
import static javax.ws.rs.core.Response.status;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.apache.commons.lang3.math.NumberUtils.isDigits;

@Path("/contacts")
@Consumes(APPLICATION_JSON)
@Produces(APPLICATION_JSON)
public class ContactsResource {
    private static final Logger log = LoggerFactory.getLogger(ContactsResource.class);

    private final JiraAuthenticationContext authenticationContext;
    private final ContactManager contactManager;
    private final UserManager userManager;
    private final GlobalPermissionManager globalPermissionManager;

    public ContactsResource(
            JiraAuthenticationContext authenticationContext,
            ContactManager contactManager,
            UserManager userManager,
            GlobalPermissionManager globalPermissionManager) {
        this.authenticationContext = authenticationContext;
        this.contactManager = contactManager;
        this.userManager = userManager;
        this.globalPermissionManager = globalPermissionManager;
    }

    @GET
    @Path("/all")
    public Response getContacts() {
        ApplicationUser currentUser = authenticationContext.getLoggedInUser();
        if (currentUser == null) {
            return status(UNAUTHORIZED)
                    .entity(authenticationContext.getI18nHelper().getText("ru.andreymarkelov.atlas.plugins.jira-bipper-server.rest.contacts.unauthorized"))
                    .build();
        }

        if (!globalPermissionManager.hasPermission(ADMINISTER, currentUser)) {
            log.warn("Invalid user:{} tries to access contacts", currentUser.getName());
            return status(FORBIDDEN)
                    .entity(authenticationContext.getI18nHelper().getText("ru.andreymarkelov.atlas.plugins.jira-bipper-server.rest.contacts.notgranted"))
                    .build();
        }

        return Response.ok(
                contactManager.getAllContacts().stream()
                        .filter(x -> x.getName() != null)
                        .map(x -> new ContactModel(x.getId(), x.getName(), x.getPhone()))
                        .sorted(comparing(ContactModel::getName))
                        .collect(toList())
        ).build();
    }

    @POST
    @Path("/self")
    public Response addContact(ContactModel contactModel) {
        ApplicationUser currentUser = authenticationContext.getLoggedInUser();
        if (currentUser == null) {
            throw new IllegalArgumentException(authenticationContext.getI18nHelper().getText("ru.andreymarkelov.atlas.plugins.jira-bipper-server.rest.contacts.unauthorized"));
        }

        if (!globalPermissionManager.hasPermission(ADMINISTER, currentUser)) {
            log.warn("Invalid user:{} tries to access contacts", currentUser.getName());
            throw new IllegalArgumentException(authenticationContext.getI18nHelper().getText("ru.andreymarkelov.atlas.plugins.jira-bipper-server.rest.contacts.notgranted"));
        }

        ApplicationUser applicationUser = userManager.getUserByName(contactModel.getName());
        if (applicationUser == null) {
            applicationUser = userManager.getUserByKey(contactModel.getName());
        }

        if (applicationUser == null) {
            throw new IllegalArgumentException(authenticationContext.getI18nHelper().getText("ru.andreymarkelov.atlas.plugins.jira-bipper-server.rest.contacts.nouser"));
        }

        if (isNotBlank(contactModel.getPhone()) && !isDigits(contactModel.getPhone())) {
            throw new IllegalArgumentException(authenticationContext.getI18nHelper().getText("ru.andreymarkelov.atlas.plugins.jira-bipper-server.rest.contacts.invalidphone"));
        }

        Contact contact = contactManager.save(new Contact(null, applicationUser.getKey(), applicationUser.getName(), contactModel.getPhone()));
        contactModel.setId(contact.getId());
        return Response.ok(contactModel).build();
    }

    @DELETE
    @Path("/self/{id}")
    public Response delete(@PathParam("id") Integer id) {
        ApplicationUser currentUser = authenticationContext.getLoggedInUser();
        if (currentUser == null) {
            return status(UNAUTHORIZED)
                    .entity(authenticationContext.getI18nHelper().getText("ru.andreymarkelov.atlas.plugins.jira-bipper-server.rest.contacts.unauthorized"))
                    .build();
        }

        if (!globalPermissionManager.hasPermission(ADMINISTER, currentUser)) {
            log.warn("Invalid user:{} tries to access contacts", currentUser.getName());
            return status(FORBIDDEN)
                    .entity(authenticationContext.getI18nHelper().getText("ru.andreymarkelov.atlas.plugins.jira-bipper-server.rest.contacts.notgranted"))
                    .build();
        }

        contactManager.deleteById(id);
        return Response.ok().build();
    }

    @GET
    @Path("/self/{id}")
    public Response getContact(@PathParam ("id") Integer id) {
        ApplicationUser currentUser = authenticationContext.getLoggedInUser();
        if (currentUser == null) {
            return status(UNAUTHORIZED)
                    .entity(authenticationContext.getI18nHelper().getText("ru.andreymarkelov.atlas.plugins.jira-bipper-server.rest.contacts.unauthorized"))
                    .build();
        }

        if (!globalPermissionManager.hasPermission(ADMINISTER, currentUser)) {
            log.warn("Invalid user:{} tries to access contacts", currentUser.getName());
            return status(FORBIDDEN)
                    .entity(authenticationContext.getI18nHelper().getText("ru.andreymarkelov.atlas.plugins.jira-bipper-server.rest.contacts.notgranted"))
                    .build();
        }

        Contact contact = contactManager.getById(id);
        return Response.ok(new ContactModel(contact.getId(), contact.getName(), contact.getPhone())).build();
    }

    @PUT
    @Path("/self/{id}")
    public Response updateContact(@PathParam ("id") Integer id, ContactModel contactModel) {
        ApplicationUser currentUser = authenticationContext.getLoggedInUser();
        if (currentUser == null) {
            return status(UNAUTHORIZED)
                    .entity(authenticationContext.getI18nHelper().getText("ru.andreymarkelov.atlas.plugins.jira-bipper-server.rest.contacts.unauthorized"))
                    .build();
        }

        if (!globalPermissionManager.hasPermission(ADMINISTER, currentUser)) {
            log.warn("Invalid user:{} tries to access contacts", currentUser.getName());
            return status(FORBIDDEN)
                    .entity(authenticationContext.getI18nHelper().getText("ru.andreymarkelov.atlas.plugins.jira-bipper-server.rest.contacts.notgranted"))
                    .build();
        }

        ApplicationUser applicationUser = userManager.getUserByName(contactModel.getName());
        if (applicationUser == null) {
            applicationUser = userManager.getUserByKey(contactModel.getName());
        }

        if (applicationUser == null) {
            throw new IllegalArgumentException(authenticationContext.getI18nHelper().getText("ru.andreymarkelov.atlas.plugins.jira-bipper-server.rest.contacts.nouser"));
        }

        if (isNotBlank(contactModel.getPhone()) && !isDigits(contactModel.getPhone())) {
            throw new IllegalArgumentException(authenticationContext.getI18nHelper().getText("ru.andreymarkelov.atlas.plugins.jira-bipper-server.rest.contacts.invalidphone"));
        }

        Contact contact = contactManager.update(new Contact(id, applicationUser.getKey(), applicationUser.getName(), contactModel.getPhone()));
        contactModel.setName(contact.getName());
        contactModel.setPhone(contact.getPhone());
        contactModel.setId(contact.getId());
        return Response.ok(contactModel).build();
    }
}
