package com.contacts.dao;

import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import com.contacts.entity.Entity;

public class EntityListPage <T extends Entity> {
	
	private List<T> items;
	int totalListSize;
	
	public EntityListPage(ResultSet set, Class<? extends Entity> entityClass) throws SQLException, InstantiationException, IllegalAccessException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException {
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
	
	public void setTotalListSize(int totalCount) {
		this.totalListSize = totalCount;
	}
	
	public int getTotalListSize() {
		return totalListSize;
	}

}
