package com.contacts;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

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
		Contact contact = findById(id);
		return contact;
	}
	
	@POST
    @Path("")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Status insert(Contact contact) {
		contactList.add(contact);
        return new Status("SUCCESS", "Inserted " + contact.getEmail());
    }
	
	@PUT
	@Path("{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Status update(@PathParam("id") int id, Contact contactUpdate) {
		Contact contact = findById(id);
		if (contact != null) {
			if (contactUpdate.getFirstName() != null) {
				contact.setFirstName(contactUpdate.getFirstName());
			}
			if (contactUpdate.getLastName() != null) {
				contact.setLastName(contactUpdate.getLastName());
			}
			if (contactUpdate.getEmail() != null) {
				contact.setEmail(contactUpdate.getEmail());
			}
			return new Status("SUCCESS", "Updated contact with id: " + id);
		} else {
			return new Status("FAILURE", "Failed to find a contact with id: " + id);
		}		
	}
	
	@DELETE
	@Path("{id}")
	public Status delete(@PathParam("id") int id) {
		Contact contact = findById(id);
		if (contact != null) {
			contactList.remove(contact);
			return new Status("SUCCESS", "Deleted contact with id: " + id);
		} else {
			return new Status("FAILURE", "Failed to find a contact with id: " + id);
		}
	}
	
	private Contact findById(int id) {
		for (Contact next : contactList) {
			if (next.getId() == id) {
				return next;
			}
		}
		return null;
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
