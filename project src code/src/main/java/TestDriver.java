public class TestDriver {
	public static void main(String args[]) {
		JDBC.establishJDBCConnection("root", "temp_pass123", "final_proj");
		//29994D
		System.out.println(JDBC.getCourse("29994D"));
	}
}