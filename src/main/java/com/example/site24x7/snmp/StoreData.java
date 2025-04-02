package com.example.site24x7.snmp;

import java.sql.*;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;
import com.example.site24x7.db.DatabaseConfig;
import com.example.site24x7.queries.SNMPQueries;

public class StoreData {
	static Map<Integer, Long> previousInTraffic = new HashMap<>();
	static Map<Integer, Long> previousOutTraffic = new HashMap<>();
	static Map<Integer, Integer> previousInErrors = new HashMap<>();
	static Map<Integer, Integer> previousOutErrors = new HashMap<>();
	static Map<Integer, Integer> previousInDiscards = new HashMap<>();
	static Map<Integer, Integer> previousOutDiscards = new HashMap<>();
	static Map<String, Integer> primeIdMap = new HashMap<>();

	public static void fetchData() throws IOException {

		JSONArray dataArray = GetData.SNMPData();
		try (Connection con = DatabaseConfig.getConnection()) {
			if (con == null || con.isClosed()) {
				System.err.println("Error: Database connection is null or closed.");
				return;
			}
			Map<String, Integer> deleteCheck = CheckDeletion.isDeleted();
			String interfaceQuery = SNMPQueries.interfaceQuery;

			String detailsQuery = SNMPQueries.detailsQuery;

			primeIdMap = GetId.retrieveId();
			try (PreparedStatement interfaceStmt = con.prepareStatement(interfaceQuery);
					PreparedStatement detailsStmt = con.prepareStatement(detailsQuery)) {
				for (int i = 0; i < dataArray.length(); i++) {
					JSONObject obj = dataArray.getJSONObject(i);

					int idx = obj.optInt("Interface ID", -1);
					if (idx == -1) {
						continue;
					}
					String interfaceName = obj.optString("Interface Name", "Unknown");
					String IP = obj.optString("IP", "0.0.0.0");
					if (deleteCheck.get(IP + "-" + idx) == 1) {
						System.out.println("Skipped the collection for " + IP + " Index " + idx);
						continue;
					}
					long inBytes = obj.optLong("inBytes", 0);
					long outBytes = obj.optLong("outBytes", 0);
					int inErrors = obj.optInt("inErrors", 0);
					int outErrors = obj.optInt("outErrors", 0);
					int inDiscards = obj.optInt("inDiscards", 0);
					int outDiscards = obj.optInt("outDiscards", 0);
					

				

					interfaceStmt.setInt(1, idx);
					interfaceStmt.setString(2, interfaceName);
					interfaceStmt.setString(3, IP);
					interfaceStmt.setInt(4, idx);
					interfaceStmt.setString(5, IP);
					int rows = interfaceStmt.executeUpdate();
					if (rows != 0) {
						primeIdMap = GetId.retrieveId();
						System.out.println("New interface added");

					}
					int interfaceId = primeIdMap.getOrDefault(IP + "-" + idx, -1);
					if (interfaceId == -1) {
						throw new SQLException("Failed to retrieve interface ID.");
					}

					if (!previousInTraffic.containsKey(interfaceId) || !previousOutTraffic.containsKey(interfaceId)
							|| !previousInErrors.containsKey(interfaceId) || !previousOutErrors.containsKey(interfaceId)
							|| !previousInDiscards.containsKey(interfaceId)
							|| !previousOutDiscards.containsKey(interfaceId)) {

						previousInTraffic.put(interfaceId, inBytes);
						previousOutTraffic.put(interfaceId, outBytes);
						previousInErrors.put(interfaceId, inErrors);
						previousOutErrors.put(interfaceId, outErrors);
						previousInDiscards.put(interfaceId, inDiscards);
						previousOutDiscards.put(interfaceId, outDiscards);
					} else {
						ValueSetter.store(previousInTraffic, previousOutTraffic, previousInErrors, previousOutErrors,
								previousInDiscards, previousOutDiscards, obj, interfaceId, detailsStmt);

					}
				}
			}

		} catch (SQLException e) {
			System.err.println("Database error: " + e.getMessage());
			e.printStackTrace();
		}

	}
}
