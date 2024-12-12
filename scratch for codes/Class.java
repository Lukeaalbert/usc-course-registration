// A class that represents a Class 
// Each class has a list of available sessions/sections including lectures, labs, discussions, etc. 
import java.util.ArrayList;
public class Class {
    private String name;
    private String title;
    private String description;
    private Double units;
	private ArrayList<Section> sections;
	
	private ArrayList<Section> lectures;
	private ArrayList<Section> discussions;
	private ArrayList<Section> labs;
	private ArrayList<Section> quizes; 
	
	private int numLectures = 0; 
	private int numDiscussions = 0;
	private int numLabs = 0;
	private int numQuizes = 0;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Double getUnits() {
		return units;
	}

	public void setUnits(String units) {
		this.units = Double.parseDouble(units);
	}
	
	public void setUnits(Double units) {
		this.units = units;
	}

	public ArrayList<Section> getSections() {
		return sections;
	}

	public void setSessions(ArrayList<Section> sections) {
		this.sections = sections;
	}
	
	public ArrayList<Section> getLectures() {
		return lectures;
	}
	
//	public void setLectures(ArrayList<Section> lectures) {
//		this.lectures = lectures;
//	}

	public ArrayList<Section> getDiscussions() {
		return discussions;
	}

//	public void setDiscussions(ArrayList<Section> discussions) {
//		this.discussions = discussions;
//	}

	public ArrayList<Section> getLabs() {
		return labs;
	}

//	public void setLabs(ArrayList<Section> labs) {
//		this.labs = labs;
//	}

	public ArrayList<Section> getQuizes() {
		return quizes;
	}

//	public void setQuizes(ArrayList<Section> quizes) {
//		this.quizes = quizes;
//	}
	
	public int getNumLectures() {
		return numLectures;
	}

	public void setNumLectures(int numLectures) {
		this.numLectures = numLectures;
	}

	public int getNumDiscussions() {
		return numDiscussions;
	}

	public void setNumDiscussions(int numDiscussions) {
		this.numDiscussions = numDiscussions;
	}

	public int getNumLabs() {
		return numLabs;
	}

	public void setNumLabs(int numLabs) {
		this.numLabs = numLabs;
	}

	public int getNumQuizes() {
		return numQuizes;
	}

	public void setNumQuizes(int numQuizes) {
		this.numQuizes = numQuizes;
	}
	

	public Class(String name, String title, String description, Double units, ArrayList<Section> sections) {
		this.name = name;
		this.title = title;
		this.description = description;
		this.units = units;
		this.sections = sections;
		
		this.lectures = new ArrayList<Section>(); 
		this.discussions = new ArrayList<Section>(); 
		this.labs = new ArrayList<Section>(); 
		this.quizes = new ArrayList<Section>(); 
		
		for (int i = 0; i < sections.size(); i++) {
			Section currSection = sections.get(i); 
			
			if (currSection.getType().toLowerCase().equals("lab")) {
				this.labs.add(currSection); 
			}
			
			else if (currSection.getType().toLowerCase().equals("qz")) {
				this.quizes.add(currSection); 
			}
			
			else if (currSection.getType().toLowerCase().equals("dis")) {
				this.discussions.add(currSection);
			}
			
			else if (currSection.getType().toLowerCase().equals("lec")) {
				this.lectures.add(currSection); 
			}
		}
	}
}
