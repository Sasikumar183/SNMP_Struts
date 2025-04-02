package com.example.site24x7.snmp;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import com.example.site24x7.db.DatabaseConfig;
import com.example.site24x7.queries.SNMPQueries;

public class RemoveData {
	public static void removeData() throws SQLException {
		String query = SNMPQueries.delelteQuery;

		Connection conn = DatabaseConfig.getConnection();
		Statement stmt = conn.createStatement();

		int rowsAffected = stmt.executeUpdate(query);
		System.out.println(rowsAffected + " rows deleted.");

		stmt.close();
		conn.close();
	}

}
