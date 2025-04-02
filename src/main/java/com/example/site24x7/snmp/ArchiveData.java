package com.example.site24x7.snmp;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalTime;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import com.datastax.oss.driver.api.core.CqlSession;
import com.example.site24x7.db.DatabaseInitializer;
import com.example.site24x7.db.DatabaseConfig;

@WebListener
public class ArchiveData implements ServletContextListener {
	private CqlSession session;
	private ScheduledExecutorService scheduler;

	@Override
	public void contextInitialized(ServletContextEvent event) {
		DatabaseInitializer.initializeDatabase();
		try {
			session = DatabaseConfig.getCassandraSession();
			if (session == null || session.isClosed()) {
				throw new IllegalStateException("Cassandra session is not initialized.");
			}
			scheduler = Executors.newScheduledThreadPool(1);

			scheduler.scheduleAtFixedRate(this::insertDummyData, calculateDelayUntil(13, 15), 24 * 60 * 60,
					TimeUnit.SECONDS);

			scheduler.scheduleAtFixedRate(() -> {
				try {
					RemoveData.removeData();
				} catch (SQLException e) {
					e.printStackTrace();
				} catch (Exception e) {
					System.err.println("Unexpected error in removeData: " + e.getMessage());
				}
			}, calculateDelayUntil(15, 59), 24 * 60 * 60, TimeUnit.SECONDS);

			scheduler.scheduleAtFixedRate(() -> {
				try {
					StoreData.fetchData();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}, 0, 1, TimeUnit.MINUTES);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static long calculateDelayUntil(int targetHour, int targetMinute) {
		LocalTime now = LocalTime.now();
		LocalTime targetTime = LocalTime.of(targetHour, targetMinute);

		if (now.isAfter(targetTime)) {
			return 24 * 60 * 60;
		}
		return java.time.Duration.between(now, targetTime).getSeconds();
	}

	private void insertDummyData() {
		System.out.println("Working");
		InsertToCassandra.insertToCassandra();
	}

	@Override
	public void contextDestroyed(ServletContextEvent event) {
		System.out.println("ServletContextListener destroyed. Closing Cassandra session.");
		if (session != null && !session.isClosed()) {
			session.close();
		}
	}

}
