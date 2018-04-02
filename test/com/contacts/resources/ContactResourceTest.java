package com.contacts.resources;

import static org.junit.jupiter.api.Assertions.*;

import javax.ws.rs.core.Response;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.contacts.dao.EntityListPage;
import com.contacts.entity.ContactEntity;
import com.contacts.entity.ValidationException;

class ContactResourceTest {
	
	ContactResource tested;
	
	@BeforeEach
	void setUp() throws Exception {
		ContactDaoMock daoMock = new ContactDaoMock();
		tested = new ContactResource(daoMock);
	}

	@Test
	void testGetAll() {
		String TEST_URI = "http://me";
		int TEST_OFFSET = 1;
		int TEST_LIMIT = 3;
		Response response = tested.getAllResponse(TEST_URI, TEST_OFFSET, TEST_LIMIT);
		assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
		ContactEntity[] page = (ContactEntity[]) response.getEntity();
		assertEquals(TEST_LIMIT, page.length);
		String[] links = response.getHeaderString("Links").split(ContactResource.LINKS_SEPARATOR);
		assertEquals(4, links.length);
	}

	@Test
	void testGetAllInvalid() {
		String TEST_URI = "http://me";
		int TEST_OFFSET = -1;
		int TEST_LIMIT = 3;
		Response response = tested.getAllResponse(TEST_URI, TEST_OFFSET, TEST_LIMIT);
		assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
	}
	
	@Test
	void testGetByValidId() {
		int TEST_ID = 1;
		Object result = tested.getById(TEST_ID).getEntity();
		ContactEntity existing = (ContactEntity)result;
		assertEquals(TEST_ID, existing.getId());
	}

	@Test
	void testGetByInvalidId() {
		int TEST_ID = -1;
		assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), tested.getById(TEST_ID).getStatus());
	}

	@Test
	void testInsertValid() {
		int TEST_ID = 100;
		ContactEntity entity = new ContactEntity(TEST_ID, "" + TEST_ID, "a", TEST_ID + ".a@mail.de");
		tested.insert(entity);
		ContactEntity found = (ContactEntity) tested.getById(TEST_ID).getEntity();
		assertEquals(TEST_ID, found.getId());
	}

	@Test
	void testInsertExistingId() {
		int TEST_ID = 1;
		ContactEntity entity = new ContactEntity(TEST_ID, "" + TEST_ID, "a", TEST_ID + ".a@mail.de");
		tested.insert(entity);
		assertEquals(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), tested.insert(entity).getStatus());
	}

	@Test
	void testInsertMissingId() {
		ContactEntity entity = new ContactEntity();
		tested.insert(entity);
		assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), tested.insert(entity).getStatus());
	}

	@Test
	void testInsertInvalidEmail() {
		ContactEntity entity = new ContactEntity(100, "a", "b", "invalid");
		tested.insert(entity);
		assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), tested.insert(entity).getStatus());
	}

	@Test
	void testUpdateValid() {
		int TEST_ID = 1;
		String TEST_FIRST_NAME = "TEST";
		ContactEntity entity = (ContactEntity) tested.getById(TEST_ID).getEntity();
		entity.setFirstName(TEST_FIRST_NAME);
		tested.update(TEST_ID, entity);
		ContactEntity result = (ContactEntity) tested.getById(TEST_ID).getEntity();
		assertEquals(TEST_FIRST_NAME, result.getFirstName());
	}
	
	@Test
	void testUpdateInvalidId() {
		int TEST_ID = 1000;
		ContactEntity entity = new ContactEntity(TEST_ID, "A", "B", "a.b@mail.de");
		assertEquals(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), tested.update(TEST_ID, entity).getStatus());
	}

	@Test
	void testUpdateInvalidEmail() {
		int TEST_ID = 1;
		ContactEntity entity = new ContactEntity(TEST_ID, "A", "B", "invalid");
		assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), tested.update(TEST_ID, entity).getStatus());
	}

	@Test
	void testDeleteValid() {
		int TEST_ID = 1;
		assertEquals(Response.Status.NO_CONTENT.getStatusCode(), tested.delete(TEST_ID).getStatus());
	}

	@Test
	void testDeleteInvalid() {
		int TEST_ID = -1;
		assertEquals(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), tested.delete(TEST_ID).getStatus());
	}

}
