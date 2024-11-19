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
}