package ru.andreymarkelov.atlas.plugins.jirabipperserver.rest;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.atlassian.jira.security.JiraAuthenticationContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.andreymarkelov.atlas.plugins.jirabipperserver.manager.ContactManager;

@Path("/phonenumbers")
public class NumberResource {
    private static final Logger log = LoggerFactory.getLogger(NumberResource.class);

    private final JiraAuthenticationContext authenticationContext;
    private final ContactManager contactManager;

    public NumberResource(
            JiraAuthenticationContext authenticationContext,
            ContactManager contactManager) {
        this.authenticationContext = authenticationContext;
        this.contactManager = contactManager;
    }

    @GET
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getMessage(@QueryParam("key") String key) {
        if (key!=null) {
            return Response.ok(new PhoneNumberModel(key)).build();
        } else
            return Response.ok(new PhoneNumberModel("default")).build();
    }

    @PUT
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response putdMessage() {
        return Response.ok(new PhoneNumberModel("default")).build();
    }

    @POST
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response putMessage() {
            return Response.ok(new PhoneNumberModel("default")).build();
    }
}
