

public class Time {
	private Integer hour;
	private Integer minute;
	
	public Integer getHour() {
		return hour;
	}

	public void setHour(Integer hour) {
		this.hour = hour;
	}

	public Integer getMinute() {
		return minute;
	}

	public void setMinute(Integer minute) {
		this.minute = minute;
	}

	public Time() {
		hour = 0; 
		minute = 0; 
	}
	
	public Time(Integer hour, Integer minute) {
		this.hour = hour; 
		this.minute = minute; 
	}
	
	public Time(String hour, String minute) {
		this.hour = Integer.parseInt(hour); 
		this.minute = Integer.parseInt(minute); 
	}
}
