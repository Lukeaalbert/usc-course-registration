import java.util.ArrayList;
import java.util.Arrays;

public class Algorithm {
	private ArrayList<Class> wantedClasses; 
	private ArrayList<Section> res; 
	
	public void getRes(int currIndex, ArrayList<Section> currSections) {
		// Base case - reached the end of wantedClasses
		if (currIndex == res.size()) {
			res = new ArrayList<>(currSections); 
			return;
		}
		
		// Current class
		Class currClass = wantedClasses.get(currIndex); 
		
		
		ArrayList<Section> lectures = currClass.getLectures();
		ArrayList<Section> labs = currClass.getLabs(); 
		ArrayList<Section> discussions = currClass.getDiscussions(); 
		ArrayList<Section> quizes = currClass.getQuizes(); 
		
		// If a lecture haven't been added to res yet
		if (currClass.getNumLectures() == 0) {
			for (int i = 0; i < lectures.size(); i++) {
				Section section = lectures.get(i); 
				if (!hasOverlap(section, currSections)) {
					currSections.add(section);
					currClass.setNumLectures(1); 
					
					if (currClass.getNumDiscussions() == 0 && discussions.size() != 0) {
						getRes(currIndex, currSections); 
						currSections.remove(currSections.size()-1);
						currClass.setNumLectures(0);
					}
					
					if (currClass.getNumQuizes() == 0 && quizes.size() != 0) {
						getRes(currIndex, currSections); 
						currSections.remove(currSections.size()-1); 
						currClass.setNumLectures(0);
					}
					
					if (currClass.getNumLabs() == 0 && labs.size() != 0) {
						getRes(currIndex, currSections); 
						currSections.remove(currSections.size()-1); 
						currClass.setNumLectures(0);
					}
					
					currSections.remove(currSections.size()-1); 
					currClass.setNumLectures(0);
				}
			}
		}
		
		if (currClass.getNumQuizes() == 0) {
			for (int i = 0; i < quizes.size(); i++) {
				Section section = quizes.get(i); 
				if (!hasOverlap(section, currSections)) {
					currSections.add(section);
					currClass.setNumQuizes(1); 
					
					if (currClass.getNumDiscussions() == 0 && discussions.size() != 0) {
						getRes(currIndex, currSections); 
						currSections.remove(currSections.size()-1); 
						currClass.setNumQuizes(0);
					}
					
					if (currClass.getNumLectures() == 0 && lectures.size() != 0) {
						getRes(currIndex, currSections); 
						currSections.remove(currSections.size()-1); 
						currClass.setNumQuizes(0);
					}
					
					if (currClass.getNumLabs() == 0 && labs.size() != 0) {
						getRes(currIndex, currSections); 
						currSections.remove(currSections.size()-1); 
						currClass.setNumQuizes(0);
					}
				}
				
				currSections.remove(currSections.size()-1); 
				currClass.setNumLectures(0);
			}
		}
		
		if (currClass.getNumLabs() == 0) {
			for (int i = 0; i < labs.size(); i++) {
				Section section = labs.get(i); 
				if (!hasOverlap(section, currSections)) {
					currSections.add(section);
					currClass.setNumLabs(1); 
					
					if (currClass.getNumDiscussions() == 0 && discussions.size() != 0) {
						getRes(currIndex, currSections); 
						currSections.remove(currSections.size()-1); 
						currClass.setNumLabs(0);
					}
					
					if (currClass.getNumQuizes() == 0 && quizes.size() != 0) {
						getRes(currIndex, currSections); 
						currSections.remove(currSections.size()-1); 
						currClass.setNumLabs(0);
					}
					
					if (currClass.getNumLectures() == 0 && lectures.size() != 0) {
						getRes(currIndex, currSections); 
						currSections.remove(currSections.size()-1); 
						currClass.setNumLabs(0);
					}
					
					currSections.remove(currSections.size()-1); 
					currClass.setNumLectures(0);
				}
			}
		}
		
		if (currClass.getNumDiscussions() == 0) {
			for (int i = 0; i < discussions.size(); i++) {
				Section section = discussions.get(i); 
				if (!hasOverlap(section, currSections)) {
					currSections.add(section);
					currClass.setNumDiscussions(1); 
					
					if (currClass.getNumLectures() == 0 && lectures.size() != 0) {
						getRes(currIndex, currSections); 
						currSections.remove(currSections.size()-1); 
						currClass.setNumDiscussions(0);
					}
					
					if (currClass.getNumQuizes() == 0 && quizes.size() != 0) {
						getRes(currIndex, currSections); 
						currSections.remove(currSections.size()-1); 
						currClass.setNumDiscussions(0);
					}
					
					if (currClass.getNumLabs() == 0 && currClass.getLabs().size() != 0) {
						getRes(currIndex, currSections); 
						currSections.remove(currSections.size()-1); 
						currClass.setNumDiscussions(0);
					}
					
					currSections.remove(currSections.size()-1); 
					currClass.setNumLectures(0);
				}
			}
		}
		
		
	}
	
	private Boolean hasOverlap(Section potentialSection, ArrayList<Section> currSections) {
		for (int i = 0; i < currSections.size(); i++) {
			Section currSection = currSections.get(i); 
			
			// If there is a date overlap
			for (int j = 0; j < 5; j++) {
				if (currSection.getDates().get(j) == 1 && potentialSection.getDates().get(j) == 1) {
					// If there is a time overlap
					
					if (((potentialSection.getStart_time().hour * 60) + potentialSection.getStart_time().minute) < ((currSection.getEnd_time().hour * 60) + currSection.getEnd_time().minute) && 
						(((currSection.getStart_time().hour * 60) + currSection.getStart_time().minute) < ((potentialSection.getEnd_time().hour * 60) + potentialSection.getEnd_time().minute))) {
						return true; 
					}
				}
			}
		}
		return false; 
	}
	
	public static void main(String[] args) {
		
	}

}
