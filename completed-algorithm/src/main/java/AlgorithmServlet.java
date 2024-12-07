

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter pw = response.getWriter(); 
		
		response.setContentType("application/json"); 
		response.setCharacterEncoding("UTF-8");
		
		SelectedClasses selectedClasses = new Gson().fromJson(request.getReader(), SelectedClasses.class); 
		
		ArrayList<WantedClass> wantedClassesNames = selectedClasses.getClasses();
//		for (int i = 0; i < wantedClassesNames.size(); i++) {
//			System.out.println(wantedClassesNames.get(i).getDeptName());
//		}
		
		List<Course> wantedClassesSync = Collections.synchronizedList(new ArrayList<Course>());
		ArrayList<Course> wantedClasses = new ArrayList<Course>(wantedClassesSync);
		
		List<Course> deptCoursesList = Collections.synchronizedList(new ArrayList<Course>()); 
		ArrayList<Course> deptCourses = new ArrayList<Course>(deptCoursesList);
		
		// For each class name, I want to get the course info for that class 
		for (int i = 0; i < wantedClassesNames.size(); i++) {
			String currDeptName = wantedClassesNames.get(i).getDeptName(); 
//			String currClassID = wantedClassesNames.get(i).getClassID();
			
			String jsonString = null; 
			try {
				System.out.println(currDeptName);
				jsonString = CourseInfoFinder.getDepartmentJsonString(currDeptName, CourseInfoFinder.semester);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				System.out.println("Exception: " + e.getMessage()); 
			}
			
//			System.out.println(currDeptName + " " + currClassID);
			
			// Address 1.0-8.0 units
//			System.out.println(jsonString);
			deptCourses.addAll(CourseInfoFinder.createClassMapping(jsonString)); // Throws an exception
//			System.out.println(deptCourses.size());
//			System.aout.println("solved");
			
		}
		
		for (int i = 0; i < wantedClassesNames.size(); i++) {
			String currDeptName = wantedClassesNames.get(i).getDeptName(); 
			String currClassID = wantedClassesNames.get(i).getClassID();
			String currName = currDeptName + "-" + currClassID; 
			try {				
				// System.out.println("finding: " + currName);
				Boolean foundClass = false;
				for (int j = 0; j < deptCourses.size(); j++) {
					
					// System.out.println(deptCourses.get(j).getName());
					if (deptCourses.get(j).getName().trim().equals(currName.trim())) {
						Course currCourse = deptCourses.get(j); 
						// System.out.println(currCourse.getName() + " " + currCourse.getNumLectures());
						wantedClasses.add(deptCourses.get(j)); 
						
						foundClass = true;
						break;
					}
				}
				
				if (foundClass == false) {

					// System.out.println(wantedClasses.size());
					
					throw new Exception("Course not found");
				}
				else {
					ArrayList<Section> testSections = wantedClasses.getLast().getSections(); 
					System.out.println(wantedClasses.getLast().getName());
//					for (int j = 0; j < testSections.size(); j++) {
//						System.out.println(testSections.get(j).getType() + " " + testSections.get(j).getStartTime().getHour() + ":" + testSections.get(j).getStartTime().getMinute() + "-" 
//								+ testSections.get(j).getEndTime().getHour() + ":" + testSections.get(j).getEndTime().getMinute()); 
//					}
				}
				
			} catch (Exception e) {
				System.out.println("Exception: " + e.getMessage()); 
			}
		}
		
		for (int i = 0; i < wantedClasses.size(); i++) {
			System.out.println(wantedClasses.get(i).getName());
//			for (int j = 0; j < wantedClasses.get(i).getSections().size(); j++) {
//				Section currSection = wantedClasses.get(i).getSections().get(j);
//				System.out.println(currSection.getType() + " - " + currSection.getStartTime().getHour() + ":" + currSection.getStartTime().getMinute() + 
//						" - " + currSection.getEndTime().getHour() + ":" + currSection.getEndTime().getMinute()); 
//			}
		}
		
		Algorithm algorithm = new Algorithm(wantedClasses); 
		// System.out.println(wantedClasses.size());
		algorithm.compute(0); 
		
		
		ArrayList<Section> res = algorithm.getRes(); 
		System.out.println("finished " + res.size());
		
		for (int j = 0; j < res.size(); j++) {
			System.out.println(res.get(j).getType());
		}
		
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
