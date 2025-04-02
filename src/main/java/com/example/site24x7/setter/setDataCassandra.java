package com.example.site24x7.setter;

import org.json.JSONObject;

import com.datastax.oss.driver.api.core.cql.Row;

public class setDataCassandra {
	public static JSONObject setCassanData(Row row) {
		JSONObject avgObj = new JSONObject();
		avgObj.put("interface_id", row.getInt("primary_id"));
		avgObj.put("avg_in_traffic", row.getDouble("avg_in_traffic"));
		avgObj.put("avg_out_traffic", row.getDouble("avg_out_traffic"));
		avgObj.put("avg_in_error", row.getDouble("avg_in_error"));
		avgObj.put("avg_out_error", row.getDouble("avg_out_error"));
		avgObj.put("avg_in_discard", row.getDouble("avg_in_discard"));
		avgObj.put("avg_out_discard", row.getDouble("avg_out_discard"));
		return avgObj;
	}
	
	public static JSONObject setCassanSpecificData(Row row) {
		JSONObject jsonObject = new JSONObject();

		jsonObject.put("interface_id", row.getInt("primary_id"));
		jsonObject.put("hour_slot", row.getString("hour_slot"));
		jsonObject.put("avg_in_discard", row.getDouble("avg_in_discard"));
		jsonObject.put("avg_in_error", row.getDouble("avg_in_error"));
		jsonObject.put("avg_in_traffic", row.getDouble("avg_in_traffic"));

		jsonObject.put("avg_out_discard", row.getDouble("avg_out_discard"));
		jsonObject.put("avg_out_error", row.getDouble("avg_out_error"));
		jsonObject.put("avg_out_traffic", row.getDouble("avg_out_traffic"));

		jsonObject.put("max_in_discard", row.getDouble("max_in_discard"));
		jsonObject.put("max_in_error", row.getDouble("max_in_error"));
		jsonObject.put("max_in_traffic", row.getDouble("max_in_traffic"));

		jsonObject.put("max_out_discard", row.getDouble("max_out_discard"));
		jsonObject.put("max_out_error", row.getDouble("max_out_error"));
		jsonObject.put("max_out_traffic", row.getDouble("max_out_traffic"));

		jsonObject.put("min_in_discard", row.getDouble("min_in_discard"));
		jsonObject.put("min_in_error", row.getDouble("min_in_error"));
		jsonObject.put("min_in_traffic", row.getDouble("min_in_traffic"));

		jsonObject.put("min_out_discard", row.getDouble("min_out_discard"));
		jsonObject.put("min_out_error", row.getDouble("min_out_error"));
		jsonObject.put("min_out_traffic", row.getDouble("min_out_traffic"));

		jsonObject.put("count_in_discard", row.getDouble("sum_in_discard"));
		jsonObject.put("count_in_error", row.getDouble("sum_in_error"));

		jsonObject.put("count_out_discard", row.getDouble("sum_out_discard"));
		jsonObject.put("count_out_error", row.getDouble("sum_out_error"));
		return jsonObject;
	}
}
