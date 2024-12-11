// Each individual section/class, it has the id, title, description, type, units, spaces available, start and end time


import java.util.ArrayList;
import java.util.Arrays;

public class Section {
	private String id; // ID
	private String name;
//	private String title; // Class title
//	private String description; // Section description/notes
	private String type; // Lecture, discussion, quiz, or lab
	private String dClassCode; // D Clearance Code
//	private Double units; // # of Units
	private Integer spacesAvailable; // # of Spaces available
	private Time startTime; // Start time 
	private Time endTime; // Start date
	private ArrayList<Integer> dates; // Dates of the session

	public String getId() {
		return id;
	}
	
//	public void setID(String id) {
//		this.id = Integer.parseInt(id); 
//	}
	
	public void setId(String id) {
		this.id = id;
	}
	
//	public String getTitle() {
//		return title;
//	}
//
//	public void setTitle(String title) {
//		this.title = title;
//	}

//	public String getDescription() {
//		return description;
//	}
//
//	public void setDescription(String description) {
//		this.description = description;
//	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	public String getDClassCode() {
		return dClassCode; 
	}
	
	public void setDClassCode(String dClassCode) {
		this.dClassCode = dClassCode; 
	}
	
//	public Double getUnits() {
//		return units;
//	}
//	
//	public void setUnits(String units) {
//		if (units == null) {
//			this.units = null; 
//		}
//		
//		else {
//			this.units = Double.parseDouble(units); 
//		}
//		
//	}
//	
//	public void setUnits(Double units) {
//		this.units = units;
//	}

	public Integer getSpacesAvailable() {
		return spacesAvailable;
	}
		
	public void setSpacesAvailable(String spacesAvailable) {
		if (spacesAvailable == null) {
			this.spacesAvailable = null; 
		}
		else {
			this.spacesAvailable = Integer.parseInt(spacesAvailable);
		}
		
	}
	
	public void setSpacesAvailable(Integer spacesAvailable) {
		this.spacesAvailable = spacesAvailable;
	}

	public Time getStartTime() {
		return startTime;
	}

	public void setStartTime(Time startTime) {
		this.endTime = startTime;
	}

	public Time getEndTime() {
		return endTime;
	}

	public void setEndTime(Time end_time) {
		this.endTime = end_time;
	}
	
	public ArrayList<Integer> getDates(){
		return dates; 
	}
	
	public void setDates(ArrayList<Integer> dates) {
		this.dates = dates; 
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	
	public void setDates(String dateString) {
		dates = new ArrayList<Integer>(Arrays.asList(0, 0, 0, 0, 0)); 
		
		if (dateString != null) {
			if (dateString.contains("M")) {
				dates.set(0, 1); 
				dateString.replace("M", ""); 
			}
			
			if (dateString.contains("T")) {
				dates.set(1, 1); 
				dateString.replace("T", ""); 
			}
			
			if (dateString.contains("W")) {
				dates.set(2, 1); 
				dateString.replace("W", ""); 
			}
			
			if (dateString.contains("F")) {
				dates.set(4, 1); 
				dateString.replace("F", ""); 
			}
			
		
			if (dateString.contains("H")) {
				dates.set(3, 1); 
				dateString.replace("H", ""); 
			}
		}
	}
	
	
	public Section() {
		id = null;
		type = null;
		dClassCode = null;
//		units = 0.0; 
		spacesAvailable = 0;
		startTime = new Time();
		endTime = new Time();
		dates = new ArrayList<>();
	}
	
	public Section(String id, String name, String type, String dClassCode, String spacesAvailable, 
			String startTime, String endTime, String dates) {
		super();
		
		this.name = name; 
		this.id = id;
		this.type = type;
		this.dClassCode = dClassCode;
		
//		this.setUnits(units);
		this.setSpacesAvailable(spacesAvailable);
		
		if (startTime == null) {
			this.startTime = null; 
		}
		else {
			Integer startHourIndex = startTime.indexOf(':'); 
			if (startHourIndex == -1) {
				// System.out.println(id); 
				this.startTime = null;
			}
			else {
				String startHour = startTime.substring(0, startHourIndex); 
				String startMinute = startTime.substring(startHourIndex+1); 
				this.startTime = new Time(startHour, startMinute);
				
				// System.out.println("Start time: " + startHour + ":" + startMinute);
			}
 
		}
		
		if (endTime == null) {
			this.endTime = null; 
		}
		else {
			
			Integer endHourIndex = endTime.indexOf(':'); 
			if (endHourIndex == -1) {
				// System.out.println(id);
				this.endTime = null; 
			}
			else {
				String endHour = endTime.substring(0, endHourIndex); 
				String endMinute = endTime.substring(endHourIndex+1); 
				
				 
				this.endTime = new Time(endHour, endMinute);
				
				// System.out.println("End time: " + endHour + ":" + endMinute);
			}
		}
		
		

		this.setDates(dates);
	}

}
