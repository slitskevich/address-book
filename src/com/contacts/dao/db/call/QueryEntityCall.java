package com.contacts.dao.db.call;

import java.sql.ResultSet;
import java.sql.Statement;

import com.contacts.entity.Entity;

public class QueryEntityCall <T extends Entity> extends DbCall {
	
	private T entity;
	
	private Class<? extends Entity> entityClass;
	
	public QueryEntityCall(Class<? extends Entity> entityClass) {
		this.entityClass = entityClass;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void doCall(Statement statement, String sql) throws Exception {
		ResultSet result = statement.executeQuery(sql);
		if (result.next()) {
			entity = (T) entityClass.getDeclaredConstructor(ResultSet.class).newInstance(result);
		}        
		result.close();
	}
	
	public T getEntity() {
		return entity;
	}
}
