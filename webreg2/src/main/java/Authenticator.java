import java.io.IOException;
import java.util.Vector;
   
public class Authenticator {
	public String user_id;
	// login
	// logout
	// register
	public Authenticator(String username, String password, String schema) throws IOException{
		if(!JDBC.hasConnection())
			// REPLACE connection username
			JDBC.establishJDBCConnection(username, password, schema);
        if(!JDBC.hasConnection())
        	throw new IOException("There is no connection to database");
	}
	
	// if user is logged in, the identifier would be on flie
	// inSession(identifier) would return the userid on the device
	public String login(String username, String password) {
		// if error, return error message string from getUser
		// if password incorrect, return "password incorrect!"
		// identifier: the thing used to identify the device the user is currently on
		// else, add user to session and return "login successful"
		Vector<String> result = JDBC.getUser(username);
        
		if(result.size() == 1)
			return result.getFirst();
		
		if(!result.get(3).equals(password))
			return "password incorrect!";
		
		user_id = result.get(1);
		
		return "login successful";
	}
	
	public String logout(String username) {
		Vector<String> result = JDBC.getUser(username);
		if(result.size() == 1)
			return result.getFirst();
		
		return "logout successful";
	}
	
	public String register(String username, String email, String password) {

		Vector<String> result = JDBC.getUser(username);
		if(result.size() > 1) {
			user_id = result.get(1);
			return "username already exists";
		}	
		
		boolean register = JDBC.insertUser(username, email, password);
		if(register) {
			result = JDBC.getUser(username);
			user_id = result.get(1);
			return "registraton successful";
		} else {
			user_id = "-1";
			return "registration unsuccessful, input 100 character max";
		}
		
	}
	
	public String deleteAccount(String username) {
		Vector<String> result = JDBC.getUser(username);
		
		if(result.size() == 1)
			return result.getFirst();
		
		JDBC.dropUser(Integer.parseInt(result.getFirst()));
		return "deletion successful";
	}
	
}
