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
import javax.ws.rs.core.Link;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.contacts.dao.ContactDao;
import com.contacts.dao.EntityListPage;
import com.contacts.dao.db.ContactDBDao;
import com.contacts.entity.ContactEntity;
import com.contacts.entity.ValidationException;

/**
 * The Class ContactResource implements REST API calls to retrieve and operate with contacts.
 */
@Path("contacts")
public class ContactResource {

	private static final Logger LOGGER = Logger.getLogger(ContactResource.class.getName());

	/** String format for the pagination links */
	private static final String PAGE_URI_FORMAT = "<%s?offset=%d&limit=%d>; rel=\"%s\"";
	
	/** Pagination links separator for response Links header */
	static final String LINKS_SEPARATOR = ",";
	
	/** Request offset query parameter name */
	static final String OFFSET_PARAMETER = "offset";
	
	/** Request limit query parameter name */
	static final String LIMIT_PARAMETER = "limit";
	
	/** Pagination link rel parameter value */
	static final String FIRST_REL = "first";
	
	/** Pagination link rel parameter value */
	static final String PREV_REL = "prev";
	
	/** Pagination link rel parameter value */
	static final String NEXT_REL = "next";
	
	/** Pagination link rel parameter value */
	static final String LAST_REL = "last";

	/** The DAO to access contacts. */
	private ContactDao dao = new ContactDBDao();

	/**
	 * Instantiates a new contact resource.
	 */
	public ContactResource() {
	}
	
	/**
	 * Instantiates a new contact resource and overrides default DAO value - used for testing.
	 *
	 * @param dao the new DAO value
	 */
	ContactResource(ContactDao dao) {
		this.dao = dao;
	}

	/**
	 * Processes requests to load contacts list. Returns result page and sets Links header with pagination links.
	 *
	 * @param request the request
	 * @param offset the result page offset
	 * @param limit the result page limit
	 * @return Response with list of contacts as an entity and Links header with pagination links, 
	 * returns BAD_REQUEST status if pagination parameter are invalid or INTERNAL_SERVER_ERROR if fails to load contacts.
	 */
	@GET
	@Path("")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAll(@Context ContainerRequestContext request, @QueryParam("offset") int offset,
			@QueryParam("limit") int limit) {
		return getAllResponse(request.getUriInfo().getAbsolutePath().toString(), offset, limit);
	}
	
	/**
	 * Auxiliary method to process load contacts page requests. Extracted for text purposes.
	 *
	 * @param uri the uri for pagination links
	 * @param offset the result page offset
	 * @param limit the result page limit
	 * @return Response with list of contacts as an entity and Links header with pagination links, 
	 * returns BAD_REQUEST status if pagination parameter are invalid or INTERNAL_SERVER_ERROR if fails to load contacts.
	 */
	Response getAllResponse(String uri, int offset, int limit) {
		LOGGER.info("getAll started, offset: " + offset + ", limit: " + limit);
		if (offset >= 0 && limit >= 0) {
			try {
				int toIndex = offset + limit;
				LOGGER.info("Loading contacts from " + offset + " to " + toIndex);
				EntityListPage<ContactEntity> result = dao.load(offset, limit);

				List<ContactEntity> contacts = result.getItems();
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

	/**
	 * Loads contact for specified ID
	 *
	 * @param id the id of the contact
	 * @return retrieved contact entity; BAD_REQUEST response if no contact with the ID can be found; INTERNAL_SERVER_ERROR response if execution fails;
	 */
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

	/**
	 * Validates and persists new contact entity
	 *
	 * @param contact the contact to be created
	 * @return successful status response; BAD_REQUEST response if contact data is invalid; INTERNAL_SERVER_ERROR if execution fails;
	 */
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

	/**
	 * Validates update values and persists changed contact.
	 *
	 * @param id the id of the contact to be updated
	 * @param contactUpdate the contact update values
	 * @return successful status response; BAD_REQUEST response if contact data is invalid; INTERNAL_SERVER_ERROR if execution fails;
	 */
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

	/**
	 * Deletes contact identified by ID
	 *
	 * @param id the id of the contact to be deleted
	 * @return the successful response; INTERNAL_SERVER_ERROR if execution fails;
	 */
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

	/**
	 * Builds the Links header value.
	 *
	 * @param uri the uri to be used for every link
	 * @param totalSize the total number of contact entities
	 * @param currentOffset the current page offset
	 * @param limit the page size limit
	 * @return value for Links header
	 */
	private String buildLinksHeaderValue(String uri, int totalSize, int currentOffset, int limit) {
		StringBuilder links = new StringBuilder();
		if (currentOffset > 0) {
			links.append(String.format(PAGE_URI_FORMAT, uri, 0, limit, FIRST_REL)).
				  append(LINKS_SEPARATOR).
				  append(String.format(PAGE_URI_FORMAT, uri, currentOffset - limit < 0 ? 0 : currentOffset - limit, limit, PREV_REL));
		}
		if (currentOffset + limit < totalSize) {
			if (links.length() > 0) {
				links.append(LINKS_SEPARATOR);
			}
			links.append(String.format(PAGE_URI_FORMAT, uri, totalSize - limit, limit, LAST_REL)).
				  append(LINKS_SEPARATOR).
				  append(String.format(PAGE_URI_FORMAT, uri, currentOffset + limit, limit, NEXT_REL));
		}
		return links.toString();
	}

}
