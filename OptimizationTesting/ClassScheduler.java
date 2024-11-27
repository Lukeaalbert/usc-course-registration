package OptimizationTesting;

import java.util.*;

//Structure of Classes
class Section{
    String id_and_d_class_code;
    String type;
    String day;
    String time;
    String loc; 
    String prof_name;
    Section(String id_and_d_class_code, String type, String day, String time, String loc, String prof_name){
        this.id_and_d_class_code = id_and_d_class_code;
        this.type = type; 
        this.day = day;
        this.time = time;
        this.loc = loc;
        this.prof_name = prof_name;
    }
}

class Course{
    String name;
    String title;
    String description; 
    String units;
    Vector<Section> sections;

    Course(String name, String title, String description, String units, Vector<Section> sections){
        this.name = name;
        this.title = title;
        this.description = description; 
        this.sections = sections;
    }
}


public class ClassScheduler {

    // Input data
    private Vector<Course> courses;
    private Map<String, List<String>> preferences; //Preferred times per course
    private Set<Integer> unavailableTimes;          //Unavailable time slots
    private Map<String, Boolean> prerequisiteStatus; //Tracks completed prerequisites - working on implementing this in rn
    private Map<String, List<String>> preferredProfs; //Preferred Professors
    //Still haven't implemented a preference system yet, but we can take things like professor into account

    public ClassScheduler(List<Course> courses, Map<String, List<String>> preferences, Set<Integer> unavailableTimes, Map<String, List<String>> preferredProfessors) {
        this.courses = courses;
        this.preferences = preferences;
        this.unavailableTimes = unavailableTimes;
        this.prerequisiteStatus = new HashMap<>();
        this.preferredProfs = preferredProfessors;
    }

    //Scoring function
    //Here is where we would check if preferences are matched and decide how to "score" the course
    private int calculateScore(Course course, int time) {
        int score = 0;

        //If the time works + 10
        if (preferences.containsKey(course.name) && preferences.get(course.name).contains(time)) {
            score += 10;
        }
        
        //If preferredProfs extra points
        if (preferredProfessors.containsKey(course.name) && preferredProfessors.get(course.name).contains(section.prof_name)) {
            score += 15;
        }
        
        //If not, -50
        if (unavailableTimes.contains(time)) {
            score -= 50;
        }

        //If prerequisites not met -30 - still working on it
        /*for (String prereq : course.prerequisites) {
            if (!prerequisiteStatus.getOrDefault(prereq, false)) {
                score -= 30;
            }
        }*/

        return score;
    }
    
    //Check for conflicts in the current schedule
    private boolean hasConflict(List<Section> schedule, Section newSection) {
        String[] newTimes = newSection.time.split("-");
        int newStart = parseTime(newTimes[0]);
        int newEnd = parseTime(newTimes[1]);

        for (Section section : schedule) {
            String[] existingTimes = section.time.split("-"); //Split the start and end time
            int existingStart = parseTime(existingTimes[0]);
            int existingEnd = parseTime(existingTimes[1]);

            // Check for overlapping times
            if (newSection.day.equals(section.day) && newStart < existingEnd && newEnd > existingStart) {
                return true;
            }
        }
        return false;
    }

    //Parse time from "HH:MM" format to minutes since midnight (basically total minutes)
    private int parseTime(String time) {
        String[] parts = time.split(":");
        int hours = Integer.parseInt(parts[0]);
        int minutes = Integer.parseInt(parts[1]);
        return hours * 60 + minutes;
    }

    // Recursive backtracking to find the best schedule
    private int findBestSchedule(int courseIndex, List<Section> currentSchedule, Map<Course, Section> selectedSections, int currentScore, Map<Course, Section> finalSchedule) {
        if (courseIndex == courses.size()) {
            // Base case: All courses processed
            if (currentScore > calculateScheduleScore(finalSchedule)) {
                finalSchedule.clear();
                finalSchedule.putAll(selectedSections);
            }
            return currentScore;
        }

        Course currentCourse = courses.get(courseIndex);
        int maxScore = currentScore;
        
        //Uses backtracking
        for (Section section : currentCourse.sections) {
            if (!hasConflict(currentSchedule, section)) {
                currentSchedule.add(section);
                selectedSections.put(currentCourse, section);
                int newScore = currentScore + calculateScore(currentCourse, section);
                maxScore = Math.max(maxScore, findBestSchedule(courseIndex + 1, currentSchedule, selectedSections, newScore, finalSchedule));
                currentSchedule.remove(currentSchedule.size() - 1);
                selectedSections.remove(currentCourse);
            }
        }

        // Option to skip the current course
        maxScore = Math.max(maxScore, findBestSchedule(courseIndex + 1, currentSchedule, selectedSections, currentScore, finalSchedule));

        return maxScore;
    }

    private int calculateScheduleScore(Map<Course, Section> schedule) {
        int score = 0;
        for (Map.Entry<Course, Section> entry : schedule.entrySet()) {
            score += calculateScore(entry.getKey(), entry.getValue());
        }
        return score;
    }
    
    //Calls helper function findBestSchedule
    public Map<Course, Section> optimizeSchedule() {
        Map<Course, Section> finalSchedule = new HashMap<>();
        findBestSchedule(0, new ArrayList<>(), new HashMap<>(), 0, finalSchedule);
        return finalSchedule;
    }

    //Print Schedule
    public void printSchedule(Map<Course, Integer> schedule) {
        if (schedule == null) {
            System.out.println("No valid schedule found.");
            return;
        }
        for (Map.Entry<Course, Integer> entry : schedule.entrySet()) {
            System.out.println("Course: " + entry.getKey().name + ", Time: " + entry.getValue());
        }
    }

    //Testing
    public static void main(String[] args) {
        Vector<Section> mathSections = new Vector<>();
        mathSections.add(new Section("M101-1", "Lecture", "Mon", "08:00-09:30", "Room 101", "Prof A"));
        mathSections.add(new Section("M101-2", "Lecture", "Tue", "10:00-11:30", "Room 102", "Prof B"));

        Vector<Section> csSections = new Vector<>();
        csSections.add(new Section("CS102-1", "Lecture", "Wed", "09:00-10:30", "Room 201", "Prof C"));
        csSections.add(new Section("CS102-2", "Lecture", "Thu", "14:00-15:30", "Room 202", "Prof D"));

        Vector<Section> engSections = new Vector<>();
        engSections.add(new Section("E201-1", "Lecture", "Mon", "11:00-12:30", "Room 301", "Prof E"));

        Vector<Course> courses = new Vector<>();
        courses.add(new Course("Math101", "Math Intro", "Basic Math", "3", mathSections));
        courses.add(new Course("CS102", "Intro to CS", "Basics of CS", "4", csSections));
        courses.add(new Course("Eng201", "English Literature", "Intro to English Lit", "3", engSections));

        Map<String, List<String>> preferredSections = new HashMap<>();
        preferredSections.put("Math101", Arrays.asList("M101-2"));
        preferredSections.put("CS102", Arrays.asList("CS102-1"));

        Set<String> unavailableTimes = new HashSet<>(Arrays.asList("08:00-09:30", "14:00-15:30"));

        Map<String, List<String>> preferredProfessors = new HashMap<>();
        preferredProfessors.put("Math101", Arrays.asList("Prof B"));
        preferredProfessors.put("CS102", Arrays.asList("Prof C"));

        ClassScheduler optimizer = new ClassScheduler(courses, preferredSections, unavailableTimes, preferredProfessors);

        Map<Course, Section> bestSchedule = optimizer.optimizeSchedule();
        optimizer.printSchedule(bestSchedule);
    }
}
