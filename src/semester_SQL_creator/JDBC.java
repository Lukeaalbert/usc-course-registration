package semester_SQL_creator;
import java.sql.*;
import java.util.Vector;
import java.util.HashSet;

public class JDBC {
    private static Connection connection;
    private static Statement stmt;

    public static void establishJDBCConnection(String username, String password) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            String connection_url = "jdbc:mysql://localhost:3306/?user=" + username + 
                                 "&password=" + password +
                                 "&useSSL=false&allowPublicKeyRetrieval=true";

            connection = DriverManager.getConnection(connection_url);
            stmt = connection.createStatement();

            System.out.println("Database connection established successfully!");

        } catch (ClassNotFoundException e) {
            System.out.println("MySQL JDBC Driver not found.");
            e.printStackTrace();
        } catch (SQLException sqle) {
            System.out.println("SQL Error: " + sqle.getMessage());
            sqle.printStackTrace();
        }
    }

    private static void createDb(String databaseName) {
        try {
            // Drop the database if it exists and create a new one
            String sql = "DROP DATABASE IF EXISTS " + databaseName;
            stmt.executeUpdate(sql);
            sql = "CREATE DATABASE " + databaseName;
            stmt.executeUpdate(sql);
            
            // Select the database
            sql = "USE " + databaseName;
            stmt.executeUpdate(sql);
            
            System.out.println(databaseName + " created and selected successfully");
        } catch (SQLException e) {
            System.out.println("Error creating/selecting database: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void populateDb(Vector<Vector<Course>> courses) {
        try {
            String sql = "CREATE TABLE courses ("
                    + "id VARCHAR(255) PRIMARY KEY, "
                    + "course_name VARCHAR(255), "
                    + "course_title VARCHAR(255), "
                    + "course_description TEXT, "
                    + "units VARCHAR(255), "
                    + "type VARCHAR(255), "
                    + "day VARCHAR(255), "
                    + "time VARCHAR(255), "
                    + "loc VARCHAR(255), "
                    + "prof VARCHAR(255)) ";

            stmt.executeUpdate(sql);

			// For managing already seen sections
            HashSet<String> processedSections = new HashSet<>();
            
            PreparedStatement pstmt = connection.prepareStatement(
                "INSERT INTO courses (id, course_name, course_title, course_description, " +
                "units, type, day, time, loc, prof) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");

            for (Vector<Course> dep : courses) {
                for (Course course : dep) {
                    for (Section section : course.sections) {

                        // Skip if we've already processed this section
                        if (!processedSections.add(section.id_and_d_class_code)) {
                            System.out.println("Skipped duplicate section: " + section.id_and_d_class_code + 
                                             " for course: " + course.name);
                            continue;
                        }

                        try {
                            pstmt.setString(1, section.id_and_d_class_code);
                            pstmt.setString(2, course.name);
                            pstmt.setString(3, course.title);
                            pstmt.setString(4, course.description);
                            pstmt.setString(5, course.units);
                            pstmt.setString(6, section.type);
                            pstmt.setString(7, section.day);
                            pstmt.setString(8, section.time);
                            pstmt.setString(9, section.loc);
                            pstmt.setString(10, section.prof_name);
                            
                            pstmt.executeUpdate();
							
                        } catch (SQLException e) {
                            System.out.println("Error inserting course: " + course.name + 
                                             " section: " + section.id_and_d_class_code);
                            e.printStackTrace();
                        }
                    }
                }
            }
            pstmt.close();
        } catch (SQLException e) {
            System.out.println("Error creating table or preparing statement: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void populateSqlDb(String databaseName, Vector<Vector<Course>> courses) {
        createDb(databaseName);
        populateDb(courses);
    }

    // insert a user into database, assume users is a table with (id INT PRIMARY KEY, username VARCHAR(100), email VARCHAR(100), password VARCHAR(100))
    public static boolean insertUser(String username, String email, String password){
        try{
        	if(username.length() >= 100 || email.length() >= 100 || password.length() >= 100)
        		return false;
        	
        	if(getUser(username).size() > 1) // a user exists
        		return false;
        	
            PreparedStatement getMaxID = connection.prepareStatement("SELECT MAX(user_id) AS m FROM users;");
            ResultSet rs = getMaxID.executeQuery();
            int max_idx = 1;
            // get max index and add one for new id
            // if result set empty, meaning table is none, then let index be 1
            if(rs.next())
            	max_idx = rs.getInt("m") + 1;
            
            PreparedStatement insertIntoBase = connection.prepareStatement("INSERT INTO users(user_id, username, email, password) VALUES (?, ?, ?, ?);");
            insertIntoBase.setInt(1, max_idx);
            insertIntoBase.setString(2, username);
            insertIntoBase.setString(3, email);
            insertIntoBase.setString(4, password);
            insertIntoBase.execute();
            return true;
        } catch(SQLException sqle){
            System.out.println("SQL Error: " + sqle.getMessage());
            sqle.printStackTrace();
        }
        return false;
    }
    
    // get information for user providing username
    // if exists: "id", "username", "email", "password"
    // if an error occurs, result a vector of single String
    public static Vector<String> getUser(String username){
    	Vector<String> results = new Vector<>();
    	if(username.length() >= 100) {
    		results.add("user doesn't exist!");
    		return results;
    	}
    	try {
    		PreparedStatement getUserByUsername = connection.prepareStatement("SELECT * FROM users WHERE username = ?;");
    		getUserByUsername.setString(1, username);
    		ResultSet rs = getUserByUsername.executeQuery();
        	if(!rs.next()) {
        		results.add("user doesn't exist!");
        	} else {
        		results.add("" + rs.getInt("user_id"));
        		results.add(rs.getString("username"));
        		results.add(rs.getString("email"));
        		results.add(rs.getString("password"));
        	}
        	return results;
    	} catch(Exception e) {
    		System.out.println("Exception: " + e.getMessage());
            e.printStackTrace();
    	}
    	results.add("an unknown error happened!");
		return results;
    }
    
    // overload: get user by id
    public static Vector<String> getUser(int id){
    	Vector<String> results = new Vector<>();
    	try {
    		PreparedStatement getUserByUsername = connection.prepareStatement("SELECT * FROM users WHERE user_id = ?;");
    		getUserByUsername.setInt(1, id);
    		ResultSet rs = getUserByUsername.executeQuery();
        	if(!rs.next()) {
        		results.add("user doesn't exist!");
        	} else {
        		results.add("" + rs.getInt("user_id"));
        		results.add(rs.getString("username"));
        		results.add(rs.getString("email"));
        		results.add(rs.getString("password"));
        	}
        	return results;
    	} catch(Exception e) {
    		System.out.println("Exception: " + e.getMessage());
            e.printStackTrace();
    	}
    	results.add("an unknown error happened!");
		return results;
    }
    
    // return whether delete is successful
    public static boolean dropUser(String username) {
    	try {
    		PreparedStatement getUserByUsername = connection.prepareStatement("DELETE FROM users WHERE username = ?;");
    		getUserByUsername.setString(1, username);
    		getUserByUsername.execute();
        	return true;
    	} catch(Exception e) {
    		System.out.println("Exception: " + e.getMessage());
            e.printStackTrace();
    	}
    	
		return false;
    }
    
    // overload, delete by id
    public static boolean dropUser(int id) {
    	try {
    		PreparedStatement getUserByUsername = connection.prepareStatement("DELETE FROM users WHERE user_id = ?;");
    		getUserByUsername.setInt(1, id);
    		getUserByUsername.execute();
        	return true;
    	} catch(Exception e) {
    		System.out.println("Exception: " + e.getMessage());
            e.printStackTrace();
    	}
    	
		return false;
    }
}