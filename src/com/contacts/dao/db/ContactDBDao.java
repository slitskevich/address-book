package com.contacts.dao.db;

import com.contacts.dao.ContactDao;
import com.contacts.dao.EntityListPage;
import com.contacts.dao.db.call.QueryEntityCall;
import com.contacts.dao.db.call.QueryEntityListCall;
import com.contacts.dao.db.call.UpdateEntityCall;
import com.contacts.entity.ContactEntity;

/**
 * The Class ContactDBDao. Implements DB data access methods.
 */
public class ContactDBDao implements ContactDao {
	
	/** SQL statement format to query all contacts */
	private static final String QUERY_ALL_FORMAT = "SELECT SQL_CALC_FOUND_ROWS * FROM contacts LIMIT %d,%d";
	
	/** SQL statement format to query contact entry by Id */
	private static final String QUERY_BY_ID = "SELECT * FROM contacts WHERE id=%d";
	
	/** SQL statement format to update contact entry */
	private static final String UPDATE = "UPDATE contacts SET email=\"%s\", firstName=\"%s\", lastName=\"%s\" WHERE id=%d";
	
	/** SQL statement format to create a new entry */
	private static final String CREATE = "INSERT INTO contacts (email, firstName, lastName) VALUES (\"%s\", \"%s\", \"%s\")";
	
	/** SQL statement format to delete contact entry */
	private static final String DELETE = "DELETE FROM contacts WHERE id=%d";
	
	/** (non-Javadoc)
	 * @see com.contacts.dao.ContactDao#load(int, int)
	 */
	public EntityListPage<ContactEntity> load(int offset, int limit) throws Exception {
		QueryEntityListCall<ContactEntity> call = new QueryEntityListCall<ContactEntity>(ContactEntity.class);
		call.execute(String.format(QUERY_ALL_FORMAT, offset, limit));
		return call.getEntityListPage();
	}
	
	/* (non-Javadoc)
	 * @see com.contacts.dao.ContactDao#loadById(int)
	 */
	public ContactEntity loadById(int id) throws Exception {
		QueryEntityCall<ContactEntity> call = new QueryEntityCall<ContactEntity>(ContactEntity.class);
		call.execute(String.format(QUERY_BY_ID, id));
		return call.getEntity();
	}
	
	/* (non-Javadoc)
	 * @see com.contacts.dao.ContactDao#update(int, com.contacts.entity.ContactEntity)
	 */
	public void update(int id, ContactEntity update) throws Exception {
		UpdateEntityCall call = new UpdateEntityCall();
		call.execute(String.format(UPDATE, update.getAddress(), update.getFirstName(), update.getLastName(), update.getId()));
	}
	
	/* (non-Javadoc)
	 * @see com.contacts.dao.ContactDao#create(com.contacts.entity.ContactEntity)
	 */
	public void create(ContactEntity contact) throws Exception {
		UpdateEntityCall call = new UpdateEntityCall();
		call.execute(String.format(CREATE, contact.getAddress(), contact.getFirstName(), contact.getLastName()));
	}
	
	/* (non-Javadoc)
	 * @see com.contacts.dao.ContactDao#delete(int)
	 */
	public void delete(int id) throws Exception {
		UpdateEntityCall call = new UpdateEntityCall();
		call.execute(String.format(DELETE, id));
	}

}
