package com.contacts.dao;

import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import com.contacts.entity.Entity;

/**
 * Represents entity list page
 *
 * @param <T> the generic type
 */
public class EntityListPage <T extends Entity> {
	
	/** The entity list page items. */
	private List<T> items;
	
	/** The total list size. */
	int totalListSize;
	
	/**
	 * Instantiates a new entity list page.
	 *
	 * @param set the DB result set containing all the entities data.
	 * @param entityClass the entity class
	 * @throws SQLException the SQL exception
	 * @throws InstantiationException the instantiation exception
	 * @throws IllegalAccessException the illegal access exception
	 * @throws NoSuchMethodException the no such method exception
	 * @throws SecurityException the security exception
	 * @throws IllegalArgumentException the illegal argument exception
	 * @throws InvocationTargetException the invocation target exception
	 */
	public EntityListPage(ResultSet set, Class<? extends Entity> entityClass) throws SQLException, InstantiationException, IllegalAccessException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException {
		this.items = new ArrayList<T>(set.getFetchSize());
		
		while (set.next()) {
			@SuppressWarnings("unchecked")
			T item = (T) entityClass.getDeclaredConstructor(ResultSet.class).newInstance(set);
			this.items.add(item);
		}
	}
	
	/**
	 * Instantiates a new entity list page.
	 *
	 * @param list the list
	 */
	public EntityListPage(Collection<? extends T> list) {
		this.items = new ArrayList<T>(list.size());
		this.items.addAll(list);
	}
	
	/**
	 * Gets the items.
	 *
	 * @return the items
	 */
	public List<T> getItems() {
		return items;
	}
	
	/**
	 * Sets the total list size.
	 *
	 * @param totalCount the new total list size
	 */
	public void setTotalListSize(int totalCount) {
		this.totalListSize = totalCount;
	}
	
	/**
	 * Gets the total list size.
	 *
	 * @return the total list size
	 */
	public int getTotalListSize() {
		return totalListSize;
	}

}
