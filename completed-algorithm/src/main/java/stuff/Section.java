// Each individual section/class, it has the id, title, description, type, units, spaces available, start and end time
package stuff;

import java.util.ArrayList;
import java.util.Arrays;

public class Section {
	private Integer id; // ID
	private String title; // Class title
	private String description; // Section description
	private String type; // Lecture, discussion, quiz, or lab
	private String dclass_code; // D Clearance Code
	private Double units; // # of Units
	private Integer spaces_available; // # of Spaces available
	private Time start_time; // Start time 
	private Time end_time; // Start date
	private ArrayList<Integer> dates; // Dates of the session

	public Integer getId() {
		return id;
	}
	
	public void setID(String id) {
		this.id = Integer.parseInt(id); 
	}
	
	public void setId(Integer id) {
		this.id = id;
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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	public String getDclass_code() {
		return dclass_code; 
	}
	
	public void setDclass_code(String dclass_code) {
		this.dclass_code = dclass_code; 
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

	public Integer getSpaces_available() {
		return spaces_available;
	}

	public void setSpaces_available(Integer spaces_available) {
		this.spaces_available = spaces_available;
	}

	public Time getStart_time() {
		return start_time;
	}

	public void setStart_time(Time start_time) {
		this.start_time = start_time;
	}

	public Time getEnd_time() {
		return end_time;
	}

	public void setEnd_time(Time end_time) {
		this.end_time = end_time;
	}
	
	public ArrayList<Integer> getDates(){
		return dates; 
	}
	
	public void setDates(ArrayList<Integer> dates) {
		this.dates = dates; 
	}
	
	public void setDates(String dateString) {
		dates = new ArrayList<Integer>(Arrays.asList(0, 0, 0, 0, 0)); 
		
		if (dateString.contains("M")) {
			dates.set(0, 1); 
			dateString.replace("M", ""); 
		}
		
		if (dateString.contains("TH")) {
			dates.set(3, 1); 
			dateString.replace("TH", ""); 
		}
		
		if (dateString.contains("W")) {
			dates.set(2, 1); 
			dateString.replace("W", ""); 
		}
		
		if (dateString.contains("F")) {
			dates.set(4, 1); 
			dateString.replace("F", ""); 
		}
		
		if (dateString.contains("T")) {
			dates.set(1, 1); 
			dateString.replace("T", ""); 
		}
		
		
	}
	
	public Section() {
		id = 0;
		title = "";
		description = "";
		type = "";
		dclass_code = "";
		units = 0.0; 
		spaces_available = 0;
		start_time = new Time();
		end_time = new Time();
		dates = new ArrayList<>();
	}
}
