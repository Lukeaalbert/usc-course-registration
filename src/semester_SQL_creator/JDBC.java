package final_project_JDBC;
import java.sql.*;
import java.util.Scanner;
import java.util.Vector;

/*

Sample usage: 

	JDBC jdbc = new JDBC();

	jdbc.establishJDBCConnection(username, password);
	
	jdbc.populateSqlDb(new_database_name, list_of_course_class_instances);

 */

public class JDBC {
	private Connection connection;
	private Statement stmt;
	
	public void establishJDBCConnection(String username, String password) {
		try {
			String connection_url = "jdbc:mysql://localhost?user=" + username + "&password=" + password;
			connection = DriverManager.getConnection(connection_url);
			stmt = connection.createStatement();
		}
		catch(SQLException sqle) {
			System.out.println(sqle.getMessage());
		}
	}
	
	private void createDb(String databaseName) {
		try {
			// Create database if it doesn't exist
	        String sql = "CREATE DATABASE IF NOT EXISTS " + databaseName;
	        stmt.executeUpdate(sql);
	        System.out.println(databaseName + " created successfully");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void populateDb(Course[] courses) {
		try {
			String create_courses_table = "CREATE TABLE courses( "
					+ "id VARCHAR(255) PRIMARY KEY, "
					+ "course_name VARCHAR(255), "
					+ "course_title VARCHAR(255), "
					+ "course_description VARCHAR(255), "
					+ "units VARCHAR(255), "
					+ "type VARCHAR(255), "
					+ "day VARCHAR(255), "
					+ "time VARCHAR(255), "
					+ "loc VARCHAR(255), "
					+ "prof VARCHAR(255)) ";
			
			stmt.executeUpdate(create_courses_table);
			
			for (Course course: courses) {
				for (Section section: course.sections) {
					String sql = "INSERT INTO courses ( "
							+ "id, "
							+ "course_name, "
							+ "course_title, "
							+ "course_description, "
							+ "units, "
							+ "type, "
							+ "day, "
							+ "time, "
							+ "loc, "
							+ "prof) "
							+ "VALUES ("
							+ section.id + ", "
							+ course.course_name + ", "
							+ course.course_title + ", "
							+ course.course_description + ", "
							+ course.units + ", "
							+ section.type + ", "
							+ section.day + ", "
							+ section.time + ", "
							+ section.loc + ", "
							+ section.prof
							+ ")";
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public void populateSqlDb(String databaseName, Course[] courses) {
		createDb(databaseName);
		populateDb(courses);
	}
}
