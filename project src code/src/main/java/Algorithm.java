import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Algorithm {
	
	private List<Section> resList = Collections.synchronizedList(new ArrayList<Section>());
	private ArrayList<Section> res = new ArrayList<Section>(resList); 
	
	private List<Section> currSectionsList = Collections.synchronizedList(new ArrayList<Section>());
	private ArrayList<Section> currSections = new ArrayList<Section>(currSectionsList); 
	
	private List<SubCourse> subCoursesList = Collections.synchronizedList(new ArrayList<SubCourse>());
	private ArrayList<SubCourse> subCourses = new ArrayList<SubCourse>(subCoursesList); 
	
	private Integer minSecionsRequired = 0; 
	
	public Algorithm(ArrayList<Course> wantedClasses) {
		
		for (int i = 0; i < wantedClasses.size(); i++) {
			Course currClass = wantedClasses.get(i);
			if (!currClass.getLecSubCourse().getSections().isEmpty()) {
				subCourses.add(currClass.getLecSubCourse());
				minSecionsRequired++; 
			}
			
			if (!currClass.getDisSubCourse().getSections().isEmpty()) {
				subCourses.add(currClass.getDisSubCourse());
				minSecionsRequired++;
			}
			
			if (!currClass.getQuizSubCourse().getSections().isEmpty()) {
				subCourses.add(currClass.getQuizSubCourse());
				minSecionsRequired++;
			}
			
			if (!currClass.getLabSubCourse().getSections().isEmpty()) {
				subCourses.add(currClass.getLabSubCourse());
				minSecionsRequired++;
			}
			
		}
		
		System.out.println("Number of subcourses: " + subCourses.size());
	}
	
	public Integer getMinSecionsRequired() {
		return minSecionsRequired; 
	}

	public ArrayList<Section> getRes() {
		return res;
	}

	public void setRes(ArrayList<Section> res) {
		this.res = res;
	}
	
	

	public void compute(int currIndex) {
		// Base case - reached the end of wantedClasses
		if (currIndex == subCourses.size()) {
			res = new ArrayList<>(currSections); 
			return;
		}

		// Current class
		SubCourse currSubcourse = subCourses.get(currIndex); 
		ArrayList<Section> sections = currSubcourse.getSections(); 
		
		for (int i = 0; i < sections.size(); i++) {
			if (!hasOverlap(sections.get(i), currSections)) {
				currSections.add(sections.get(i));
				
				compute(currIndex+1); 
				
				// System.out.println("Removing: " + currClass.getName());
				currSections.remove(currSections.size()-1);
			}
		}
	}
	
	private Boolean hasOverlap(Section potentialSection, ArrayList<Section> currSections) {
		// System.out.println(potentialSection.getType()); 
		
		for (int i = 0; i < currSections.size(); i++) {
			Section currSection = currSections.get(i); 
			
			// If there is a date overlap
			for (int j = 0; j < 5; j++) {
				if (currSection.getDates().get(j) == 1 && potentialSection.getDates().get(j) == 1) {
					// If there is a time overlap
					
					if (((potentialSection.getStartTime().getHour() * 60) + potentialSection.getStartTime().getMinute()) < ((currSection.getEndTime().getHour() * 60) + currSection.getEndTime().getMinute()) && 
						(((currSection.getStartTime().getHour() * 60) + currSection.getStartTime().getMinute()) < ((potentialSection.getEndTime().getHour() * 60) + potentialSection.getEndTime().getMinute()))) {
						return true; 
					}
				}
			}
		}
		
		// System.out.println(potentialSection.getStartTime().getHour() + ":" + potentialSection.getStartTime().getMinute() + "-" + potentialSection.getEndTime().getHour() + ":" + potentialSection.getEndTime().getMinute());
		return false; 
	}

}
