package com.example.site24x7.queries;

public class RESTAPIQueries {

	// cassandra specific
	public static String query = """
			 SELECT primary_id, hour_slot, avg_in_discard, avg_in_error, avg_in_traffic,
			       avg_out_discard, avg_out_error, avg_out_traffic, max_in_discard, max_in_error,
			       max_in_traffic, max_out_discard, max_out_error, max_out_traffic, min_in_discard,
			       min_in_error, min_in_traffic, min_out_discard, min_out_error, min_out_traffic,
			       sum_in_discard, sum_in_error, sum_out_discard, sum_out_error
			FROM snmp_interface_traffic
			WHERE interface_ip = ? AND interface_idx = ? AND hour_slot >= ?;
			""";

	// Delete interface
	public static String cqlDeleteQuery = "DELETE FROM snmp_interface_traffic WHERE interface_ip=? and interface_idx=?;";

	// Fetch Interface
	public static String fetchInterface = "SELECT idx,interface_name from interface where IP = ?;";

	// Cassandra Data
	public static String CqueryPID = """
			SELECT
				interface_idx
			FROM snmp_interface_traffic
			WHERE interface_ip= ?
			GROUP BY interface_ip,interface_idx;
			""";

	public static String Cquery(String placeholders) {
		String cquery = """
				SELECT
				    primary_id,
				    interface_ip,
				    interface_idx,
				    AVG(avg_in_traffic) AS avg_in_traffic,
				    AVG(avg_out_traffic) AS avg_out_traffic,
				    AVG(avg_in_error) AS avg_in_error,
				    AVG(avg_out_error) AS avg_out_error,
				    AVG(avg_in_discard) AS avg_in_discard,
				    AVG(avg_out_discard) AS avg_out_discard
				FROM snmp_interface_traffic
				WHERE interface_ip = ?
				      AND interface_idx IN (%s)
				      AND hour_slot >= ?
				GROUP BY interface_ip, interface_idx;
				""".formatted(placeholders);

		return cquery;
	}

	// GetData

	public static StringBuilder GetDataquery(String interval, String ip) {
		StringBuilder GetData = new StringBuilder("""
				SELECT
				    inter_details.id,
				    interface.idx,
				    interface.interface_name,
				    interface.IP,
				    AVG(in_traffic) AS avg_in_traffic,
				    AVG(out_traffic) AS avg_out_traffic,
				    AVG(in_error) AS avg_in_error,
				    AVG(out_error) AS avg_out_error,
				    AVG(in_discard) AS avg_in_discard,
				    AVG(out_discard) AS avg_out_discard
				FROM inter_details
				JOIN interface ON inter_details.id = interface.id
				WHERE collected_time >= NOW() - INTERVAL %s
				      AND interface.IP = '%s'
				GROUP BY inter_details.id, interface.idx, interface.interface_name, interface.IP;
				""".formatted(interval, ip));

		return GetData;

	}

	// GetIp
	public static String GETIP = "SELECT DISTINCT IP FROM interface";

	// GETSPECIFIC

	public static String getData = "SELECT * FROM interface WHERE id = ?";

	public static String Spquery = """
					SELECT
				    id,
				    FROM_UNIXTIME(FLOOR(UNIX_TIMESTAMP(collected_time) / ?) * ?) AS time_slot,

				    -- Traffic Statistics
				    AVG(in_traffic) AS avg_in_traffic,
				    MAX(in_traffic) AS max_in_traffic,
				    MIN(in_traffic) AS min_in_traffic,

				    AVG(out_traffic) AS avg_out_traffic,
				    MAX(out_traffic) AS max_out_traffic,
				    MIN(out_traffic) AS min_out_traffic,

				    -- Error Statistics
				    AVG(in_error) AS avg_in_error,
				    MAX(in_error) AS max_in_error,
				    MIN(in_error) AS min_in_error,
				    SUM(in_error) AS count_in_error,

				    AVG(out_error) AS avg_out_error,
				    MAX(out_error) AS max_out_error,
				    MIN(out_error) AS min_out_error,
				    SUM(out_error) AS count_out_error,

				    -- Discard Statistics
				    AVG(in_discard) AS avg_in_discard,
				    MAX(in_discard) AS max_in_discard,
				    MIN(in_discard) AS min_in_discard,
				    SUM(in_discard) AS count_in_discard,

				    AVG(out_discard) AS avg_out_discard,
				    MAX(out_discard) AS max_out_discard,
				    MIN(out_discard) AS min_out_discard,
				    SUM(out_discard) AS count_out_discard
			FROM inter_details
			WHERE  collected_time >= NOW() - INTERVAL ? HOUR
			AND id = ?
			GROUP BY id, time_slot
			ORDER BY id, time_slot;
			""";

	public static String Squery = """
						SELECT
						    id,
						    DATE_FORMAT(FROM_UNIXTIME(FLOOR(UNIX_TIMESTAMP(collected_time) / ?) * ?), '%Y-%m-%d %H:00:00') AS time_slot,

						    -- Traffic Statistics
						    AVG(in_traffic) AS avg_in_traffic,
						    MAX(in_traffic) AS max_in_traffic,
						    MIN(in_traffic) AS min_in_traffic,

						    AVG(out_traffic) AS avg_out_traffic,
						    MAX(out_traffic) AS max_out_traffic,
						    MIN(out_traffic) AS min_out_traffic,

						    -- Error Statistics
						    AVG(in_error) AS avg_in_error,
						    MAX(in_error) AS max_in_error,
						    MIN(in_error) AS min_in_error,
						    SUM(in_error) AS count_in_error,

						    AVG(out_error) AS avg_out_error,
						    MAX(out_error) AS max_out_error,
						    MIN(out_error) AS min_out_error,
						    SUM(out_error) AS count_out_error,

						    -- Discard Statistics
						    AVG(in_discard) AS avg_in_discard,
						    MAX(in_discard) AS max_in_discard,
						    MIN(in_discard) AS min_in_discard,
						    SUM(in_discard) AS count_in_discard,

						    AVG(out_discard) AS avg_out_discard,
						    MAX(out_discard) AS max_out_discard,
						    MIN(out_discard) AS min_out_discard,
						    SUM(out_discard) AS count_out_discard

						FROM inter_details
						WHERE collected_time >= NOW() - INTERVAL ? HOUR
						AND id = ?
						GROUP BY id, time_slot
						ORDER BY id, time_slot;
			""";

	// Get sql data
	public static String statusQuery = """
			    SELECT id, oper_status, admin_status
			    FROM (
			        SELECT id, oper_status, admin_status,
			               ROW_NUMBER() OVER (PARTITION BY id ORDER BY collected_time DESC) AS rn
			        FROM inter_details
			    ) t
			    WHERE rn = 1;
			""";

	// Status

	public static String status = statusQuery + " AND id = ?;";

	// Rename
	public static String rename = "UPDATE interface SET interface_name = ? WHERE id = ?";

}
