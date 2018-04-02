/**
 * 
 */
package com.contacts.resources;

import java.util.*;

import com.contacts.dao.ContactDao;
import com.contacts.dao.EntityListPage;
import com.contacts.entity.ContactEntity;

/**
 * @author slava
 *
 */
public class ContactDaoMock implements ContactDao {
	
	List<ContactEntity> contacts = new ArrayList<ContactEntity>();
	
	public ContactDaoMock() {
		for (int i = 0; i < 10; i += 1) {
			contacts.add(new ContactEntity(i, "" + i, "a", i + ".a@mail.de"));
		}
	}

	/* (non-Javadoc)
	 * @see com.contacts.dao.ContactDao#load(int, int)
	 */
	@Override
	public EntityListPage<ContactEntity> load(int offset, int limit) throws Exception {
		List<ContactEntity> list = contacts.subList(offset, offset + limit);
		EntityListPage<ContactEntity> result = new EntityListPage<ContactEntity>(list);
		result.setTotalListSize(contacts.size());
		
		return result;
	}

	/* (non-Javadoc)
	 * @see com.contacts.dao.ContactDao#loadById(int)
	 */
	@Override
	public ContactEntity loadById(int id) throws Exception {
		for (ContactEntity next : contacts) {
			if (next.getId() == id) {
				return next;
			}
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see com.contacts.dao.ContactDao#update(int, com.contacts.entity.ContactEntity)
	 */
	@Override
	public void update(int id, ContactEntity update) throws Exception {
		boolean updated = false;
		for (ContactEntity next : contacts) {
			if (next.getId() == id) {
				next.setFirstName(update.getFirstName());
				next.setLastName(update.getLastName());
				next.setAddress(update.getAddress());
				updated = true;
				break;
			}
		}
		if (!updated) {
			throw new Exception();
		}
	}

	/* (non-Javadoc)
	 * @see com.contacts.dao.ContactDao#create(com.contacts.entity.ContactEntity)
	 */
	@Override
	public void create(ContactEntity contact) throws Exception {
		for (ContactEntity next : contacts) {
			if (next.getId() == contact.getId()) {
				throw new Exception();
			}
		}
		contacts.add(contact);
	}

	/* (non-Javadoc)
	 * @see com.contacts.dao.ContactDao#delete(int)
	 */
	@Override
	public void delete(int id) throws Exception {
		int index = -1;
		for (int i = 0; i < contacts.size(); i += 1) {
			if (contacts.get(i).getId() == id) {
				index = i;
				break;
			}
		}
		if (index == -1) {
			throw new Exception();
		} else {
			contacts.remove(index);
		}
	}

}
