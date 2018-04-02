package com.contacts.dao.db.call;

import java.sql.ResultSet;
import java.sql.Statement;

import com.contacts.entity.Entity;

/**
 * Utility to query entity
 *
 * @param <T> the generic type
 */
public class QueryEntityCall <T extends Entity> extends DbCall {
	
	/** The entity. */
	private T entity;
	
	/** The resulting entity class. */
	private Class<? extends Entity> entityClass;
	
	/**
	 * Instantiates a new query entity call.
	 *
	 * @param entityClass the entity class
	 */
	public QueryEntityCall(Class<? extends Entity> entityClass) {
		this.entityClass = entityClass;
	}

	/* (non-Javadoc)
	 * @see com.contacts.dao.db.call.DbCall#doCall(java.sql.Statement, java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	protected void doCall(Statement statement, String sql) throws Exception {
		ResultSet result = statement.executeQuery(sql);
		if (result.next()) {
			entity = (T) entityClass.getDeclaredConstructor(ResultSet.class).newInstance(result);
		}        
		result.close();
	}
	
	/**
	 * Gets the entity.
	 *
	 * @return the entity
	 */
	public T getEntity() {
		return entity;
	}
}
