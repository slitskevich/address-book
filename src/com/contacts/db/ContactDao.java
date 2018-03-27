package com.contacts.db;

import com.contacts.entity.Contact;
import com.contacts.entity.EntityList;

public class ContactDao {
	
	private static final String QUERY_ALL_FORMAT = "SELECT SQL_CALC_FOUND_ROWS * FROM contacts LIMIT %d,%d";
	private static final String QUERY_BY_ID = "SELECT * FROM contacts WHERE id=%d";
	
	public EntityList<Contact> load(int offset, int limit) throws Exception {
		Database db = new Database();
		return db.query(String.format(QUERY_ALL_FORMAT, offset, limit), Contact.class);	
	}
	
	public Contact loadById(int id) throws Exception {
		Database db = new Database();
		return db.queryEntity(String.format(QUERY_BY_ID, id), Contact.class);
	}

}
