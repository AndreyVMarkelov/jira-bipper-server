package ru.andreymarkelov.atlas.plugins.jirabipperserver.rest;

import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.atlassian.jira.security.GlobalPermissionManager;
import com.atlassian.jira.security.JiraAuthenticationContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.andreymarkelov.atlas.plugins.jirabipperserver.manager.AuthManager;
import ru.andreymarkelov.atlas.plugins.jirabipperserver.manager.SenderService;

import static javax.ws.rs.core.Response.Status.UNAUTHORIZED;
import static javax.ws.rs.core.Response.status;

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

    @PUT
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response storeAccountSettings(AccountSettingsReqModel model) {
        return status(UNAUTHORIZED).build();
    }
}
