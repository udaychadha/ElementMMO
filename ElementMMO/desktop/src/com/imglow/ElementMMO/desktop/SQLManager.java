package com.imglow.ElementMMO.desktop;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.locks.ReentrantLock;

import javax.swing.JOptionPane;

import com.imglow.ElementMMO.SQL;



public class SQLManager implements SQL {
	
	// VARIABLES START --------------------------------------------------------
	
	//public static final String DB_ADDRESS = "jdbc:mysql://localhost/";
	public String DB_ADDRESS = "jdbc:mysql://";
	public static final String DB_NAME = "elementmmo";
	public static final String DRIVER = "com.mysql.jdbc.Driver";
	public static final String USER = "user";
	public static final String PASSWORD = "1234";
	
	Connection myConnection;
	PreparedStatement myPS;
	
	//protected ReentrantLock queryLock;
	
	// VARIABLES END ----------------------------------------------------------
	
	
	
	public SQLManager (String host)
	{
		DB_ADDRESS += host + "/";
		//queryLock = new ReentrantLock();
		
		// establish connection with sql database
		try {
			Class.forName(DRIVER);
			myConnection = DriverManager.getConnection(DB_ADDRESS + DB_NAME, USER, PASSWORD);
		}
		catch (SQLException sqle) { System.out.println("SQLException in SQL Manager Constructor: " + sqle.getMessage()); }
		catch (LinkageError le) { System.out.println("LinkerError in SQL Manager Constructor: " + le.getMessage()); }
		catch (ClassNotFoundException  cnfe) { System.out.println("ClassNotFoundException in SQL Manager Constructor: " + cnfe.getMessage()); }
		catch (Exception e) { System.out.println("Exception in SQL Manager Constructor: " + e.getMessage()); }
	}

	
	
	// Get whether user exists
	public boolean userExists (String user)
	{
		try {
			Statement myStatement = myConnection.createStatement();
			//ResultSet myResults = myStatement.executeQuery("SELECT 1 FROM users WHERE users.username = " + user);
			ResultSet myResults = myStatement.executeQuery("SELECT DISTINCT * FROM users WHERE username IN('" + user + "')");
			if (myResults.next()) return true;
		}
		catch (SQLException se) { System.out.println("ERROR in userExists: " + se.getMessage()); }
		return false;
	}
	
	
	
	// Get whether login credentials are valid
	public boolean isValidLogin (String user, String pw)
	{
		// if specified user doesn't exist, abort
		if (!userExists(user)) return false;
		
		// get result and compare typed password with stored password
		try {
			ResultSet myResults = getUserEntry(user);
			myResults.next();
			String actualPW = myResults.getString("password");
			if (actualPW.equals(pw)) return true;
		}
		catch (SQLException se) { System.out.println("ERROR in isValidLogin: " + se.getMessage()); }
		return false;
	}
	
	
	
	// Get user entry
	public ResultSet getUserEntry (String user)
	{
		// abort if user does not exist
		if (!userExists(user)) return null;
		
		try {
			Statement myStatement = myConnection.createStatement();
			//return myStatement.executeQuery("SELECT 1 FROM users WHERE users.username = " + user);
			return myStatement.executeQuery("SELECT DISTINCT * FROM users WHERE username IN('" + user + "')");
		}
		catch (SQLException se) { System.out.println("ERROR in getUserEntry: " + se.getMessage()); }
		return null;
	}
	
	
	
	// Enter new user
	public void createUser (String user, String pw, int charID)
	{
		// abort if user already exists
		if (userExists(user)) return;
		
		// enter new user into database
		try {
			myPS = myConnection.prepareStatement("INSERT INTO users (username, password, char_id, killcount, deathcount) VALUES (?, ?, ?, ?, ?)");
			myPS.setString(1, user);
			myPS.setString(2, pw);
			myPS.setInt(3, charID);
			myPS.setInt(4, 0);
			myPS.setInt(5, 0);
			myPS.execute();
		}
		catch (SQLException se) { System.out.println("ERROR in createUser: " + se.getMessage()); }
	}
	
	
	
	// Get number of kills of specified user
	public int getKillCount (String user)
	{
		try {
			ResultSet myResults = getUserEntry(user);
			if (myResults.next()) {
				return myResults.getInt("killcount");
			}
		}
		catch (SQLException se) { System.out.println("ERROR in addKill: " + se.getMessage()); }
		return 0;
	}
	
	
	
	// Increment kill count of specified user
	public void addKill (String user)
	{
		try {
			int currentKills = getKillCount(user);
			currentKills++;
			myPS = myConnection.prepareStatement("UPDATE users SET killcount='" + currentKills + "' WHERE username='" + user + "'");
		}
		catch (SQLException se) { System.out.println("ERROR in addKill: " + se.getMessage()); }
	}
	
	
	
	// Get number of deaths of specified user
	public int getDeathCount (String user)
	{
		try {
			ResultSet myResults = getUserEntry(user);
			if (myResults.next()) {
				return myResults.getInt("deathcount");
			}
		}
		catch (SQLException se) { System.out.println("ERROR in addKill: " + se.getMessage()); }
		return 0;
	}
	
	
	
	// Increment death count of specified user
	public void addDeath (String user)
	{
		try {
			int currentDeaths = getDeathCount(user);
			currentDeaths++;
			myPS = myConnection.prepareStatement("UPDATE users SET deathcount='" + currentDeaths + "' WHERE username='" + user + "'");
		}
		catch (SQLException se) { System.out.println("ERROR in addDeath: " + se.getMessage()); }
	}
	
	
	
	// Inform user they have been disconnected
	//	- creates message popup
	public void disconnected ()
	{
		// you have been disconnected from the server
		JOptionPane.showMessageDialog(null, "You have been disconnected from the server.", "Disconnected", JOptionPane.INFORMATION_MESSAGE);
	}
	
}




















