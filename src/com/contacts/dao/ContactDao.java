package com.contacts.dao;

import com.contacts.entity.ContactEntity;

/**
 * The Interface ContactDao defines methods to access and modify contacts data
 */
public interface ContactDao {
	
	/**
	 * Loads contacts page.
	 *
	 * @param offset the result page offset
	 * @param limit the result size limit
	 * @return entities list
	 * @throws Exception if the load operation fails
	 */
	public EntityListPage<ContactEntity> load(int offset, int limit) throws Exception;
	
	/**
	 * Loads contact entity by id.
	 *
	 * @param id the id of the contact
	 * @return the contact entity
	 * @throws Exception if the load operation fails
	 */
	public ContactEntity loadById(int id) throws Exception;
	
	/**
	 * Updates contact identified by ID.
	 *
	 * @param id the id of the contact to update
	 * @param update the update values
	 * @throws Exception if the update operation fails
	 */
	public void update(int id, ContactEntity update) throws Exception;
	
	/**
	 * Creates new contact entry
	 *
	 * @param contact the contact to be inserted into data storage.
	 * @throws Exception if the contact can't be created
	 */
	public void create(ContactEntity contact) throws Exception;
	
	/**
	 * Deletes contact identified by ID
	 *
	 * @param id the id of the contact to delete
	 * @throws Exception if the contact can't be deleted.
	 */
	public void delete(int id) throws Exception;
}
