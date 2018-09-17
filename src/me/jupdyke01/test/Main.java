package me.jupdyke01.test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Main {

	Connection myConn;
	Statement mySt;
	PreparedStatement myStmt;


	public Main() {
		try {	
			myConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/game", "root", "");
			mySt = myConn.createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		
		
		//createAccount("jupdyke01", "jupdyke01@hotmail.com");
		removeAccount("jupdyke01");
		try {
			myConn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public boolean accountExists(String username) {
		try {
			myStmt = myConn.prepareStatement("select * from users where username =?");
			myStmt.setString(1, username);
			ResultSet rs = myStmt.executeQuery();
			if (rs.next()) {
				return true;
			} else {
				return false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	/*public boolean emailVerified(String username) {
		if (accountExists(username)) {
			
		}
	}*/

	public void verifyEmail(String username) {
		// Somehow verify emails
		if (accountExists(username)) {
			try {
				myStmt = myConn.prepareStatement("update users set email_verification='1' where username=?");
				myStmt.setString(1, username);
				myStmt.executeUpdate();
				System.out.println("Email Verified!");
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else {
			// No account by that name exists
		}
	}

	public void createAccount(String username, String email) {
		try {			
			Date dt = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String currentTime = sdf.format(dt);

			ResultSet myRs = mySt.executeQuery("select * from users");
			while(myRs.next()) {
				if (myRs.getString("username").equals(username)) {
					System.out.println("Already an account with the username: " + username + "!");
					return;
				} else if (myRs.getString("email").equals(email)) {
					System.out.println("Already an account with the email: " + email + "!");
					return;
				}
			}

			PreparedStatement myStmt = myConn.prepareStatement("insert into users (username, email, sign_up_date)" + " values (?, ?, ?)");
			myStmt.setString(1, username);
			myStmt.setString(2, email);
			myStmt.setString(3, currentTime);
			myStmt.executeUpdate();

			System.out.println("Your account has been successfuly created with the current information: ");
			System.out.println("   Username: " + username);
			System.out.println("   Email: " + email);			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void removeAccount(String username) {
		if (accountExists(username)) {
			try {
				PreparedStatement myStmt = myConn.prepareStatement("delete from users where username=?");
				myStmt.setString(1, username);
				myStmt.executeUpdate();
				System.out.println("Account removed with the username: " + username);
			} catch(SQLException e) {
				e.printStackTrace();
			}
		} else {
			System.out.println("No account with the username: " + username);
		}
	}

	public static void main(String[] args) {
		new Main();
	}

}
