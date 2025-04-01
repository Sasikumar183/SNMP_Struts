package com.example.site24x7.snmp;

import java.io.IOException;

import org.snmp4j.CommunityTarget;
import org.snmp4j.PDU;
import org.snmp4j.Snmp;
import org.snmp4j.event.ResponseEvent;
import org.snmp4j.smi.Address;
import org.snmp4j.smi.GenericAddress;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.VariableBinding;

import com.example.site24x7.db.PropertiesReader;

public class GetSnmpValue {
	@SuppressWarnings("unchecked")

	public static String getSnmpValue(Snmp snmp, String oid, String ip) {
		PropertiesReader properties = new PropertiesReader();
		String SNMP_COMMUNITY = properties.getProperty("SNMP_COMMUNITY");
		int SNMP_VERSION = Integer.parseInt(properties.getProperty("SNMP_VERSION"));
		Address targetAddress = GenericAddress.parse("udp:" + ip + "/161");
		@SuppressWarnings("rawtypes")
		CommunityTarget target = new CommunityTarget();
		target.setCommunity(new OctetString(SNMP_COMMUNITY));
		target.setAddress(targetAddress);
		target.setRetries(2);
		target.setTimeout(1500);
		target.setVersion(SNMP_VERSION);

		PDU pdu = new PDU();
		pdu.add(new VariableBinding(new OID(oid)));
		pdu.setType(PDU.GET);

		try {
			@SuppressWarnings({ "rawtypes" })
			ResponseEvent response = snmp.send(pdu, target);
			if (response == null || response.getResponse() == null) {
				return "N/A";
			}
			return response.getResponse().get(0).getVariable().toString();
		} catch (IOException e) {
			return "N/A";
		}
	}
}
