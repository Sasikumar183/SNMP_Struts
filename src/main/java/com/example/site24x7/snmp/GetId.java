package com.example.site24x7.snmp;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import com.example.site24x7.db.DatabaseConfig;
import com.example.site24x7.queries.SNMPQueries;

public class GetId {
    public static Map<String, Integer> retrieveId() {

        String query = SNMPQueries.getId;
        Map<String, Integer> res = new HashMap<String, Integer>();

        Connection con;
        try {
            con = DatabaseConfig.getConnection();
            PreparedStatement ps = con.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String key = rs.getString("IP") + "-" + rs.getInt("idx");
                int value = rs.getInt("id");
                res.put(key, value);
            }
            con.close();

        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return res;
    }

}
