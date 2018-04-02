package com.contacts.dao.db.call;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Logger;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

/**
 * Abstract class specifying usage interface and implementing DB access. 
 */
public abstract class DbCall {
	
	protected static final Logger LOGGER = Logger.getLogger(DbCall.class.getName());
	
	/** The data source. */
	private static DataSource dataSource;

	private static final String DATA_SOURCE_NAME = "jdbc/db";
	
	/**
	 * Instantiates a new instance and loads the reference to the data source.
	 */
	public DbCall() {
		try {
			dataSource = (DataSource) new InitialContext().lookup("java:comp/env/" + DATA_SOURCE_NAME);
		} catch (NamingException e) {
			throw new IllegalStateException(DATA_SOURCE_NAME + " is missing in JNDI!", e);
		}
	}
	
	/**
	 * Prepares Statement instance for the SQL query execution, initiates actual query and closes it once completed
	 *
	 * @param sql the SQL query to execute
	 * @throws Exception if execution fails
	 */
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
	
	/**
	 * Actual SQL query execution using provided Statement.
	 *
	 * @param statement the statement to execute the querey
	 * @param sql the SQL query
	 * @throws Exception if execution fails
	 */
	protected abstract void doCall(Statement statement, String sql) throws Exception;
}
