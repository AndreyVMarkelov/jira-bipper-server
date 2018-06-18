package ru.andreymarkelov.atlas.plugins.jirabipperserver.rest;

import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.atlassian.jira.security.GlobalPermissionManager;
import com.atlassian.jira.security.JiraAuthenticationContext;
import com.atlassian.jira.user.ApplicationUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.andreymarkelov.atlas.plugins.jirabipperserver.manager.AuthManager;
import ru.andreymarkelov.atlas.plugins.jirabipperserver.manager.SenderService;
import ru.andreymarkelov.atlas.plugins.jirabipperserver.rest.model.AccountGenerateKeyReqModel;
import ru.andreymarkelov.atlas.plugins.jirabipperserver.rest.model.AccountSetupKeyReqRespModel;

import static com.atlassian.jira.permission.GlobalPermissionKey.ADMINISTER;
import static javax.ws.rs.core.Response.Status.BAD_REQUEST;
import static javax.ws.rs.core.Response.Status.FORBIDDEN;
import static javax.ws.rs.core.Response.Status.OK;
import static javax.ws.rs.core.Response.status;
import static org.apache.commons.lang3.StringUtils.isBlank;

@Path("/admin")
public class AdminResource {
    private static final Logger log = LoggerFactory.getLogger(AdminResource.class);

    private final SenderService senderService;
    private final JiraAuthenticationContext authenticationContext;
    private final GlobalPermissionManager globalPermissionManager;
    private final AuthManager authManager;

    public AdminResource(
            SenderService senderService,
            JiraAuthenticationContext authenticationContext,
            GlobalPermissionManager globalPermissionManager,
            AuthManager authManager) {
        this.senderService = senderService;
        this.authenticationContext = authenticationContext;
        this.globalPermissionManager = globalPermissionManager;
        this.authManager = authManager;
    }

    @Path("/generatekey")
    @PUT
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response storeAccountSettings(AccountGenerateKeyReqModel model) {
        ApplicationUser currentUser = authenticationContext.getLoggedInUser();
        if (!globalPermissionManager.hasPermission(ADMINISTER, currentUser)) {
            log.warn("Invalid user:{} tries to access phone number", currentUser.getName());
            return status(FORBIDDEN)
                    .entity(authenticationContext.getI18nHelper().getText("ru.andreymarkelov.atlas.plugins.jira-bipper-server.rest.admin.notgranted"))
                    .build();
        }

        if (isBlank(model.getSender()) || isBlank(model.getAccountId()) || isBlank(model.getPassword())) {
            return status(BAD_REQUEST)
                    .entity(authenticationContext.getI18nHelper().getText("ru.andreymarkelov.atlas.plugins.jira-bipper-server.rest.admin.nosenderandcreds"))
                    .build();
        }

        try {
            String apiKey = senderService.generateApiKey(model.getAccountId(), model.getPassword());
            Long generationTime = System.currentTimeMillis();
            authManager.setSenderName(model.getSender());
            authManager.setApiKey(apiKey);
            authManager.setGenerationTime(generationTime);
            return status(OK).entity(new AccountSetupKeyReqRespModel(model.getSender(), apiKey, generationTime)).build();
        } catch (Exception e) {
            return status(BAD_REQUEST)
                    .entity(authenticationContext.getI18nHelper().getText("ru.andreymarkelov.atlas.plugins.jira-bipper-server.rest.admin.generatekey", e.getMessage()))
                    .build();
        }
    }

    @Path("/savekey")
    @PUT
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response storeAccount1Settings(AccountSetupKeyReqRespModel model) {
        ApplicationUser currentUser = authenticationContext.getLoggedInUser();
        if (!globalPermissionManager.hasPermission(ADMINISTER, currentUser)) {
            log.warn("Invalid user:{} tries to access phone number", currentUser.getName());
            return status(FORBIDDEN)
                    .entity(authenticationContext.getI18nHelper().getText("ru.andreymarkelov.atlas.plugins.jira-bipper-server.rest.admin.notgranted"))
                    .build();
        }

        if (isBlank(model.getSender()) || isBlank(model.getApiKey())) {
            return status(BAD_REQUEST)
                    .entity(authenticationContext.getI18nHelper().getText("ru.andreymarkelov.atlas.plugins.jira-bipper-server.rest.admin.nosenderandapikey"))
                    .build();
        }

        authManager.setSenderName(model.getSender());
        authManager.setApiKey(model.getApiKey());
        authManager.setGenerationTime(null);
        return status(OK).entity(model).build();
    }
}
