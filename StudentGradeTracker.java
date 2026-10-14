import java.util.ArrayList;
import java.util.Scanner;
class Student {
    private String name;
    private int rollNumber;
    private ArrayList<Double> grades;

    public Student(String name, int rollNumber) {
        this.name = name;
        this.rollNumber = rollNumber;
        this.grades = new ArrayList<>();
    }

    public void addGrade(double grade) {
        grades.add(grade);
    }

    public String getName() {
        return name;
    }

    public int getRollNumber() {
        return rollNumber;
    }

    public ArrayList<Double> getGrades() {
        return grades;
    }

    // Calculate average for this specific student
    public double getAverage() {
        if (grades.isEmpty()) return 0.0;
        double sum = 0;
        for (double g : grades) {
            sum += g;
        }
        return sum / grades.size();
    }

    // Find highest score for this student
    public double getHighest() {
        if (grades.isEmpty()) return 0.0;
        double max = grades.get(0);
        for (double g : grades) {
            if (g > max) max = g;
        }
        return max;
    }

    // Find lowest score for this student
    public double getLowest() {
        if (grades.isEmpty()) return 0.0;
        double min = grades.get(0);
        for (double g : grades) {
            if (g < min) min = g;
        }
        return min;
    }
}

public class StudentGradeTracker {
    private static ArrayList<Student> studentList = new ArrayList<>();
    private static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("==========================================");
        System.out.println("       STUDENT GRADE TRACKER SYSTEM       ");
        System.out.println("==========================================");

        boolean running = true;
        while (running) {
            System.out.println("\n1. Add New Student & Grades");
            System.out.println("2. View Summary Report of All Students");
            System.out.println("3. View Overall Class Statistics");
            System.out.println("4. Exit");
            System.out.print("Enter your choice: ");

            int choice;
            try {
                choice = Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input! Please enter a number between 1 and 4.");
                continue;
            }

            switch (choice) {
                case 1:
                    addStudent();
                    break;
                case 2:
                    displaySummaryReport();
                    break;
                case 3:
                    displayClassStats();
                    break;
                case 4:
                    System.out.println("Exiting program... Goodbye!");
                    running = false;
                    break;
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
        sc.close();
    }

    private static void addStudent() {
        System.out.print("\nEnter Student Name: ");
        String name = sc.nextLine();

        System.out.print("Enter Roll Number: ");
        int roll;
        try {
            roll = Integer.parseInt(sc.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid roll number! Returning to menu.");
            return;
        }

        Student student = new Student(name, roll);

        System.out.print("How many subject grades do you want to enter for " + name + "? ");
        int count;
        try {
            count = Integer.parseInt(sc.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid number!");
            return;
        }

        for (int i = 1; i <= count; i++) {
            while (true) {
                System.out.print("  Enter grade for Subject " + i + " (0 - 100): ");
                try {
                    double grade = Double.parseDouble(sc.nextLine());
                    if (grade >= 0 && grade <= 100) {
                        student.addGrade(grade);
                        break;
                    } else {
                        System.out.println("  Please enter a valid marks value between 0 and 100.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("  Invalid input! Please enter a numeric grade.");
                }
            }
        }

        studentList.add(student);
        System.out.println("Student record added successfully!");
    }

    private static void displaySummaryReport() {
        if (studentList.isEmpty()) {
            System.out.println("\nNo student records found yet. Add some students first!");
            return;
        }

        System.out.println("\n-------------------------------------------------------------------------");
        System.out.printf("%-10s %-20s %-12s %-12s %-12s\n", "Roll No", "Student Name", "Average", "Highest", "Lowest");
        System.out.println("-------------------------------------------------------------------------");

        for (Student s : studentList) {
            System.out.printf("%-10d %-20s %-12.2f %-12.2f %-12.2f\n",
                    s.getRollNumber(),
                    s.getName(),
                    s.getAverage(),
                    s.getHighest(),
                    s.getLowest());
        }
        System.out.println("-------------------------------------------------------------------------");
    }

    private static void displayClassStats() {
        if (studentList.isEmpty()) {
            System.out.println("\nNo data available to calculate class statistics.");
            return;
        }

        double totalSum = 0;
        int totalGradesCount = 0;
        double overallHighest = -1;
        double overallLowest = 101;
        String topStudent = "";
        String lowStudent = "";

        for (Student s : studentList) {
            for (double g : s.getGrades()) {
                totalSum += g;
                totalGradesCount++;

                if (g > overallHighest) {
                    overallHighest = g;
                    topStudent = s.getName();
                }
                if (g < overallLowest) {
                    overallLowest = g;
                    lowStudent = s.getName();
                }
            }
        }

        if (totalGradesCount == 0) {
            System.out.println("\nNo grades entered yet.");
            return;
        }

        double classAverage = totalSum / totalGradesCount;
        System.out.println("\n=== OVERALL CLASS STATISTICS ===");
        System.out.println("Total Students : " + studentList.size());
        System.out.printf("Class Average  : %.2f\n", classAverage);
        System.out.printf("Highest Score  : %.2f (by %s)\n", overallHighest, topStudent);
        System.out.printf("Lowest Score   : %.2f (by %s)\n", overallLowest, lowStudent);
    }
}