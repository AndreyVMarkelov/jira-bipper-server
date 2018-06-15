package ru.andreymarkelov.atlas.plugins.jirabipperserver.rest;

import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.atlassian.jira.security.GlobalPermissionManager;
import com.atlassian.jira.security.JiraAuthenticationContext;
import com.atlassian.jira.user.ApplicationUser;
import com.atlassian.jira.user.util.UserManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.andreymarkelov.atlas.plugins.jirabipperserver.manager.ContactManager;

import static com.atlassian.jira.permission.GlobalPermissionKey.ADMINISTER;
import static javax.ws.rs.core.Response.Status.BAD_REQUEST;
import static javax.ws.rs.core.Response.Status.FORBIDDEN;
import static javax.ws.rs.core.Response.Status.NOT_FOUND;
import static javax.ws.rs.core.Response.Status.UNAUTHORIZED;
import static javax.ws.rs.core.Response.status;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.apache.commons.lang3.math.NumberUtils.isDigits;

@Path("/phonenumbers")
public class NumberResource {
    private static final Logger log = LoggerFactory.getLogger(NumberResource.class);

    private final JiraAuthenticationContext authenticationContext;
    private final ContactManager contactManager;
    private final UserManager userManager;
    private final GlobalPermissionManager globalPermissionManager;

    public NumberResource(
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
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getUserByKeyOrName(
            @QueryParam("key") String key,
            @QueryParam("name") String name) {
        ApplicationUser currentUser = authenticationContext.getLoggedInUser();
        if (currentUser == null) {
            return status(UNAUTHORIZED)
                    .entity(authenticationContext.getI18nHelper().getText("ru.andreymarkelov.atlas.plugins.jira-bipper-server.rest.unauthorized"))
                    .build();
        }

        if (isNotBlank(key)) {
            ApplicationUser user = userManager.getUserByKey(key);
            if (user == null) {
                return status(NOT_FOUND)
                        .entity(authenticationContext.getI18nHelper().getText("ru.andreymarkelov.atlas.plugins.jira-bipper-server.rest.nouserbykey"))
                        .build();
            }

            if  (!globalPermissionManager.hasPermission(ADMINISTER, currentUser) && !currentUser.equals(user)) {
                log.warn("Invalid user:{} tries to access phone number", currentUser.getName());
                return status(FORBIDDEN)
                        .entity(authenticationContext.getI18nHelper().getText("ru.andreymarkelov.atlas.plugins.jira-bipper-server.rest.notgranted"))
                        .build();
            }

            return Response.ok(new PhoneNumberReqRespModel(user.getKey(), user.getName(), contactManager.getPhoneByKey(key))).build();
        } else if (isNotBlank(name)) {
            ApplicationUser user = userManager.getUserByName(name);
            if (user == null) {
                return status(NOT_FOUND)
                        .entity(authenticationContext.getI18nHelper().getText("ru.andreymarkelov.atlas.plugins.jira-bipper-server.rest.nouserbyname"))
                        .build();
            }

            if  (!globalPermissionManager.hasPermission(ADMINISTER, currentUser) && !currentUser.equals(user)) {
                log.warn("Invalid user:{} tries to access phone number", currentUser.getName());
                return status(FORBIDDEN)
                        .entity(authenticationContext.getI18nHelper().getText("ru.andreymarkelov.atlas.plugins.jira-bipper-server.rest.notgranted"))
                        .build();
            }

            return Response.ok(new PhoneNumberReqRespModel(user.getKey(), user.getName(), contactManager.getPhoneByName(name))).build();
        } else {
            return status(BAD_REQUEST)
                    .entity(authenticationContext.getI18nHelper().getText("ru.andreymarkelov.atlas.plugins.jira-bipper-server.rest.nouserkeyorname"))
                    .build();
        }
    }

    @PUT
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response storePhoneForUser(PhoneNumberReqRespModel model) {
        ApplicationUser currentUser = authenticationContext.getLoggedInUser();
        if (currentUser == null) {
            return status(UNAUTHORIZED)
                    .entity(authenticationContext.getI18nHelper().getText("ru.andreymarkelov.atlas.plugins.jira-bipper-server.rest.unauthorized"))
                    .build();
        }

        if (isNotBlank(model.getPhone()) && !isDigits(model.getPhone())) {
            return status(BAD_REQUEST)
                    .entity(authenticationContext.getI18nHelper().getText("ru.andreymarkelov.atlas.plugins.jira-bipper-server.rest.invalidphone"))
                    .build();
        }

        if (isNotBlank(model.getKey())) {
            ApplicationUser user = userManager.getUserByKey(model.getKey());
            if (user == null) {
                return status(NOT_FOUND)
                        .entity(authenticationContext.getI18nHelper().getText("ru.andreymarkelov.atlas.plugins.jira-bipper-server.rest.nouserbykey"))
                        .build();
            }

            if  (!globalPermissionManager.hasPermission(ADMINISTER, currentUser) && !currentUser.equals(user)) {
                log.warn("Invalid user:{} tries to access phone number", currentUser.getName());
                return status(FORBIDDEN)
                        .entity(authenticationContext.getI18nHelper().getText("ru.andreymarkelov.atlas.plugins.jira-bipper-server.rest.notgranted"))
                        .build();
            }
            contactManager.storePhone(user.getKey(), user.getName(), model.getPhone());
            return Response.ok(model).build();
        } else if (isNotBlank(model.getName())) {
            ApplicationUser user = userManager.getUserByName(model.getName());
            if (user == null) {
                return status(NOT_FOUND)
                        .entity(authenticationContext.getI18nHelper().getText("ru.andreymarkelov.atlas.plugins.jira-bipper-server.rest.nouserbyname"))
                        .build();
            }

            if  (!globalPermissionManager.hasPermission(ADMINISTER, currentUser) && !currentUser.equals(user)) {
                log.warn("Invalid user:{} tries to access phone number", currentUser.getName());
                return status(FORBIDDEN)
                        .entity(authenticationContext.getI18nHelper().getText("ru.andreymarkelov.atlas.plugins.jira-bipper-server.rest.notgranted"))
                        .build();
            }
            contactManager.storePhone(user.getKey(), user.getName(), model.getPhone());
            return Response.ok(model).build();
        } else {
            return status(BAD_REQUEST)
                    .entity(authenticationContext.getI18nHelper().getText("ru.andreymarkelov.atlas.plugins.jira-bipper-server.rest.nouserkeyorname"))
                    .build();
        }
    }
}
