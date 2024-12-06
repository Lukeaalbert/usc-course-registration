import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Vector;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
   
public class Authenticator {
	private Path session; // stores the Path object to the session file for list of users in session
	public String user_id;
	// login
	// logout
	// register
	public Authenticator() throws IOException{
		session = Paths.get("session.csv");
        if(!Files.exists(session))
        	Files.createFile(session); // if doesn't have session file for tracking user, create new one
        if(!JDBC.hasConnection())
        	throw new IOException("There is no connection to database");
	}
	
	// return whether current identifier is in session
	// if in session, return the user_id in session
	// if not, return 0;
	public int inSession(String identifier) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(session.toAbsolutePath().toString()));
		String content = "";
		String line;
		while((line=br.readLine()) != null && (!line.equals("")))
			if(line.split(",")[1].equals(identifier)) {
				return Integer.parseInt(line.split(",")[0]);
			}
		return 0;
	}
	
	// change to private later
	// add a user to the current session; if current username is already attached to a session, overwrite it
	private void addSession(int user_id, String identifier) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(session.toAbsolutePath().toString()));
		String content = "";
		String line;
		while((line=br.readLine()) != null && (!line.equals("")))
			if(Integer.parseInt(line.split(",")[0]) != user_id) // if in session, skip past record
				content = content + line + "\n";
		
		PrintWriter pw = new PrintWriter(new FileWriter(session.toAbsolutePath().toString()));
		content = content + user_id + "," + identifier;
		pw.write(content);
		pw.flush();
		br.close();
		pw.close();
	}
	
	private void removeSession(int user_id) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(session.toAbsolutePath().toString()));
		String content = "";
		String line;
		while((line=br.readLine()) != null && (!line.equals("")))
			if(Integer.parseInt(line.split(",")[0]) != user_id) // if in session, skip past record
				content = content + line + "\n";
		
		PrintWriter pw = new PrintWriter(new FileWriter(session.toAbsolutePath().toString()));
		pw.write(content);
		pw.flush();
		br.close();
		pw.close();
	}
	
	// if user is logged in, the identifier would be on flie
	// inSession(identifier) would return the userid on the device
	public String login(String username, String password) {
		// if error, return error message string from getUser
		// if password incorrect, return "password incorrect!"
		// identifier: the thing used to identify the device the user is currently on
		// else, add user to session and return "login successful"
		Vector<String> result = JDBC.getUser(username);
        LocalDateTime now = LocalDateTime.now();

		// Define the format
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

		// Format the current date and time
		String identifier =  now.format(formatter);
        
		if(result.size() == 1)
			return result.getFirst();
		
		if(!result.get(3).equals(password))
			return "password incorrect!";
		
		try {
			addSession(Integer.parseInt(result.getFirst()), identifier);
		} catch (NumberFormatException e) {
			
			return "unable to add current user to session: " + e.getMessage();
		} catch (IOException e) {
			e.printStackTrace();
			return "unable to add current user to session: " + e.getMessage();
		}
		
		user_id = result.getFirst();
		
		return "login successful";
	}
	
	public String logout(String username) {
		Vector<String> result = JDBC.getUser(username);
		if(result.size() == 1)
			return result.getFirst();
		try {
			removeSession(Integer.parseInt(result.getFirst()));
		} catch (NumberFormatException e) {
			
			return "unable to remove current user from session: " + e.getMessage();
		} catch (IOException e) {
			e.printStackTrace();
			return "unable to remove current user from session: " + e.getMessage();
		}
		return "logout successful";
	}
	
	public String register(String username, String email, String password) {
		// REPLACE connection 
		if(!JDBC.hasConnection())
			JDBC.establishJDBCConnection("root", "temp_pass123");
		
		Vector<String> result = JDBC.getUser(username);
		if(result.size() > 1)
			return "username already exists";
		
		boolean register = JDBC.insertUser(username, email, password);
		if(register) {
			result = JDBC.getUser(username);
			user_id = result.getFirst();
			return "registraton successful";
		} else {
			user_id = "-1";
			return "registration unsuccessful, input 100 character max";
		}
		
	}
	
	public String deleteAccount(String username) {
		Vector<String> result = JDBC.getUser(username);
		// REPLACE connection 
		if(!JDBC.hasConnection())
			JDBC.establishJDBCConnection("root", "temp_pass123");
		
		if(result.size() == 1)
			return result.getFirst();
		
		JDBC.dropUser(Integer.parseInt(result.getFirst()));
		return "deletion successful";
	}
	
	public static void main(String args[]) {
		try {
			JDBC database = new JDBC();
			database.establishJDBCConnection("root", "temp_pass123");
			Authenticator a = new Authenticator();
			System.out.println(a.deleteAccount("user2"));
		}catch(IOException e) {
			System.out.println(e.getMessage());
		}
	}
}
