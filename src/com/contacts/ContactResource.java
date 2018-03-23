package com.contacts;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("contacts")
public class ContactResource {
	
	private static List<Contact> contactList;
	
	public ContactResource() {
		initTable();
	}
	
	@GET
	@Path("")
	@Produces(MediaType.APPLICATION_JSON)
	public Contact[] getAll() {
		return contactList.toArray(new Contact[contactList.size()]);
	}
	
	@GET
	@Path("{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Contact getById(@PathParam("id") int id) {
		return findById(id);
	}
	
	@POST
    @Path("")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response insert(Contact contact) {
		contactList.add(contact);
        return Response.status(201).build();
    }
	
	@PUT
	@Path("{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response update(@PathParam("id") int id, Contact contactUpdate) {
		Contact contact = findById(id);
		boolean modified = false;
		if (contactUpdate.getFirstName() != null) {
			contact.setFirstName(contactUpdate.getFirstName());
			modified = true;
		}
		if (contactUpdate.getLastName() != null) {
			contact.setLastName(contactUpdate.getLastName());
			modified = true;
		}
		if (contactUpdate.getEmail() != null) {
			contact.setEmail(contactUpdate.getEmail());
			modified = true;
		}
		return Response.status(modified ? 200 : 304).build();
	}
	
	@DELETE
	@Path("{id}")
	public Response delete(@PathParam("id") int id) {
		Contact contact = findById(id);
		contactList.remove(contact);
		return Response.status(204).build();
	}
	
	private Contact findById(int id) {
		for (Contact next : contactList) {
			if (next.getId() == id) {
				return next;
			}
		}
		throw new NotFoundException();
	}

	
	private void initTable() {
		if (contactList == null) {
			contactList = new ArrayList<Contact>();
			for (int i = 0; i < 20; i += 1) {
				contactList.add(new Contact(i, "first-" + i, "last-" + i, i + ".last@mail.de"));
			}
		}
	}

}
