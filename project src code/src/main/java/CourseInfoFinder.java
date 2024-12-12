import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;

// Compile: javac -cp "semester_SQL_creator/lib/*" semester_SQL_creator/CourseInfoFinder.java
// Run:
// Windows: java -cp ".;semester_SQL_creator/lib/*" semester_SQL_creator.CourseInfoFinder
// Mac/Linux: java -cp ".:semester_SQL_creator/lib/*" semester_SQL_creator.CourseInfoFinder

public class CourseInfoFinder {
	
	public static Map<String, Course> nameToCourse = Collections.synchronizedMap(new HashMap<String, Course>());
	
    public static String[] departments = new String[]{"AHIS", "ALI", "AMST", "ANTH", "ARAB", "ASTR", "BISC", "CHEM", "CLAS", "COLT", "CORE", "CSLC",
               "EALC", "EASC", "ECON", "ENGL", "ENST", "FREN", "GEOG", "GEOL", "GERM", "SWMS", "GR", "HEBR",
               "HIST", "HBIO", "INDS", "IR", "IRAN", "ITAL", "JS", "LAT", "LING", "MATH", "MDA", "MDES", "MPW",
               "NEUR", "NSCI", "OS", "PHED", "PHIL", "PHYS", "POIR", "PORT", "POSC", "PSYC", "REL", "RNR",
               "SLL", "SOCI", "SPAN", "SSCI", "SSEM", "USC", "VISS", "WRIT", "ACCT", "ARCH", "ACAD", "ACCT",
               "BAEP", "BUAD", "BUCO", "DSO", "FBE", "GSBA", "MKT", "MOR", "HRM", "CMPP", "CNTV", "CTAN",
               "CTCS", "CTIN", "CTPR", "CTWR", "IML", "ASCJ", "CMGT", "COMM", "DSM", "JOUR", "PR", "PUBD",
               "DANC", "DENT", "CBY", "DHIS", "THTR", "EDCO", "EDHP", "EDUC", "AME", "ASTE", "BME", "CHE", "CE",
               "CSCI", "EE", "ENE", "ENGR", "ISE", "INF", "ITP", "MASC", "PTE", "SAE", "ART", "CRIT", "DES",
               "FA", "FACE", "FACS", "FADN", "FADW", "FAIN", "FAPH", "FAPT", "FAPR", "FASC", "WCT", "GCT",
               "SCIN", "SCIS", "ARLT", "SI", "ARTS", "HINQ", "SANA", "LIFE", "PSC", "QREA", "GPG", "GPH",
               "GESM", "GERO", "LAW", "ACMD", "ANST", "BIOC", "CBG", "DSR", "HP", "INTD", "MEDB", "MEDS",
               "MICB", "MPHY", "MSS", "NIIN", "PATH", "PHBI", "PM", "PCPA", "SCRM", "ARTL", "MTEC", "MSCR",
               "MTAL", "MUCM", "MUCO", "MUCD", "MUEN", "MUHL", "MUIN", "MUJZ", "MPEM", "MPGU", "MPKS", "MPPM",
               "MPST", "MPVA", "MPWP", "MUSC", "SCOR", "OT", "HCDA", "PHRD", "PMEP", "RXRS", "BKN", "PT",
               "AEST", "HMGT", "MS", "NAUT", "NSC", "PPD", "PPDE", "PLUS", "RED"};

    // Note: 20243 = Fall 2024
    public static String semester = "20243";

    public static String getDepartmentJsonString(String department_id, String semester_id) throws Exception{
        try {
            String url_text = "https://web-app.usc.edu/web/soc/api/classes/" + department_id + "/" + semester_id;
            
            // Create a HttpClient
            HttpClient client = HttpClient.newHttpClient();
            
            // Create a HttpRequest
            HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url_text)).build();
            
            // Send the request and get the response
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            // System.out.println(url_text);
             System.out.println(response.body());
            return response.body();
            
            
        } catch (IOException | InterruptedException e) {
            throw e;
        }

    }
    public static ArrayList<Course> createClassMapping(String json_response){
    	// System.out.println(json_response);
        List<Course> coursesList =  Collections.synchronizedList(new ArrayList<Course>());
        ArrayList<Course> courses = new ArrayList<Course>(coursesList);
        
        // Map<String, Course> nameToCourse = Collections.synchronizedMap(new HashMap<String, Course>());

        // Parse the JSON string into a JSONObject
        JSONObject jsonObject = new JSONObject(json_response);
        
        JSONArray allCourses = (jsonObject.getJSONObject("OfferedCourses")).getJSONArray("course");
        for(int i = 0; i < allCourses.length(); i++){
            // Units has an edge case 1.0-8.0 units for some courses - deal with later
            // String units = course_data.getString("units"); 
            
            JSONObject courseData = allCourses.getJSONObject(i).getJSONObject("CourseData");
            String prefix = null; 
            String number = null; 
            String name = null;
            String title = null; 
            String description = null; 
            
            List<Section> sectionsList = Collections.synchronizedList(new ArrayList<Section>());
            ArrayList<Section> sections = new ArrayList<Section>(sectionsList);
            
            JSONArray allSectionsArr = null;
            JSONObject allSectionsObj = null; 
            
            try {
            	 prefix = courseData.getString("prefix"); 
            }
            catch (JSONException je){
            	prefix = null; 
            }
            
            try {
            	number = courseData.getString("number"); 
            }
            catch (JSONException je){
            	prefix = null; 
            }
            
            if (prefix != null && number != null) {
            	name = prefix + "-" + number;
            }  
            
            try {
            	title = courseData.getString("title");
            }
            catch (JSONException je){
            	title = null; 
            }
            
            try {
            	description = courseData.getString("description");
            }
            catch (JSONException je){
            	description = null; 
            }
            
            try {
            	try {
            		allSectionsArr = courseData.getJSONArray("SectionData");
            	}
            	catch (JSONException je){
            		allSectionsObj = courseData.getJSONObject("SectionData");
            	}
            	

                if (allSectionsArr != null) {
                	for (int j = 0; j < allSectionsArr.length(); j++) {
                        Section newSection = null; 
                        
                    	JSONObject currSection = allSectionsArr.getJSONObject(j);
                    	
                    	String id = null;
                    	String type = null;
                    	String dClassCode = null;
                    	String availableSeats = null;
                    	String startTime = null;
                    	String endTime = null; 
                    	String dates = null; 
                    	
                        try {
                        	id = currSection.getString("id"); 
                        }
                        catch (JSONException je){
                        	id = null; 
                        }
                    	
                        try {
                          	 type = currSection.getString("type");
                        }
                        catch (JSONException je){
                        	type = null; 
                        }
                    	
                        try {
                        	dClassCode = currSection.getString("dclass_code");
                        }
                        catch (JSONException je){
                        	dClassCode = null;
                        }

                        try {
                        	availableSeats = currSection.getString("spaces_available"); 
                        }
                        catch (JSONException je){
                        	availableSeats = null;
                        }
                    	
                    	
                        try {
                          	startTime =  currSection.getString("start_time"); 
                        }
                        catch (JSONException je){
                        	startTime = null;
                        }
                    	
                    	
                        try {
                        	endTime = currSection.getString("end_time"); 
                        }
                        catch (JSONException je){
                        	endTime = null;
                        }
                    	
                    	
                        try {
                        	dates = currSection.getString("day");
                        }
                        catch (JSONException je){
                        	dates = null; 
                        }
                        
                        // System.out.println(id + " " + name + " " + title + dClassCode + " " + availableSeats + " " + startTime + " " + endTime + " " + dates); 
                        newSection = new Section(id, name, type, dClassCode, availableSeats, startTime, endTime, dates);
                        sections.add(newSection); 
                    }
                }
                else {
                	Section newSection = null; 
                	String id = null;
                	String type = null;
                	String dClassCode = null;
                	String availableSeats = null;
                	String startTime = null;
                	String endTime = null; 
                	String dates = null; 
                	
                    try {
                    	id = allSectionsObj.getString("id"); 
                    }
                    catch (JSONException je){
                    	id = null; 
                    }
                	
                    try {
                      	 type = allSectionsObj.getString("type");
                    }
                    catch (JSONException je){
                    	type = null; 
                    }
                	
                    try {
                    	dClassCode = allSectionsObj.getString("dclass_code");
                    }
                    catch (JSONException je){
                    	dClassCode = null;
                    }

                    try {
                    	availableSeats = allSectionsObj.getString("spaces_available"); 
                    }
                    catch (JSONException je){
                    	availableSeats = null;
                    }
                	
                	
                    try {
                      	startTime =  allSectionsObj.getString("start_time"); 
                    }
                    catch (JSONException je){
                    	startTime = null;
                    }
                	
                	
                    try {
                    	endTime = allSectionsObj.getString("end_time"); 
                    }
                    catch (JSONException je){
                    	endTime = null;
                    }
                	
                	
                    try {
                    	dates = allSectionsObj.getString("day");
                    }
                    catch (JSONException je){
                    	dates = null; 
                    }
                    
                    // System.out.println(id + " " + name + " " + title + dClassCode + " " + availableSeats + " " + startTime + " " + endTime + " " + dates); 
                    newSection = new Section(id, name, type, dClassCode, availableSeats, startTime, endTime, dates);
                    sections.add(newSection); 
                }
            }
            catch (JSONException je) {
            	
            }
            
            Course newCourse = new Course(name, title, description, sections);
//        	if (name.equals("CSCI-360")) {
//        		System.out.println("CS-360:" + newCourse.getNumLectures());
//        	}
            courses.add(newCourse);
        }
        
        for (int i = 0; i < courses.size(); i++) {
        	// System.out.println(courses.get(i).getName()); 
//        	if (courses.get(i).getName().equals("CSCI-360")) {
//        		System.out.println("CS-360:" + courses.get(i).getSections().size());
//        	}
//        	for (int j = 0; j < courses.get(i).getSections().size(); j++) {
//        		// System.out.println(courses.get(i).getSections().get(j).getType()); 
//        	}
        }
        return courses;
    }

    public static void printCourses(ArrayList<Course> courses){
        for(int i = 0; i < courses.size(); i++){
            Course c = courses.get(i);
            System.out.println("Name: " + c.getName());
            System.out.println("Title: " + c.getTitle());
            System.out.println("Description: " + c.getDescription());
            System.out.println("--------------------------------------------------");

            for(int j = 0; j < c.getSections().size(); j++){
                Section s = c.getSections().get(j);
                System.out.println("ID: " + s.getId() + s.getDClassCode());
                System.out.println("Type: " + s.getType());
                
                System.out.println("Day: "); 
                for (int k = 0; k < s.getDates().size(); i++) {
                	System.out.println(s.getDates().get(k));
                }
                
                System.out.println("Start Time: " + s.getStartTime()); 
                
                System.out.println("End Time: " + s.getEndTime()); 
                
//                System.out.println("Location: " + s.get);
//                System.out.println("Professor: " + s.prof_name);
                System.out.println();
            }

        }
           
        
    }
    
    
    public static void main(String[] args){
        for(int i = 0; i < departments.length; i++){
            String department = departments[i];
            try{
                String json_response = getDepartmentJsonString(department, semester);
                System.out.println(json_response);
                ArrayList<Course> courses = createClassMapping(json_response);
                printCourses(courses);
            }
            catch(Exception e){
                System.out.println("Error while fetching data for " + department + ": " + e.getMessage());
            }
        }
    }
    
}

//class Section{
//    String id_and_d_class_code;
//    String type;
//    String day;
//    String time;
//    String loc;
//    String prof_name;
//    Section(String id_and_d_class_code, String type, String day, String time, String loc, String prof_name){
//        this.id_and_d_class_code = id_and_d_class_code;
//        this.type = type; 
//        this.day = day;
//        this.time = time;
//        this.loc = loc;
//        this.prof_name = prof_name;
//    }
//}
//
//class Course{
//    String name;
//    String title;
//    String description; 
//    String units;
//    Vector<Section> sections;
//
//    Course(String name, String title, String description, String units, Vector<Section> sections){
//        this.name = name;
//        this.title = title;
//        this.description = description; 
//        this.sections = sections;
//    }
//}