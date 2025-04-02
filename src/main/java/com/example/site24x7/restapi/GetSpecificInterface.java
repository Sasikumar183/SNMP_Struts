package com.example.site24x7.restapi;

import java.sql.Connection;
import java.sql.SQLException;
import org.json.JSONArray;
import org.json.JSONObject;
import com.example.site24x7.db.DatabaseConfig;
import com.example.site24x7.queries.RESTAPIQueries;
import com.example.site24x7.setter.SetDataSQL;

import java.sql.*;

public class GetSpecificInterface {
	public static int index;

	public static JSONObject getGeneralDetails(int id) throws SQLException {
		String query = RESTAPIQueries.getData;
		JSONObject json = new JSONObject();

		Connection con = DatabaseConfig.getConnection();
		java.sql.PreparedStatement ps = con.prepareStatement(query);
		ps.setInt(1, id);
		ResultSet rs = ps.executeQuery();
		if (rs.next()) {
			json.put("id", id);
			json.put("index", rs.getInt("idx"));
			index = rs.getInt("idx");
			json.put("name", rs.getString("interface_name"));
			json.put("ip", rs.getString("IP"));
		} else {
			json.put("error", "Id not there");
		}
		con.close();
		ps.close();
		rs.close();
		return json;
	}

	public static JSONObject getInsights(int id, String interval, String IP) throws SQLException {
		String query = "";

		int seconds = 0, hour = 0;
		if (interval.equals("1h")) {
			seconds = 600;
			hour = 1;
		} else if (interval.equals("6h")) {
			seconds = 1800;
			hour = 6;
		} else if (interval.equals("12h")) {
			seconds = 3600;
			hour = 12;
		} else if (interval.equals("1d")) {
			seconds = 3600;
			hour = 24;
		} else if (interval.equals("1w")) {
			seconds = 86400;
			hour = 168;
		} else if (interval.equals("30d")) {
			seconds = 172800;
			hour = 720;
		}

		query = RESTAPIQueries.Spquery;

		String Squery = RESTAPIQueries.Squery;
		JSONObject json = new JSONObject();

		Connection con = DatabaseConfig.getConnection();
		java.sql.PreparedStatement ps;
		if (interval.equals("6h") || interval.equals("1h")) {
			ps = con.prepareStatement(query);
		} else {
			ps = con.prepareStatement(Squery);

		}
		ps.setInt(1, seconds);
		ps.setInt(2, seconds);
		ps.setInt(3, hour);
		ps.setInt(4, id);
		ResultSet rs = ps.executeQuery();
		JSONArray jsonArr = new JSONArray();

		while (rs.next()) {
			JSONObject data = SetDataSQL.setData(rs); 

			jsonArr.put(data);
		}
		if (interval.equals("1h") || interval.equals("6h")) {
			json.put("data", OrderData.orderData(jsonArr, interval));

		} else if (interval.equals("1w") || interval.equals("30d") || interval.equals("12h") || interval.equals("1d")) {
			System.out.println(jsonArr);
			json.put("data",
					OrderData.orderData(
							JSONMerger.merge(jsonArr,
									CassandraSpecificInterface.getCassandraData(id, interval, IP, index), interval),
							interval));

		}
		con.close();
		ps.close();
		rs.close();
		return json;
	}

}
