import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Vector;
import org.json.JSONArray;
import org.json.JSONObject;

// Compile: javac -cp "lib/*" CourseInfoFinder.java
// Run: java -cp ".;lib/*" CourseInfoFinder

public class CourseInfoFinder {

    static String[] departments = new String[]{"AHIS", "ALI", "AMST", "ANTH", "ARAB", "ASTR", "BISC", "CHEM", "CLAS", "COLT", "CORE", "CSLC",
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
    static String semester = "20243";

    private static String getDepartmentJsonString(String department_id, String semester_id) throws Exception{
        try {
            String url_text = "https://web-app.usc.edu/web/soc/api/classes/" + department_id + "/" + semester_id;
            
            // Create a HttpClient
            HttpClient client = HttpClient.newHttpClient();
            
            // Create a HttpRequest
            HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url_text)).build();
            
            // Send the request and get the response
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return response.body();
            
        } catch (IOException | InterruptedException e) {
            throw e;
        }

    }
    public static Vector<Course> createClassMapping(String json_response){
        Vector<Course> courses = new Vector<Course>();
        HashMap<String, Course> nameToCourse = new HashMap<String, Course>();

        // Parse the JSON string into a JSONObject
        JSONObject jsonObject = new JSONObject(json_response);
        JSONArray all_courses = (jsonObject.getJSONObject("OfferedCourses")).getJSONArray("course");
        for(int i = 0; i < all_courses.length(); i++){
           
            JSONObject course_data = ((JSONObject) all_courses.get(i)).getJSONObject("CourseData");
            
            String name = course_data.getString("prefix") + "-" + course_data.getString("number");

            String title;
            try{
                title = course_data.getString("title");
            }
            catch(Exception e){
                title = "N/A";
            }

            String description = "N/A";
            try{
                description = course_data.getString("description");
            }
            catch(Exception e){
                description = "N/A";
            }

            String units;
            try{
                units = course_data.getString("units");
                units = units.split(",")[0];
            }
            catch(Exception e){
                units = "N/A";
            }

            // Add course to courses if it doesn't exist
            if(!(nameToCourse.containsKey(name))){
                Vector<Section> sections = new Vector<Section>();
                Course c = new Course(name, title, description, units, sections);
                nameToCourse.put(name, c); // update hashmap
                courses.addElement(c); // add to list of courses
            }

            JSONArray all_sections;
            try{
                all_sections = course_data.getJSONArray("SectionData");
                
            }
            catch(Exception e){
                // only one section
                
                JSONObject section = course_data.getJSONObject("SectionData");


                all_sections = new JSONArray();

            }
            

            for(int j = 0; j < all_sections.length(); j++){
                JSONObject section_data = ((JSONObject) all_sections.get(j));

                String id_and_d_class_code;
                try{
                    id_and_d_class_code = section_data.getString("id") + section_data.getString("dclass_code");
                }
                catch(Exception e){
                    id_and_d_class_code = "Unknown";
                }

                String type;
                try{
                    type = section_data.getString("type");
                }
                catch(Exception e){
                    type = "Unknown";
                }

                String day;
                try{
                    day = section_data.getString("day");
                }
                catch(Exception e){
                    day = "Unknown";
                }

                // Create the time string
                String time;
                try{
                    time = section_data.getString("start_time") + "-" + section_data.getString("end_time");
                }
                catch(Exception e){
                    time = "Unknown";
                }

                String loc;
                try{
                    loc = section_data.getString("location");
                }
                catch(Exception e){
                    loc = "Unknown";
                }

                String prof_name;
                try{
                    JSONObject prof = section_data.getJSONObject("instructor");
                    prof_name = prof.getString("last_name") + "," + prof.getString("first_name");
                }
                catch(Exception e){
                    prof_name = "Unknown";
                }
                
                

                System.out.println(id_and_d_class_code);
                System.out.println(type);
                System.out.println(day);
                System.out.println(time);
                System.out.println(loc);
                System.out.println(prof_name);
                Section s = new Section(id_and_d_class_code, type, day, time, loc, prof_name);
                (nameToCourse.get(name)).sections.addElement(s);

            }

            //System.out.println(all_sections);

            /*
            System.out.println(name);
            System.out.println(title);
            System.out.println(description);
            System.out.println(units);
            */

            
        }
        
        System.out.println();
        return courses;
    }

    public static void printCourses(Vector<Course> courses){
        for(int i = 0; i < courses.size(); i++){
            Course c = courses.get(i);
            System.out.println("Name: " + c.name);
            System.out.println("Title: " + c.title);
            System.out.println("Description: " + c.description);
            System.out.println("--------------------------------------------------");

            for(int j = 0; j < c.sections.size(); j++){
                Section s = c.sections.get(j);
                System.out.println("ID: " + s.id_and_d_class_code);
                System.out.println("Type: " + s.type);
                System.out.println("Day: " + s.day);
                System.out.println("Time: " + s.time);
                System.out.println("Location: " + s.loc);
                System.out.println("Professor: " + s.prof_name);
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
                Vector<Course> courses = createClassMapping(json_response);
                printCourses(courses);
            }
            catch(Exception e){
                System.out.println("Error while fetching data for " + department + ": " + e.getMessage());
            }
        }
        
    }
}

class Section{
    String id_and_d_class_code;
    String type;
    String day;
    String time;
    String loc; 
    String prof_name;
    Section(String id_and_d_class_code, String type, String day, String time, String loc, String prof_name){
        this.id_and_d_class_code = id_and_d_class_code;
        this.type = type; 
        this.day = day;
        this.time = time;
        this.loc = loc;
        this.prof_name = prof_name;
    }
}

class Course{
    String name;
    String title;
    String description; 
    String units;
    Vector<Section> sections;

    Course(String name, String title, String description, String units, Vector<Section> sections){
        this.name = name;
        this.title = title;
        this.description = description; 
        this.sections = sections;
    }
}