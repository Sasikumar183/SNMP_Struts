package com.example.site24x7.restapi;

import com.example.site24x7.db.DatabaseConfig;
import com.example.site24x7.queries.RESTAPIQueries;

import org.json.JSONObject;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GetStatus {
    public static JSONObject getCurrentStatus(int id) throws SQLException {
        String query = RESTAPIQueries.status;
                
        JSONObject json = new JSONObject();

        Connection con = DatabaseConfig.getConnection();
        java.sql.PreparedStatement ps = con.prepareStatement(query);
        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            json.put("operation_status", rs.getInt("oper_status"));
            json.put("admin_status", rs.getInt("admin_status"));
        } else {
            json.put("error", "Id not there");
        }
        con.close();
        ps.close();
        rs.close();

        return json;
    }
}
