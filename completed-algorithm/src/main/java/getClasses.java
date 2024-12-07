import javax.servlet.ServletException;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.lang.Class;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.Properties;

@WebServlet("/getClasses")
public class getClasses extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private Properties dbProperties;
    
    public getClasses() {
        super();
    }
    
    private JSONArray getClasesFromSearch(String user_search) {
        Connection connection = null;
        Statement stmt = null;
        ResultSet rs = null;
        JSONArray searchHistory = new JSONArray();
        
        try {
            // ***CONNECT TO SQL***
        	Class.forName("com.mysql.cj.jdbc.Driver");
            
            String connection_url = "jdbc:mysql://localhost:3306/?user={username}&password={password}!&useSSL=false&allowPublicKeyRetrieval=true";
            
            connection = DriverManager.getConnection(
                    dbProperties.getProperty("url"),
                    dbProperties.getProperty("username"),
                    dbProperties.getProperty("password")
                );
            
            stmt = connection.createStatement();
            
            // ***USE fall2024***
            String sql = "USE " + dbProperties.getProperty("dbname");
            stmt.executeUpdate(sql);
            
            // ***GET USERNAME***
            PreparedStatement pstmt = connection.prepareStatement("SELECT * FROM courses WHERE LOWER(course_name) LIKE LOWER(?)");
    		pstmt.setString(1, user_search);
    		
			rs = pstmt.executeQuery();
			
			boolean resultFound = false;
			
			int classCount = 1;
			while (rs.next()) {
			    resultFound = true;

			    int columnCount = rs.getMetaData().getColumnCount();

			    String data[] = new String[columnCount];

			    for (int i = 1; i <= columnCount; i++) {
			        data[i - 1] = rs.getString(i);
			    }

			    String dataAsString = Arrays.toString(data);

			    System.out.println("ADDING: " + dataAsString);

			    JSONObject searchEntry = new JSONObject();
			    searchEntry.put("r" + classCount, new JSONArray(Arrays.asList(data)));
			    classCount++;
			    searchHistory.put(searchEntry);
			}

		    if (!resultFound) {
		    	JSONObject searchEntry = new JSONObject();
                searchEntry.put("result", "No Classes Found.");
                searchHistory.put(searchEntry);
		    }
			
            return searchHistory;
            
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    
    private void loadProperties() throws ServletException {
        dbProperties = new Properties();
        try {
            String filePath = getServletContext().getRealPath("/WEB-INF/database_properties.txt");
            File file = new File(filePath);

            try (FileInputStream fis = new FileInputStream(file)) {
                dbProperties.load(fis);
            }
        } catch (Exception e) {
            throw new ServletException("Error loading database properties", e);
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        loadProperties();
        
        // TODO: check that user_search isn't empty and is correctly formatted
        // on front end.
        String user_search = request.getParameter("user_search");
        
        JSONArray classMatches = getClasesFromSearch(user_search);
        
        if (classMatches == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"error\": \"Error retrieving classes.\"}");
            return;
        } else {
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().write(classMatches.toString());
        }
    }
}