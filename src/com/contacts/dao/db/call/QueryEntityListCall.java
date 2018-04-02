package com.contacts.dao.db.call;

import java.sql.ResultSet;
import java.sql.Statement;

import com.contacts.dao.EntityListPage;
import com.contacts.entity.Entity;

/**
 * The utility to execute list entities queries
 *
 * @param <T> the generic type
 */
public class QueryEntityListCall <T extends Entity> extends DbCall {

	/** The query to calculate total number of entries */
	private static final String TOTAL_COUNT_QUERY = "SELECT FOUND_ROWS()";

	/** The execution result. */
	private EntityListPage<T> listPage;
	
	/** The result entity class. */
	private Class<? extends Entity> entityClass;
	
	/**
	 * Instantiates a new query entity list call.
	 *
	 * @param entityClass the entity class
	 */
	public QueryEntityListCall(Class<? extends Entity> entityClass) {
		this.entityClass = entityClass;
	}

	/**
	 * Executes the SQL statement to load the list of entities. Right after that executes a statement
	 * to calculate the total number of entities regardless of current pagination parameters
	 * 
	 * @see com.contacts.dao.db.call.DbCall#doCall(java.sql.Statement, java.lang.String)
	 */
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
	
	/**
	 * Gets the entity list page.
	 *
	 * @return the entity list page
	 */
	public EntityListPage<T> getEntityListPage() {
		return listPage;
	}
}
