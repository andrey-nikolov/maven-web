package org.maven.rest;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class Database {
	
	private static String dbDriver = "com.mysql.jdbc.Driver";
	private static String dbUrl = "jdbc:mysql://localhost/maven_web";
	private static String dbUsername = "root";
	private static String dbPassword = "password";
	
	public static boolean checkUsername (String username) throws SQLException {
		
		String query = String.format("select distinct NAME from USER where NAME = '%s'", username);
		boolean match = false;
		
		try {
			Class.forName(dbDriver);
			Connection connection = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(query);
			match = resultSet.first();
			connection.close();
		} catch (ClassNotFoundException e) {
			throw new RuntimeException("Configuration error.");
		}

		return match;
	}
	
	public static boolean checkPassword (String username, String password) throws SQLException {
		
		String query = String.format("select distinct NAME from USER where NAME = '%s' and PASSWORD = '%s'", username, password);
		boolean match = false;
		
		try {
			Class.forName(dbDriver);
			Connection connection = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(query);
			match = resultSet.first();
			connection.close();
		} catch (ClassNotFoundException e) {
			throw new RuntimeException("Configuration error.");
		}
		
		return match;
	}
	
	public static List<User> getUsers (String gender) throws SQLException {

		String query;
		if (gender == null || gender.isEmpty())
			query = "select distinct NAME, GENDER, AGE from USER order by NAME";
		else
			query = String.format("select distinct NAME, GENDER, AGE from USER where GENDER = '%s' order by NAME", gender);

		List<User> users = new ArrayList<User>();
		
		try {
			Class.forName(dbDriver);
			Connection connection = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(query);			
			while (resultSet.next()) {
				User user = new User(resultSet.getString(1), resultSet.getString(2), Integer.parseInt(resultSet.getString(3)));
				users.add(user);
			};
			connection.close();
		} catch (ClassNotFoundException e) {
			throw new RuntimeException("Configuration error.");
		}
		
		return users;
	}
	
	public static String getDatabaseReport () {
		
		StringBuilder report = new StringBuilder("Checking database status...\n");
		report.append("  URL: " + dbUrl + "\n");
		report.append("  Driver: " + dbDriver + "\n");
		report.append("  Username: " + dbUsername + "\n");
		report.append("  Password: " + dbPassword + "\n");

		String query = String.format("select count(*) from USER");
		
		try {
			Class.forName(dbDriver);		
			Connection connection = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(query);
			report.append("  Status: Connection success.\n");
			if (resultSet.next())
				report.append("  Entries: " + resultSet.getString(1) + "\n");
			connection.close();
		} catch (ClassNotFoundException e) {
			report.append("  Status: Driver error.\n");
		} catch (SQLException e) {
			report.append("  Status: Connection error.\n");
		}
		
		return report.toString();
	}
}
