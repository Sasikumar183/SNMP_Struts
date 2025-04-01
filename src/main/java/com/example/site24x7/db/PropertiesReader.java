package com.example.site24x7.db;

import java.io.InputStream;
import java.util.Properties;


public class PropertiesReader {
	public String getProperty(String key) {
		Properties properties = new Properties();
		try (InputStream input = getClass().getClassLoader().getResourceAsStream("hostdetails.properties")) {
			if (input == null) {
				System.out.println("Sorry, unable to find config.properties");
				return null;
			}
			properties.load(input);
			return properties.getProperty(key);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static void main(String[] args) {
		PropertiesReader reader = new PropertiesReader();
		System.out.println(reader.getProperty("SNMP_VERSION"));
	}

}
