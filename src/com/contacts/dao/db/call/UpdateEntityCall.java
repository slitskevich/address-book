package com.contacts.dao.db.call;

import java.sql.*;

/**
 * Utility to execute update SQL queries.
 */
public class UpdateEntityCall extends DbCall {

	/* (non-Javadoc)
	 * @see com.contacts.dao.db.call.DbCall#doCall(java.sql.Statement, java.lang.String)
	 */
	@Override
	protected void doCall(Statement statement, String sql) throws SQLException {
		statement.executeUpdate(sql);
	}
}
