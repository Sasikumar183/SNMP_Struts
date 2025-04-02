package com.example.site24x7.restapi;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import javax.servlet.http.HttpServletRequest;

import com.example.site24x7.queries.RESTAPIQueries;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.json.JSONObject;

@SuppressWarnings("deprecation")
public class GetData extends ActionSupport implements ServletRequestAware {
	private static final long serialVersionUID = 1L;
	private InputStream input;
	private HttpServletRequest request;

	public String execute() throws NumberFormatException, SQLException {
		if (!"GET".equalsIgnoreCase(request.getMethod())) {
			input = new ByteArrayInputStream("Invalid Request Method".getBytes(StandardCharsets.UTF_8));
			return SUCCESS;
		}

		String time = request.getParameter("time");

		String ip = request.getParameter("ip");

		String interval = time.equals("1h") ? "1 HOUR"
				: time.equals("6h") ? "6 HOUR"
						: time.equals("12h") ? "12 HOUR"
								: time.equals("1d") ? "1 DAY"
										: time.equals("1w") ? "7 DAY" : time.equals("30d") ? "30 DAY" : "NA";

		// For Specific interface
		if (interval.equals("NA")) {
			input = new ByteArrayInputStream("Invalid time interval".getBytes(StandardCharsets.UTF_8));
			return SUCCESS;
		}

		String id = request.getParameter("id");
		if (id != null && !id.isEmpty()) {
			// System.out.println("Parameter contains the search string!");
			JSONObject generalDetails = new JSONObject();
			JSONObject general = GetSpecificInterface.getGeneralDetails(Integer.parseInt(id));
			JSONObject interfaceInsight = GetSpecificInterface.getInsights(Integer.parseInt(id), time, ip);
			JSONObject status = GetStatus.getCurrentStatus(Integer.parseInt(id));

			generalDetails.append("general", general);
			generalDetails.append("status", status);
			generalDetails.append("data", interfaceInsight);
			input = new ByteArrayInputStream(generalDetails.toString(4).getBytes(StandardCharsets.UTF_8));

		}

		// For dashboard
		else {
			StringBuilder query = RESTAPIQueries.GetDataquery(interval, ip);

			JSONObject jsonRes, jsonRes2, finalRes;
			try {
				jsonRes = GetSqlData.getData(query);

				if (!(time.equals("1h") || time.equals("6h"))) {
					jsonRes2 = GetCassandraData.getData(time, ip);
					finalRes = AggreageTwoDB.getAggregate(jsonRes.getJSONArray("data"), jsonRes2.getJSONArray("data"));
					String jsonString = finalRes.toString(4);
					input = new ByteArrayInputStream(jsonString.getBytes(StandardCharsets.UTF_8));
				} else {
					String jsonString = jsonRes.toString(4);
					input = new ByteArrayInputStream(jsonString.getBytes(StandardCharsets.UTF_8));
				}

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				input = new ByteArrayInputStream("Database error".getBytes(StandardCharsets.UTF_8));
			}
		}
		return SUCCESS;
	}

	public InputStream getInput() {
		return input;
	}

	@Override
	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}
}
