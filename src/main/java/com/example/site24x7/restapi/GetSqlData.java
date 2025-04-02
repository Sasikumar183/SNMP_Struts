package com.example.site24x7.restapi;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.json.JSONArray;
import org.json.JSONObject;
import com.example.site24x7.db.DatabaseConfig;
import com.example.site24x7.queries.RESTAPIQueries;
import com.example.site24x7.setter.SetDataSQL;

public class GetSqlData {

    public static JSONObject getData(StringBuilder query) {
        JSONObject jsonRes = new JSONObject();

        try {
            Connection con = DatabaseConfig.getConnection();
            PreparedStatement ps = con.prepareStatement(query.toString());
            ResultSet mysqlResultSet = ps.executeQuery();

            String statusQuery = RESTAPIQueries.statusQuery;

            PreparedStatement statusStmt = con.prepareStatement(statusQuery);
            ResultSet statusResultSet = statusStmt.executeQuery();
            JSONObject statusMap = new JSONObject();

            while (statusResultSet.next()) {
                JSONObject statusObj = new JSONObject();
                statusObj.put("oper_status", statusResultSet.getString("oper_status"));
                statusObj.put("admin_status", statusResultSet.getString("admin_status"));
                statusMap.put(String.valueOf(statusResultSet.getInt("id")), statusObj);
            }

            JSONArray dataArray = new JSONArray();
            while (mysqlResultSet.next()) {
                JSONObject record = SetDataSQL.setSqlData(mysqlResultSet, statusMap);
                
                dataArray.put(record);
            }

            ps.close();
            mysqlResultSet.close();
            statusStmt.close();
            statusResultSet.close();
            con.close();

            jsonRes.put("data", dataArray);

        } catch (SQLException e) {
            e.printStackTrace();
            jsonRes.put("error", "DB ERROR");
        }
        return jsonRes;
    }
}
