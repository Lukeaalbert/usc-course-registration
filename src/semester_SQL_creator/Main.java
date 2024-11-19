package semester_SQL_creator;
import java.util.Scanner;
import java.util.Vector;

// Note: make sure ur currently in src directory when compiling and running.
// Note: let it run...it may take a while, but that's expected.

// Compile: javac -cp "semester_SQL_creator/lib/*" semester_SQL_creator/*.java
// Run:
// Windows: java -cp ".;semester_SQL_creator/lib/*" semester_SQL_creator.Main
// Mac/Linux: java -cp ".:semester_SQL_creator/lib/*" semester_SQL_creator.Main

public class Main {

    public static void main(String[] args){
        Vector<Vector<Course>> allCourses = new Vector<>();
        for(int i = 0; i < CourseInfoFinder.departments.length; i++){
            String department = CourseInfoFinder.departments[i];
            try{
                String json_response = CourseInfoFinder.getDepartmentJsonString(department, CourseInfoFinder.semester);
                Vector<Course> courses = CourseInfoFinder.createClassMapping(json_response);
                allCourses.add(courses);
            }
            catch(Exception e){
                System.out.println("Error while fetching data for " + department + ": " + e.getMessage());
            }
        }
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter SQL username: ");
        String username = scanner.nextLine();
        System.out.print("Enter SQL password: ");
        String password = scanner.nextLine();
        JDBC.establishJDBCConnection(username, password);

        System.out.print("Enter new database name: ");
        String new_database_name = scanner.nextLine();
        JDBC.populateSqlDb(new_database_name, allCourses);
    }
}