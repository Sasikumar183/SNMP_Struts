package com.example.site24x7.snmp;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.BoundStatement;
import com.example.site24x7.db.DatabaseConfig;
import com.example.site24x7.queries.SNMPQueries;

public class InsertToCassandra {

	static void insertToCassandra() {
		CqlSession session = DatabaseConfig.getCassandraSession();
		if (session == null || session.isClosed()) {
			throw new IllegalStateException("Cassandra session is not initialized.");
		}
		System.out.println("Working");
		String sqlQuery = SNMPQueries.sqlQuery;
		String insertQuery = SNMPQueries.insertQuery;

		try (Connection conn = DatabaseConfig.getConnection();
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(sqlQuery)) {

			if (session.isClosed()) {
				throw new IllegalStateException("Cassandra session is not initialized.");
			}

			com.datastax.oss.driver.api.core.cql.PreparedStatement statement = session.prepare(insertQuery);

			while (rs.next()) {
				BoundStatement boundStatement = statement.bind(rs.getInt("primary_id"), rs.getInt("interface_idx"),
						rs.getString("hour_slot"), rs.getString("interface_ip"),

						rs.getDouble("avg_in_discard"), rs.getDouble("avg_in_error"), rs.getDouble("avg_in_traffic"),
						rs.getDouble("avg_out_discard"), rs.getDouble("avg_out_error"), rs.getDouble("avg_out_traffic"),

						rs.getDouble("max_in_discard"), rs.getDouble("max_in_error"), rs.getDouble("max_in_traffic"),
						rs.getDouble("max_out_discard"), rs.getDouble("max_out_error"), rs.getDouble("max_out_traffic"),

						rs.getDouble("min_in_discard"), rs.getDouble("min_in_error"), rs.getDouble("min_in_traffic"),
						rs.getDouble("min_out_discard"), rs.getDouble("min_out_error"), rs.getDouble("min_out_traffic"),

						rs.getDouble("sum_in_discard"), rs.getDouble("sum_in_error"), rs.getDouble("sum_out_discard"),
						rs.getDouble("sum_out_error"));

				session.execute(boundStatement);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
