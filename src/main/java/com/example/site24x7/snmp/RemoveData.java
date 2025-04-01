package com.example.site24x7.snmp;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import com.example.site24x7.db.DatabaseConfig;

public class RemoveData {
	public static void removeData() throws SQLException {
		String query = "DELETE FROM inter_details WHERE collected_time >= CONCAT(DATE_SUB(CURDATE(), INTERVAL 1 DAY), ' 10:00:00') AND collected_time < CONCAT(CURDATE(), ' 10:00:00');";

		Connection conn = DatabaseConfig.getConnection();
		Statement stmt = conn.createStatement();

		int rowsAffected = stmt.executeUpdate(query);
		System.out.println(rowsAffected + " rows deleted.");

		stmt.close();
		conn.close();
	}

}
