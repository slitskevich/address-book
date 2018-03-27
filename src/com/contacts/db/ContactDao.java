package com.contacts.db;

import com.contacts.entity.Contact;
import com.contacts.entity.EntityList;

public class ContactDao {
	
	private static final String QUERY_ALL_FORMAT = "SELECT SQL_CALC_FOUND_ROWS * FROM contacts LIMIT %d,%d";
	
	public EntityList<Contact> load(int offset, int limit) throws Exception {
		Database db = new Database();
		return db.query(String.format(QUERY_ALL_FORMAT, offset, limit), Contact.class);	
	}

}
