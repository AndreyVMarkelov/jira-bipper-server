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

import ru.andreymarkelov.atlas.plugins.jirabipperserver.data.Contact;
import ru.andreymarkelov.atlas.plugins.jirabipperserver.manager.ContactManager;
import ru.andreymarkelov.atlas.plugins.jirabipperserver.rest.model.ContactModel;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toList;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

@Path("/contacts")
@Consumes(APPLICATION_JSON)
@Produces(APPLICATION_JSON)
public class ContactsResource {
    private final ContactManager contactManager;

    public ContactsResource(ContactManager contactManager) {
        this.contactManager = contactManager;
    }

    @GET
    @Path("/all")
    public Response getContacts() {
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
        Contact contact = contactManager.save(new Contact(null, "d", contactModel.getName(), contactModel.getPhone()));
        contactModel.setId(contact.getId());
        return Response.ok(contactModel).build();
    }

    @DELETE
    @Path("/self/{id}")
    public Response delete(@PathParam("id") Integer id) {
        contactManager.deleteById(id);
        return Response.ok().build();
    }

    @GET
    @Path("/self/{id}")
    public Response getContact(@PathParam ("id") Integer id) {
        Contact contact = contactManager.getById(id);
        return Response.ok(new ContactModel(contact.getId(), contact.getName(), contact.getPhone())).build();
    }

    @PUT
    @Path("/self/{id}")
    public Response updateContact(@PathParam ("id") Integer id, ContactModel contactModel) {
        Contact contact = contactManager.update(new Contact(id, null, contactModel.getName(), contactModel.getPhone()));
        contactModel.setName(contact.getName());
        contactModel.setPhone(contact.getPhone());
        contactModel.setId(contact.getId());
        return Response.ok(contactModel).build();
    }
}
