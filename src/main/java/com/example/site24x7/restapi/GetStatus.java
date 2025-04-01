package com.example.site24x7.restapi;

import com.example.site24x7.db.DatabaseConfig;
import org.json.JSONObject;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GetStatus {
    public static JSONObject getCurrentStatus(int id) throws SQLException {
        String query = """
                    SELECT id, oper_status, admin_status
                    FROM (
                        SELECT id, oper_status, admin_status, 
                               ROW_NUMBER() OVER (PARTITION BY id ORDER BY collected_time DESC) AS rn
                        FROM inter_details
                    ) t
                    WHERE rn = 1 AND id = ?;
                """;
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
