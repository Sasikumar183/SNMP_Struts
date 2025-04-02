package com.example.site24x7.setter;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.json.JSONException;
import org.json.JSONObject;


public class SetDataSQL {
	public static JSONObject setData(ResultSet rs) throws JSONException, SQLException {
		JSONObject data = new JSONObject();
		data.put("time_slot", rs.getString("time_slot"));
		data.put("avg_in_traffic", rs.getDouble("avg_in_traffic"));
		data.put("max_in_traffic", rs.getDouble("max_in_traffic"));
		data.put("min_in_traffic", rs.getDouble("min_in_traffic"));
		data.put("avg_out_traffic", rs.getDouble("avg_out_traffic"));
		data.put("max_out_traffic", rs.getDouble("max_out_traffic"));
		data.put("min_out_traffic", rs.getDouble("min_out_traffic"));
		data.put("avg_in_error", rs.getDouble("avg_in_error"));
		data.put("max_in_error", rs.getDouble("max_in_error"));
		data.put("min_in_error", rs.getDouble("min_in_error"));
		data.put("count_in_error", rs.getInt("count_in_error"));
		data.put("avg_out_error", rs.getDouble("avg_out_error"));
		data.put("max_out_error", rs.getDouble("max_out_error"));
		data.put("min_out_error", rs.getDouble("min_out_error"));
		data.put("count_out_error", rs.getInt("count_out_error"));
		data.put("avg_in_discard", rs.getDouble("avg_in_discard"));
		data.put("max_in_discard", rs.getDouble("max_in_discard"));
		data.put("min_in_discard", rs.getDouble("min_in_discard"));
		data.put("count_in_discard", rs.getInt("count_in_discard"));
		data.put("avg_out_discard", rs.getDouble("avg_out_discard"));
		data.put("max_out_discard", rs.getDouble("max_out_discard"));
		data.put("min_out_discard", rs.getDouble("min_out_discard"));
		data.put("count_out_discard", rs.getInt("count_out_discard"));
		return data;
	}

	public static JSONObject setSqlData(ResultSet mysqlResultSet, JSONObject statusMap)
			throws JSONException, SQLException {
		JSONObject record = new JSONObject();
		int interfaceId = mysqlResultSet.getInt("id");

		record.put("interface_id", interfaceId);
		record.put("index", mysqlResultSet.getInt("idx"));
		record.put("interface_name", mysqlResultSet.getString("interface_name"));
		record.put("interface_ip", mysqlResultSet.getString("IP"));
		record.put("avg_in_traffic", mysqlResultSet.getDouble("avg_in_traffic"));
		record.put("avg_out_traffic", mysqlResultSet.getDouble("avg_out_traffic"));
		record.put("avg_in_error", mysqlResultSet.getDouble("avg_in_error"));
		record.put("avg_out_error", mysqlResultSet.getDouble("avg_out_error"));
		record.put("avg_in_discard", mysqlResultSet.getDouble("avg_in_discard"));
		record.put("avg_out_discard", mysqlResultSet.getDouble("avg_out_discard"));

		if (statusMap.has(String.valueOf(interfaceId))) {
			JSONObject statusData = statusMap.getJSONObject(String.valueOf(interfaceId));
			record.put("oper_status", statusData.getString("oper_status"));
			record.put("admin_status", statusData.getString("admin_status"));
		} else {
			record.put("oper_status", JSONObject.NULL);
			record.put("admin_status", JSONObject.NULL);
		}
		return record;
	}

}
