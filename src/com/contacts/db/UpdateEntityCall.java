package com.contacts.db;

import java.sql.*;

public class UpdateEntityCall extends DbCall {

	@Override
	protected void doCall(Statement statement, String sql) throws SQLException {
		statement.executeUpdate(sql);
	}
}
