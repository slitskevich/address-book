package com.contacts.dao;

import com.contacts.entity.ContactEntity;

public interface ContactDao {
	public EntityListPage<ContactEntity> load(int offset, int limit) throws Exception;
	public ContactEntity loadById(int id) throws Exception;
	public void update(int id, ContactEntity update) throws Exception;
	public void create(ContactEntity contact) throws Exception;
	public void delete(int id) throws Exception;
}
