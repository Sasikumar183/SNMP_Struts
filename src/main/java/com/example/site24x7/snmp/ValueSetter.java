package com.example.site24x7.snmp;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;

import org.json.JSONObject;

public class ValueSetter {
	public static void store(Map<Integer, Long> previousInTraffic, Map<Integer, Long> previousOutTraffic,
			Map<Integer, Integer> previousInErrors, Map<Integer, Integer> previousOutErrors,
			Map<Integer, Integer> previousInDiscards, Map<Integer, Integer> previousOutDiscards, JSONObject obj,
			int interfaceId, PreparedStatement detailsStmt) throws SQLException {

		long inBytes = obj.optLong("inBytes", 0);
		long outBytes = obj.optLong("outBytes", 0);
		int inErrors = obj.optInt("inErrors", 0);
		int outErrors = obj.optInt("outErrors", 0);
		int inDiscards = obj.optInt("inDiscards", 0);
		int outDiscards = obj.optInt("outDiscards", 0);
		int adminStatus = obj.optInt("adminStatus", 0);
		int operationStatus = obj.optInt("operationStatus", 0);

		long prevInBytes = previousInTraffic.get(interfaceId);
		long prevOutBytes = previousOutTraffic.get(interfaceId);
		long deltaIn = (inBytes >= prevInBytes) ? (inBytes - prevInBytes) : inBytes;
		long deltaOut = (outBytes >= prevOutBytes) ? (outBytes - prevOutBytes) : outBytes;

		// Compute delta for errors
		int prevInErrors = previousInErrors.get(interfaceId);
		int prevOutErrors = previousOutErrors.get(interfaceId);
		int deltaInErrors = (inErrors >= prevInErrors) ? (inErrors - prevInErrors) : inErrors;
		int deltaOutErrors = (outErrors >= prevOutErrors) ? (outErrors - prevOutErrors) : outErrors;

		// Compute delta for discards
		int prevInDiscards = previousInDiscards.get(interfaceId);
		int prevOutDiscards = previousOutDiscards.get(interfaceId);
		int deltaInDiscards = (inDiscards >= prevInDiscards) ? (inDiscards - prevInDiscards) : inDiscards;
		int deltaOutDiscards = (outDiscards >= prevOutDiscards) ? (outDiscards - prevOutDiscards) : outDiscards;

		// Insert into `inter_details` table
		detailsStmt.setInt(1, interfaceId);
		detailsStmt.setLong(2, deltaIn);
		detailsStmt.setLong(3, deltaOut);
		detailsStmt.setInt(4, deltaInErrors);
		detailsStmt.setInt(5, deltaOutErrors);
		detailsStmt.setInt(6, deltaInDiscards);
		detailsStmt.setInt(7, deltaOutDiscards);
		detailsStmt.setInt(8, adminStatus);
		detailsStmt.setInt(9, operationStatus);
		detailsStmt.executeUpdate();
		// Update previous values
		previousInTraffic.put(interfaceId, inBytes);
		previousOutTraffic.put(interfaceId, outBytes);
		previousInErrors.put(interfaceId, inErrors);
		previousOutErrors.put(interfaceId, outErrors);
		previousInDiscards.put(interfaceId, inDiscards);
		previousOutDiscards.put(interfaceId, outDiscards);

	}
}
