package com.contacts.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Logger;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import com.contacts.entity.Entity;
import com.contacts.entity.EntityList;

public class Database {

	private static final Logger LOGGER = Logger.getLogger(Database.class.getName());

	private static DataSource dataSource;

	private static final String DATA_SOURCE_NAME = "jdbc/db";
	
	private static final String TOTAL_COUNT_QUERY = "SELECT FOUND_ROWS()";

	public Database() {
		try {
			dataSource = (DataSource) new InitialContext().lookup("java:comp/env/" + DATA_SOURCE_NAME);
		} catch (NamingException e) {
			throw new IllegalStateException(DATA_SOURCE_NAME + " is missing in JNDI!", e);
		}
	}

	public Connection getConnection() throws SQLException {
		return dataSource.getConnection();
	}

	public <T extends Entity> EntityList<T> query(String sql, Class<? extends Entity> entityClass) throws Exception {
		LOGGER.info("Query database: " + sql);
		Connection connection = null;
		Statement statement = null;
		ResultSet result = null;
		ResultSet counter = null;
		EntityList<T> list = null;
		try {
			connection = dataSource.getConnection();
			statement = connection.createStatement();
			result = statement.executeQuery(sql);
	        list = new EntityList<T>(result, entityClass);
	        
			counter = statement.executeQuery(TOTAL_COUNT_QUERY);
			counter.next();
	        list.setTotalCount(counter.getInt(1));
	        
			result.close();
			statement.close();
			connection.close();
		} catch (SQLException se) {
			LOGGER.severe("Failed to query DB");
			se.printStackTrace();
			throw se;
		} finally {
			try {
				if (statement != null) {
					statement.close();
				}
			} catch (SQLException se) {
				LOGGER.severe("Failed to close DB statement: " + statement.toString());
				se.printStackTrace();
			} 
			try {
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException se) {
				LOGGER.severe("Failed to close DB connection: " + se.getMessage());
				se.printStackTrace();
			} 
		}
		return list;
	}
	
	class QueryResult {
		
		private ResultSet set;
		private int totalCount;
		
		public QueryResult(ResultSet set, int totalCount) {
			this.set = set;
			this.totalCount = totalCount;
		}
		
		public ResultSet getSet() {
			return set;
		}
		
		public int getTotalCount() {
			return totalCount;
		}
	}

}
