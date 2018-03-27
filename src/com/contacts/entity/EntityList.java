package com.contacts.entity;

import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class EntityList <T extends Entity> {
	
	private List<T> items;
	int totalCount;
	
	public EntityList(ResultSet set, Class<? extends Entity> entityClass) throws SQLException, InstantiationException, IllegalAccessException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException {
		this.items = new ArrayList<T>(set.getFetchSize());
		
		while (set.next()) {
			@SuppressWarnings("unchecked")
			T item = (T) entityClass.getDeclaredConstructor(ResultSet.class).newInstance(set);
			this.items.add(item);
		}
	}
	
	public List<T> getItems() {
		return items;
	}
	
	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}
	
	public int getTotalCount() {
		return totalCount;
	}

}
