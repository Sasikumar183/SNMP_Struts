package com.example.site24x7.setter;

import org.json.JSONObject;

public class CreateEmptyRecord {
	public static JSONObject createEmptyRecord(String key) {
		JSONObject obj = new JSONObject();
		obj.put("date", key);
		obj.put("sum_in_discard", 0);
		obj.put("min_in_discard", Integer.MAX_VALUE);
		obj.put("sum_in_error", 0);
		obj.put("min_out_discard", Integer.MAX_VALUE);
		obj.put("max_out_traffic", 0);
		obj.put("avg_in_traffic", 0);
		obj.put("max_in_discard", 0);
		obj.put("min_out_traffic", Integer.MAX_VALUE);
		obj.put("sum_out_error", 0);
		obj.put("min_out_error", Integer.MAX_VALUE);
		obj.put("min_in_traffic", Integer.MAX_VALUE);
		obj.put("max_out_discard", 0);
		obj.put("max_out_error", 0);
		obj.put("avg_in_discard", 0);
		obj.put("avg_out_discard", 0);
		obj.put("avg_in_error", 0);
		obj.put("max_in_error", 0);
		obj.put("avg_out_traffic", 0);
		obj.put("max_in_traffic", 0);
		obj.put("interface_id", -1);
		obj.put("min_in_error", Integer.MAX_VALUE);
		obj.put("sum_out_discard", 0);
		obj.put("avg_out_error", 0);
		return obj;
	}
}
