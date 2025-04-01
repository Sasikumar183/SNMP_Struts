package com.example.site24x7.snmp;
import javax.servlet.http.HttpServlet;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;
import org.snmp4j.*;
import org.snmp4j.transport.DefaultUdpTransportMapping;

import com.example.site24x7.db.PropertiesReader;

public class GetData extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public static JSONArray SNMPData() throws IOException {
		PropertiesReader properties = new PropertiesReader();
		String SNMP_HOST = properties.getProperty("SNMP_HOST");
		Snmp snmp = new Snmp(new DefaultUdpTransportMapping());
		snmp.listen();
		JSONArray data = new JSONArray();
		String ipList[] = SNMP_HOST.split(",");

		for (String ip : ipList) {
			String totalOID = "1.3.6.1.2.1.2.1.0";
			String totalInterfacesStr = GetSnmpValue.getSnmpValue(snmp, totalOID, ip);

			if (totalInterfacesStr.equals("N/A")) {
				System.out.println("Failed to fetch total interfaces.");
				snmp.close();
				return data;
			}

			int totalInterfaces = Integer.parseInt(totalInterfacesStr);
			List<String> oids = Arrays.asList("1.3.6.1.2.1.2.2.1.1", // Interface ID (ifIndex)
					"1.3.6.1.2.1.2.2.1.2", // Interface Name (ifDescr)
					"1.3.6.1.2.1.2.2.1.10", // Bytes In
					"1.3.6.1.2.1.2.2.1.16", // Bytes Out
					"1.3.6.1.2.1.2.2.1.14", // In Errors
					"1.3.6.1.2.1.2.2.1.20", // Out Errors
					"1.3.6.1.2.1.2.2.1.7", // Admin Status
					"1.3.6.1.2.1.2.2.1.8", // Oper Status
					"1.3.6.1.2.1.2.2.1.13", // Inbound Discards
					"1.3.6.1.2.1.2.2.1.19" // Outbound Discards
			);

			for (int i = 1; i <= totalInterfaces - 1; i++) {
				String interfaceId = GetSnmpValue.getSnmpValue(snmp, oids.get(0) + "." + i, ip); // Get Interface Index
				String interfaceName = GetSnmpValue.getSnmpValue(snmp, oids.get(1) + "." + i, ip);

				String inBytes = GetSnmpValue.getSnmpValue(snmp, oids.get(2) + "." + i, ip);
				String outBytes = GetSnmpValue.getSnmpValue(snmp, oids.get(3) + "." + i, ip);
				String inErrors = GetSnmpValue.getSnmpValue(snmp, oids.get(4) + "." + i, ip);
				String outErrors = GetSnmpValue.getSnmpValue(snmp, oids.get(5) + "." + i, ip);
				String adminStatus = GetSnmpValue.getSnmpValue(snmp, oids.get(6) + "." + i, ip);
				String operStatus = GetSnmpValue.getSnmpValue(snmp, oids.get(7) + "." + i, ip);
				String inDiscards = GetSnmpValue.getSnmpValue(snmp, oids.get(8) + "." + i, ip);
				String outDiscards = GetSnmpValue.getSnmpValue(snmp, oids.get(9) + "." + i, ip);

				JSONObject dataobj = new JSONObject();
				dataobj.put("Interface ID", interfaceId);
				dataobj.put("IP", ip);
				dataobj.put("Interface Name", interfaceName);
				dataobj.put("inBytes", inBytes);
				dataobj.put("outBytes", outBytes);
				dataobj.put("inErrors", inErrors);
				dataobj.put("outErrors", outErrors);
				dataobj.put("adminStatus", adminStatus);
				dataobj.put("operationStatus", operStatus);
				dataobj.put("inDiscards", inDiscards);
				dataobj.put("outDiscards", outDiscards);

				data.put(dataobj);
			}
		}
		System.out.println(data.toString(4));
		snmp.close();
		return data;
	}
}
