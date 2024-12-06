
public class Message {
	private String message;
	private String user_id;
	
	public Message(String message, String id) {
		this.message = message;
		this.user_id = id;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}
	
	public void setUserID(String user_id) {
		this.user_id = user_id;
	}
	
	public String getMessage() {
		return message;
	}
	
	public String getUserID() {
		return user_id;
	}
}
