import java.util.ArrayList;

public class SubCourse {
    private String name;
    private String title;
    private String description;
//    private Double units;
	private ArrayList<Section> sections;
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
	public ArrayList<Section> getSections() {
		return sections;
	}
	public void setSections(ArrayList<Section> sections) {
		this.sections = sections;
	}
	public SubCourse(String name, String title, String description, ArrayList<Section> sections) {
		super();
		this.name = name;
		this.title = title;
		this.description = description;
		this.sections = sections;
	}
	
	
}
