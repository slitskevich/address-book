package com.contacts;

import java.util.List;
import java.util.logging.Logger;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.validator.routines.EmailValidator;

import com.contacts.db.ContactDao;
import com.contacts.entity.Contact;
import com.contacts.entity.EntityList;

@Path("contacts")
public class ContactResource {
	
	private static final Logger LOGGER = Logger.getLogger(ContactResource.class.getName());
	
	private static final String PAGE_URI_FORMAT = "<%s?offset=%d&limit=%d>; rel=\"%s\"";
	
	private static ContactDao dao = new ContactDao();
	
	@GET
	@Path("")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAll(@Context ContainerRequestContext request, @QueryParam("offset") int offset, @QueryParam("limit") int limit) {
		LOGGER.info("getAll started, offset: " + offset + ", limit: " + limit);
		if (offset < 0 || limit < 0) {
			throw new BadRequestException();
		} else {
			try {
				int toIndex = offset + limit;
				LOGGER.info("Loading contacts from " + offset + " to " + toIndex);
				String uri = request.getUriInfo().getAbsolutePath().toString();
				
				EntityList<Contact> result = dao.load(offset, limit);
				
				StringBuilder links = new StringBuilder();
				if (offset > 0) {
					links.append(String.format(PAGE_URI_FORMAT, uri, 0, limit, "first")).
						  append(",").
						  append(String.format(PAGE_URI_FORMAT, uri, offset - limit < 0 ? 0 : offset - limit, limit, "prev"));
				}
				if (offset + limit < result.getTotalCount()) {
					if (links.length() > 0) {
						links.append(",");
					}
					links.append(String.format(PAGE_URI_FORMAT, uri, result.getTotalCount() - limit, limit, "last")).
						  append(",").
						  append(String.format(PAGE_URI_FORMAT, uri, offset + limit, limit, "next"));
				}

				List<Contact> contacts = result.getItems();
				return Response.ok(contacts.toArray(new Contact[contacts.size()])).
								header("Links", links.toString()).
								build();
			} catch (Exception ex) {
				LOGGER.severe(ex.getMessage());
				ex.printStackTrace();
				throw new InternalServerErrorException();
			}
		}
	}
	
	@GET
	@Path("{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getById(@PathParam("id") int id) {
		try {
			Contact contact = dao.loadById(id);
			if (contact != null) {
				return Response.ok(contact).build();
			} else {
				throw new NotFoundException();
			}
		} catch (Exception ex) {
			LOGGER.severe(ex.getMessage());
			ex.printStackTrace();
			throw new InternalServerErrorException();
		}
	}
	
	@POST
    @Path("")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response insert(Contact contact) {
		LOGGER.info("Call to create contact");
		try {
			dao.create(contact);
			return Response.status(201).build();
		} catch (Exception ex) {
			LOGGER.severe(ex.getMessage());
			ex.printStackTrace();
			throw new InternalServerErrorException();
		}
    }
	
	@PUT
	@Path("{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response update(@PathParam("id") int id, Contact contactUpdate) {
		LOGGER.info("Call to update contact with id: " + id);
		try {
			if (EmailValidator.getInstance().isValid(contactUpdate.getAddress())) {
				dao.update(id, contactUpdate);
				return Response.ok().build();
			} else {
				return Response.status(400).entity(new Status("Invalid e-mail address: " + contactUpdate.getAddress())).build();
			}
		} catch (Exception ex) {
			LOGGER.severe(ex.getMessage());
			ex.printStackTrace();
			throw new InternalServerErrorException();
		}
	}
	
	@DELETE
	@Path("{id}")
	public Response delete(@PathParam("id") int id) {
		LOGGER.info("Call to update contact with id: " + id);
		try {
			dao.delete(id);
			return Response.status(204).build();
		} catch (Exception ex) {
			LOGGER.severe(ex.getMessage());
			ex.printStackTrace();
			throw new InternalServerErrorException();
		}
	}
}
