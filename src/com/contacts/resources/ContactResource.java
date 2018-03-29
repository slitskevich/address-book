package com.contacts.resources;

import java.util.List;
import java.util.logging.Logger;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
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
import javax.ws.rs.core.Response.Status;

import com.contacts.dao.ContactDao;
import com.contacts.dao.EntityListPage;
import com.contacts.dao.db.ContactDBDao;
import com.contacts.entity.ContactEntity;
import com.contacts.entity.ValidationException;

@Path("contacts")
public class ContactResource {

	private static final Logger LOGGER = Logger.getLogger(ContactResource.class.getName());

	private static final String PAGE_URI_FORMAT = "<%s?offset=%d&limit=%d>; rel=\"%s\"";

	private static ContactDao dao = new ContactDBDao();

	public ContactResource() {
	}

	@GET
	@Path("")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAll(@Context ContainerRequestContext request, @QueryParam("offset") int offset,
			@QueryParam("limit") int limit) {
		LOGGER.info("getAll started, offset: " + offset + ", limit: " + limit);
		if (offset >= 0 && limit >= 0) {
			try {
				int toIndex = offset + limit;
				LOGGER.info("Loading contacts from " + offset + " to " + toIndex);
				EntityListPage<ContactEntity> result = dao.load(offset, limit);

				List<ContactEntity> contacts = result.getItems();
				String uri = request.getUriInfo().getAbsolutePath().toString();
				String links = this.buildLinksHeaderValue(uri, result.getTotalListSize(), offset, limit);

				return Response.ok(contacts.toArray(new ContactEntity[contacts.size()])).header("Links", links).build();
			} catch (Exception ex) {
				LOGGER.severe(ex.getMessage());
				return Response.status(Status.INTERNAL_SERVER_ERROR).entity(new ResourceStatus(ex.getMessage())).build();
			}
		} else {
			return Response.status(Status.BAD_REQUEST)
					.entity(new ResourceStatus("Invalid pagination parameters (offset: " + offset + ", limit: " + limit))
					.build();
		}
	}

	@GET
	@Path("{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getById(@PathParam("id") int id) {
		try {
			ContactEntity contact = dao.loadById(id);
			if (contact != null) {
				return Response.ok(contact).build();
			} else {
				return Response.status(Status.BAD_REQUEST).entity(new ResourceStatus("Can't find contact with id: " + id))
						.build();
			}
		} catch (Exception ex) {
			LOGGER.severe(ex.getMessage());
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(new ResourceStatus(ex.getMessage())).build();
		}
	}

	@POST
	@Path("")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response insert(ContactEntity contact) {
		LOGGER.info("Call to create contact");
		try {
			contact.validate();
			dao.create(contact);
			return Response.status(Status.CREATED).build();
		} catch (ValidationException ex) {
			return Response.status(Status.BAD_REQUEST).entity(new ResourceStatus(ex.getMessage())).build();
		} catch (Exception ex) {
			LOGGER.severe(ex.getMessage());
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(new ResourceStatus(ex.getMessage())).build();
		}
	}

	@PUT
	@Path("{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response update(@PathParam("id") int id, ContactEntity contactUpdate) {
		LOGGER.info("Call to update contact with id: " + id);
		try {
			contactUpdate.validate();
			dao.update(id, contactUpdate);
			return Response.ok().build();
		} catch (ValidationException ex) {
			return Response.status(Status.BAD_REQUEST).entity(new ResourceStatus(ex.getMessage())).build();
		} catch (Exception ex) {
			LOGGER.severe(ex.getMessage());
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(new ResourceStatus(ex.getMessage())).build();
		}
	}

	@DELETE
	@Path("{id}")
	public Response delete(@PathParam("id") int id) {
		LOGGER.info("Call to update contact with id: " + id);
		try {
			dao.delete(id);
			return Response.status(Status.NO_CONTENT).build();
		} catch (Exception ex) {
			LOGGER.severe(ex.getMessage());
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(new ResourceStatus(ex.getMessage())).build();
		}
	}

	private String buildLinksHeaderValue(String uri, int totalSize, int currentOffset, int limit) {
		StringBuilder links = new StringBuilder();
		if (currentOffset > 0) {
			links.append(String.format(PAGE_URI_FORMAT, uri, 0, limit, "first")).append(",").append(String.format(
					PAGE_URI_FORMAT, uri, currentOffset - limit < 0 ? 0 : currentOffset - limit, limit, "prev"));
		}
		if (currentOffset + limit < totalSize) {
			if (links.length() > 0) {
				links.append(",");
			}
			links.append(String.format(PAGE_URI_FORMAT, uri, totalSize - limit, limit, "last")).append(",")
					.append(String.format(PAGE_URI_FORMAT, uri, currentOffset + limit, limit, "next"));
		}
		return links.toString();
	}

}
