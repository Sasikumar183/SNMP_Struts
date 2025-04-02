package com.example.site24x7.restapi;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.PreparedStatement;
import com.datastax.oss.driver.api.core.cql.ResultSet;
import com.datastax.oss.driver.api.core.cql.Row;
import com.example.site24x7.db.DatabaseConfig;
import com.example.site24x7.queries.RESTAPIQueries;
import com.example.site24x7.setter.setDataCassandra;

import org.json.JSONArray;
import org.json.JSONObject;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GetCassandraData {

    public static JSONObject getData(String interval, String ip) {
        JSONObject res = new JSONObject();
        CqlSession session = DatabaseConfig.getCassandraSession();
        try {
            String CqueryPID = RESTAPIQueries.CqueryPID;

            List<Integer> arr = new ArrayList<Integer>();
            PreparedStatement preparedStatement = session.prepare(CqueryPID);
            ResultSet resultSet = session.execute(preparedStatement.bind(ip));
            for (Row row : resultSet) {
                arr.add(row.getInt("interface_idx"));
            }
            Collections.sort(arr);
            System.out.println(arr);

            String placeholders = String.join(",", Collections.nCopies(arr.size(), "?"));

            String Cquery = RESTAPIQueries.Cquery(placeholders);
            String inter = getTimestamp(interval);
            preparedStatement = session.prepare(Cquery);
            JSONArray resultArray = new JSONArray();

            // Bind values separately
            List<Object> values = new ArrayList<>();
            values.add(ip);
            values.addAll(arr); // Add all interface_idx values separately
            values.add(inter);

            // Execute the query
            resultSet = session.execute(preparedStatement.bind(values.toArray()));

            // Process results
            for (Row row : resultSet) {
                JSONObject avgObj = setDataCassandra.setCassanData(row);
                resultArray.put(avgObj);
            }
            res.put("data", resultArray);

        } catch (Exception e) {
            e.printStackTrace();
            res.put("error", "db error");
        }
        return res;
    }

    public static String getTimestamp(String time) {

        LocalDateTime now = LocalDateTime.now(ZoneOffset.UTC);
        LocalDateTime resultTime = time.equals("12h") ? now.minusHours(12) : time.equals("1d") ? now.minusDays(1) : time.equals("1w") ? now.minusDays(7) : time.equals("30d") ? now.minusDays(30) : now;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return resultTime.format(formatter);
    }

}