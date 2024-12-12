import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Properties;
import java.util.Vector;

import com.google.gson.Gson;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet{
	private static final long serialVersionUID = 2L;
	private Properties dbProperties;
	
	private void loadProperties() throws ServletException {
        dbProperties = new Properties();
        try {
            String filePath = getServletContext().getRealPath("/WEB-INF/database_properties.txt");
            File file = new File(filePath);
            System.out.println(filePath);

            try (FileInputStream fis = new FileInputStream(file)) {
                dbProperties.load(fis);
            }
        } catch (Exception e) {
            throw new ServletException("Error loading database properties", e);
        }
    }
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException{
		response.setContentType("application/json");
		loadProperties();
		
		BufferedReader reader = request.getReader();
		StringBuilder sb = new StringBuilder();
		PrintWriter out = response.getWriter();
		
		String line;
		while((line = reader.readLine()) != null){
			sb.append(line);
		}
		
		Gson gson = new Gson();

        // Parse the JSON string into a User object
        User user = gson.fromJson(sb.toString(), User.class);
        String username = user.getUsername();
        String email = user.getEmail();
        String password = user.getPassword();
        
        Authenticator auth = new Authenticator(dbProperties.getProperty("username"), dbProperties.getProperty("password"),
        		dbProperties.getProperty("dbname"));
        
        out.println(gson.toJson(new Message(auth.register(username, email, password), auth.user_id)));
	}
}
