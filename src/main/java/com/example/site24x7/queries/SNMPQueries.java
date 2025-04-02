package com.example.site24x7.queries;

public class SNMPQueries {

	public static String checkDeletionCheck = "SELECT IP, idx, isDeleted FROM interface;";

	public static String checkDeletionUpdate = "UPDATE interface set isDeleted = 1 WHERE IP = ? AND idx = ?;";

	public static String getId = "SELECT id,IP,idx from interface;";

	public static String sqlQuery = """
			    SELECT
			        interface.id AS primary_id,
			        interface.idx AS interface_idx,
			        DATE_FORMAT(collected_time, '%Y-%m-%d %H:00:00') AS hour_slot,
			        interface.IP AS interface_ip,

			        MAX(in_traffic) AS max_in_traffic,
			        MIN(in_traffic) AS min_in_traffic,
			        AVG(in_traffic) AS avg_in_traffic,

			        MAX(out_traffic) AS max_out_traffic,
			        MIN(out_traffic) AS min_out_traffic,
			        AVG(out_traffic) AS avg_out_traffic,

			        MAX(in_error) AS max_in_error,
			        MIN(in_error) AS min_in_error,
			        AVG(in_error) AS avg_in_error,
			        SUM(in_error) AS sum_in_error,

			        MAX(out_error) AS max_out_error,
			        MIN(out_error) AS min_out_error,
			        AVG(out_error) AS avg_out_error,
			        SUM(out_error) AS sum_out_error,

			        MAX(in_discard) AS max_in_discard,
			        MIN(in_discard) AS min_in_discard,
			        AVG(in_discard) AS avg_in_discard,
			        SUM(in_discard) AS sum_in_discard,

			        MAX(out_discard) AS max_out_discard,
			        MIN(out_discard) AS min_out_discard,
			        AVG(out_discard) AS avg_out_discard,
			        SUM(out_discard) AS sum_out_discard

			    FROM inter_details
			    JOIN interface ON inter_details.id = interface.id
			    GROUP BY inter_details.id, hour_slot, interface.IP
			    ORDER BY inter_details.id, hour_slot;
			""";

	public static String insertQuery = """
			INSERT INTO snmp_interface_traffic (
				primary_id, interface_idx, hour_slot, interface_ip,
				avg_in_discard, avg_in_error, avg_in_traffic,
				avg_out_discard, avg_out_error, avg_out_traffic,
				max_in_discard, max_in_error, max_in_traffic,
				max_out_discard, max_out_error, max_out_traffic,
				min_in_discard, min_in_error, min_in_traffic,
				min_out_discard, min_out_error, min_out_traffic,
				sum_in_discard, sum_in_error, sum_out_discard, sum_out_error
				)
			VALUES (
				?, ?, ?, ?, ?,
				?, ?, ?,
				?, ?, ?,
				?, ?, ?,
				?, ?, ?,
				?, ?, ?,
				?, ?, ?,
				?, ?, ?
				);
			""";

	public static String delelteQuery = "DELETE FROM inter_details WHERE collected_time >= CONCAT(DATE_SUB(CURDATE(), INTERVAL 1 DAY), ' 10:00:00') AND collected_time < CONCAT(CURDATE(), ' 10:00:00');";

	public static String interfaceQuery = """
			      		INSERT INTO interface (idx, interface_name, IP)
			            	SELECT ?, ?, ?
			            	FROM DUAL
			            	WHERE NOT EXISTS (
			            	    SELECT 1 FROM interface WHERE idx = ? AND IP = ?
			            );
			""";

	public static String detailsQuery = "INSERT INTO inter_details (id, in_traffic, out_traffic, in_error, out_error, in_discard, out_discard, admin_status, oper_status, collected_time) "
			+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, NOW())";
}
