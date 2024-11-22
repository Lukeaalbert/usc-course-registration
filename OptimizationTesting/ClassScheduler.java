package OptimizationTesting;

import java.util.*;

public class ClassScheduler {
    //Basic Class representing a course
	//Everything is really simple for now
    static class Course {
        String name;
        List<Integer> availableTimes; //I have only checked the starting times listed, and not the duration so I can keep things simple and see if the DP part works well
        List<String> prerequisites;

        Course(String name, List<Integer> availableTimes, List<String> prerequisites) {
            this.name = name;
            this.availableTimes = availableTimes;
            this.prerequisites = prerequisites;
        }
    }

    // Input data
    private List<Course> courses;
    private Map<String, List<Integer>> preferences; //Preferred times per course
    private Set<Integer> unavailableTimes;          //Unavailable time slots
    private Map<String, Boolean> prerequisiteStatus; //Tracks completed prerequisites
    //Still haven't implemented a preference system yet, but we can take things like professor into account

    public ClassScheduler(List<Course> courses, Map<String, List<Integer>> preferences, Set<Integer> unavailableTimes) {
        this.courses = courses;
        this.preferences = preferences;
        this.unavailableTimes = unavailableTimes;
        this.prerequisiteStatus = new HashMap<>();
    }

    //Scoring function
    //Here is where we would check if preferences are matched and decide how to "score" the course
    private int calculateScore(Course course, int time) {
        int score = 0;

        //If the time works + 10
        if (preferences.containsKey(course.name) && preferences.get(course.name).contains(time)) {
            score += 10;
        }

        //If not, - 20
        if (unavailableTimes.contains(time)) {
            score -= 20;
        }

        //If prerequisites not met -30
        for (String prereq : course.prerequisites) {
            if (!prerequisiteStatus.getOrDefault(prereq, false)) {
                score -= 30;
            }
        }

        return score;
    }

    //Dynamic Programming optimization
    public Map<Course, Integer> optimizeSchedule() {
        //DP state: dp[i][t] represents the best score using the first i courses, occupying t time
        int n = courses.size();
        int maxTime = 24; //Assuming we only consider a 24-hour schedule
        int[][] dp = new int[n + 1][maxTime + 1];
        Map<Integer, Map<Course, Integer>>[] scheduleMap = new Map[n + 1];

        //Initialize DP tables and schedule map
        for (int i = 0; i <= n; i++) {
            scheduleMap[i] = new HashMap<>();
        }

        //Iterate through courses
        for (int i = 1; i <= n; i++) {
            Course course = courses.get(i - 1);

            for (int t = 0; t <= maxTime; t++) {
                dp[i][t] = dp[i - 1][t]; //Default to not scheduling this course
                scheduleMap[i].put(t, new HashMap<>(scheduleMap[i - 1].getOrDefault(t, new HashMap<>())));

                for (int time : course.availableTimes) {
                    if (t >= time) {
                        int score = dp[i - 1][t - time] + calculateScore(course, time);

                        if (score > dp[i][t]) {
                            dp[i][t] = score;

                            //Update the schedule for the current time
                            Map<Course, Integer> newSchedule = new HashMap<>(scheduleMap[i - 1].getOrDefault(t - time, new HashMap<>()));
                            newSchedule.put(course, time);
                            scheduleMap[i].put(t, newSchedule);

                            //Update prerequisite status for future courses
                            prerequisiteStatus.put(course.name, true);
                        }
                    }
                }
            }
        }

        //Find the best schedule
        int bestScore = 0;
        int bestTime = 0;

        for (int t = 0; t <= maxTime; t++) {
            if (dp[n][t] > bestScore) {
                bestScore = dp[n][t];
                bestTime = t;
            }
        }

        System.out.println("Best Score: " + bestScore);
        return scheduleMap[n].get(bestTime);
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

    public static void main(String[] args) {
        //Sample input courses
        List<Course> courses = Arrays.asList(
                new Course("Math101", Arrays.asList(8, 10, 12), Collections.emptyList()),
                new Course("CS102", Arrays.asList(10, 14), Arrays.asList("Math101")),
                new Course("Eng201", Arrays.asList(9, 11), Collections.emptyList())
        );

        Map<String, List<Integer>> preferences = new HashMap<>();
        preferences.put("Math101", Arrays.asList(8));
        preferences.put("CS102", Arrays.asList(14));

        Set<Integer> unavailableTimes = new HashSet<>(Arrays.asList(12, 13));

        //Initialize optimizer
        ClassScheduler optimizer = new ClassScheduler(courses, preferences, unavailableTimes);

        //Optimize schedule
        Map<Course, Integer> bestSchedule = optimizer.optimizeSchedule();
        optimizer.printSchedule(bestSchedule);
    }
}
