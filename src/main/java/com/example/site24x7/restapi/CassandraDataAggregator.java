package com.example.site24x7.restapi;

import org.json.JSONArray;
import org.json.JSONObject;

import com.example.site24x7.setter.CreateEmptyRecord;

import java.util.*;
import java.time.LocalDate;

public class CassandraDataAggregator {

	public static JSONArray getAggregated(JSONArray jsonArray, String interval) {
		return processAggregation(jsonArray, interval);
	}

	public static JSONArray processAggregation(JSONArray jsonArray, String interval) {
		Map<String, JSONObject> aggregatedData = new HashMap<>();

		for (int i = 0; i < jsonArray.length(); i++) {
			JSONObject record = jsonArray.getJSONObject(i);
			String timestamp = record.getString("hour_slot");
			LocalDate date = LocalDate.parse(timestamp.split(" ")[0]);

			String key;
			if (interval.equals("1w") || interval.equals("30d")) {
				key = date.toString();
			} else {
				continue;
			}

			aggregatedData.putIfAbsent(key, CreateEmptyRecord.createEmptyRecord(key));

			JSONObject aggregated = aggregatedData.get(key);
			aggregateValues(aggregated, record);
		}

		return new JSONArray(aggregatedData.values());
	}

	

    private static void aggregateValues(JSONObject aggregated, JSONObject record) {
        aggregated.put("count_in_discard", aggregated.getInt("sum_in_discard") + record.getInt("count_in_discard"));
        aggregated.put("min_in_discard", Math.min(aggregated.getInt("min_in_discard"), record.getInt("min_in_discard")));
        aggregated.put("count_in_error", aggregated.getInt("sum_in_error") + record.getInt("count_in_error"));
        aggregated.put("min_out_discard", Math.min(aggregated.getInt("min_out_discard"), record.getInt("min_out_discard")));
        aggregated.put("max_out_traffic", Math.max(aggregated.getInt("max_out_traffic"), record.getInt("max_out_traffic")));
        aggregated.put("avg_in_traffic", (aggregated.getInt("avg_in_traffic") + record.getInt("avg_in_traffic")) / 2);
        aggregated.put("max_in_discard", Math.max(aggregated.getInt("max_in_discard"), record.getInt("max_in_discard")));
        aggregated.put("min_out_traffic", Math.min(aggregated.getInt("min_out_traffic"), record.getInt("min_out_traffic")));
        aggregated.put("count_out_error", aggregated.getInt("sum_out_error") + record.getInt("count_out_error"));
        aggregated.put("min_out_error", Math.min(aggregated.getInt("min_out_error"), record.getInt("min_out_error")));
        aggregated.put("min_in_traffic", Math.min(aggregated.getInt("min_in_traffic"), record.getInt("min_in_traffic")));
        aggregated.put("max_out_discard", Math.max(aggregated.getInt("max_out_discard"), record.getInt("max_out_discard")));
        aggregated.put("max_out_error", Math.max(aggregated.getInt("max_out_error"), record.getInt("max_out_error")));
        aggregated.put("avg_in_discard", (aggregated.getInt("sum_in_discard") + record.getInt("count_in_discard")) / 2);
        aggregated.put("avg_out_discard", (aggregated.getInt("sum_out_discard") + record.getInt("count_out_discard")) / 2);
        aggregated.put("avg_in_error", (aggregated.getInt("sum_in_error") + record.getInt("count_in_error")) / 2);
        aggregated.put("max_in_error", Math.max(aggregated.getInt("max_in_error"), record.getInt("max_in_error")));
        aggregated.put("avg_out_traffic", (aggregated.getInt("avg_out_traffic") + record.getInt("avg_out_traffic")) / 2);
        aggregated.put("max_in_traffic", Math.max(aggregated.getInt("max_in_traffic"), record.getInt("max_in_traffic")));
        aggregated.put("interface_id", record.getInt("interface_id"));
        aggregated.put("min_in_error", Math.min(aggregated.getInt("min_in_error"), record.getInt("min_in_error")));
        aggregated.put("count_out_discard", aggregated.getInt("sum_out_discard") + record.getInt("count_out_discard"));
        aggregated.put("avg_out_error", (aggregated.getInt("sum_out_error") + record.getInt("count_out_error")) / 2);
    }
}