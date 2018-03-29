package com.contacts.dao.db.call;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Logger;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public abstract class DbCall {
	
	protected static final Logger LOGGER = Logger.getLogger(DbCall.class.getName());
	
	private static DataSource dataSource;

	private static final String DATA_SOURCE_NAME = "jdbc/db";
	
	public DbCall() {
		try {
			dataSource = (DataSource) new InitialContext().lookup("java:comp/env/" + DATA_SOURCE_NAME);
		} catch (NamingException e) {
			throw new IllegalStateException(DATA_SOURCE_NAME + " is missing in JNDI!", e);
		}
	}
	
	public void execute(String sql) throws Exception {
		LOGGER.info("DB call: " + sql);
		Connection connection = null;
		Statement statement = null;
		try {
			connection = dataSource.getConnection();
			statement = connection.createStatement();
			
			doCall(statement, sql);

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
	}
	
	protected abstract void doCall(Statement statement, String sql) throws Exception;
}
