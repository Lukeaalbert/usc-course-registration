

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Vector;

/**
 * Servlet implementation class AlgorithmServlet
 */
@WebServlet("/AlgorithmServlet")
public class AlgorithmServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       

    public AlgorithmServlet() {
        super();

    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		response.getWriter().append("Served at: ").append(request.getContextPath());
	}
	
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

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		PrintWriter pw = response.getWriter(); 
		
		response.setContentType("application/json"); 
		response.setCharacterEncoding("UTF-8");
		loadProperties();
		JDBC.establishJDBCConnection(dbProperties.getProperty("username"), dbProperties.getProperty("password"),
        		dbProperties.getProperty("dbname"));
		
		String[] sectionIDs = new Gson().fromJson(request.getReader(), String[].class);
//		for(String i: sectionIDs) {
//			System.out.println(i);
//		}
		ArrayList<Section> allSections = new ArrayList<>();
		for(String str: sectionIDs) {
			Vector<String> section = JDBC.getCourse(str);
			Section c = new Section(section.get(0), section.get(1), section.get(2), "R", "0", section.get(3).split("-")[0], section.get(3).split("-")[1], section.get(4));
			allSections.add(c);
		}
		
		Map<String, ArrayList<Section>> sectionsByName = new HashMap<>();

		for (Section section : allSections) {
		    String name = section.getName();
		    
		    // If the name doesn't exist in the map, create a new ArrayList
		    if (!sectionsByName.containsKey(name)) {
		        sectionsByName.put(name, new ArrayList<>());
		    }
		    
		    // Add the section to the corresponding ArrayList
		    sectionsByName.get(name).add(section);
		}
		
		List<Course> wantedClassesSync = Collections.synchronizedList(new ArrayList<Course>());
		ArrayList<Course> wantedClasses = new ArrayList<Course>(wantedClassesSync);
		
		for(Entry<String, ArrayList<Section>> e: sectionsByName.entrySet()) {
			Course c = new Course(e.getKey(), "", "", e.getValue());
			wantedClasses.add(c);
		}
		
		System.out.println(wantedClasses);
		
		Algorithm algorithm = new Algorithm(wantedClasses); 
		// System.out.println(wantedClasses.size());
		algorithm.compute(0); 
		
		
		ArrayList<Section> res = algorithm.getRes(); 
		
//		System.out.println("finished " + res.size());
//		
//		for (int j = 0; j < res.size(); j++) {
//			System.out.println(res.get(j).getType());
//		}
//		
		
		Gson gson = new Gson();
		
		if (res.size() != algorithm.getMinSecionsRequired()) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			
			String message = "Cannot find valid schedule"; 
			pw.write(gson.toJson(message));
			pw.flush();
		}
		
		else {
			response.setStatus(HttpServletResponse.SC_OK);
			System.out.println(gson.toJson(res));
			pw.write(gson.toJson(res)); 
			pw.flush(); 
		}

	}

}
