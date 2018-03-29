package com.contacts.dao.db.call;

import java.sql.ResultSet;
import java.sql.Statement;

import com.contacts.dao.EntityListPage;
import com.contacts.entity.Entity;

public class QueryEntityListCall <T extends Entity> extends DbCall {

	private static final String TOTAL_COUNT_QUERY = "SELECT FOUND_ROWS()";

	private EntityListPage<T> listPage;
	
	private Class<? extends Entity> entityClass;
	
	public QueryEntityListCall(Class<? extends Entity> entityClass) {
		this.entityClass = entityClass;
	}

	@Override
	protected void doCall(Statement statement, String sql) throws Exception {
		ResultSet result = statement.executeQuery(sql);
		listPage = new EntityListPage<T>(result, entityClass);
		
		ResultSet counter = statement.executeQuery(TOTAL_COUNT_QUERY);
		counter.next();
		listPage.setTotalListSize(counter.getInt(1));

		result.close();
		counter.close();
	}
	
	public EntityListPage<T> getEntityListPage() {
		return listPage;
	}
}
