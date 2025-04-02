package com.example.site24x7.restapi;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.ResultSet;
import com.datastax.oss.driver.api.core.cql.Row;
import com.example.site24x7.db.DatabaseConfig;
import com.example.site24x7.queries.RESTAPIQueries;
import com.example.site24x7.setter.setDataCassandra;

import java.time.Instant;

import org.json.JSONArray;
import org.json.JSONObject;

public class CassandraSpecificInterface {

	public static JSONArray getCassandraData(int id, String interval, String IP, int index) {
//		System.out.println(index +"  "+IP);
		Instant now = Instant.now();
		Instant inter;
		if (interval.equals("12h")) {
			inter = now.minusSeconds(12 * 3600);
		} else if (interval.equals("1d")) {
			inter = now.minusSeconds(24 * 3600);
		} else if (interval.equals("1w")) {
			inter = now.minusSeconds(7 * 24 * 3600);
		} else if (interval.equals("30d")) {
			inter = now.minusSeconds(30 * 24 * 3600);
		} else {
			return new JSONArray();
		}

		String query = RESTAPIQueries.query;

		// Execute query
		String timestampStr = inter.toString();

		CqlSession session = DatabaseConfig.getCassandraSession();

		ResultSet resultSet = session.execute(session.prepare(query).bind(IP, index, timestampStr));

		JSONArray jsonArray = new JSONArray();
		for (Row row : resultSet) {
			JSONObject jsonObject = setDataCassandra.setCassanSpecificData(row);
			jsonArray.put(jsonObject);
		}

		if (interval.equals("30d") || interval.equals("1w")) {
			jsonArray = CassandraDataAggregator.getAggregated(jsonArray, interval);
		}
		return jsonArray;
	}

}
