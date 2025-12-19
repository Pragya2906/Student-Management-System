import java.util.*;
class ConsoleColors { // Fixed: was inconsistent spelling
    public static final String RESET = "\033[0m";

    public static final String RED = "\033[0;31m";
    public static final String GREEN = "\033[0;32m";
    public static final String YELLOW = "\033[0;33m";
    public static final String BLUE = "\033[0;34m";
    public static final String PURPLE = "\033[0;35m";
    public static final String CYAN = "\033[0;36m";

    public static final String YELLOW_BOLD = "\033[1;33m";
    public static final String BLUE_BOLD = "\033[1;34m";
    public static final String WHITE_BOLD = "\033[1;37m";
}

enum StudentStatus {
    Active("Currently Enrolled"),
    Graduated("Completed"),
    Suspended("Temporarily Suspended");

    private final String description;
    StudentStatus(String description) {
        this.description = description;
    }
    String getDescription() {
        return description;
    }
}

enum GradeLevel {
    Freshman(1), Sophomore(2), Pre_Final(3), Final(4);

    private final int year;
    GradeLevel(int year) {
        this.year = year;
    }
    int getYear() {
        return year;
    }
}

class Student {
    private String name;
    private int id;
    private StudentStatus status;
    private GradeLevel level;
    private double gpa;

    public Student(String name, int id, GradeLevel level) {
        this.name = name;
        this.id = id;
        this.status = StudentStatus.Active;
        this.level = level;
        this.gpa = 0.0;
    }

    public String getName() {
        return name;
    }
    public int getId() {
        return id;
    }
    public StudentStatus getStatus() {
        return status;
    }
    public GradeLevel getLevel() {
        return level;
    }
    public double getGpa() {
        return gpa;
    }
    public void setStatus(StudentStatus status) {
        this.status = status;
    }
    public void setLevel(GradeLevel level) {
        this.level = level;
    }
    public void setGpa(double gpa) {
        this.gpa = gpa;
    }

    public void displayInfo() {
        System.out.println(ConsoleColors.PURPLE + " Student Information " + ConsoleColors.RESET);
        System.out.println(ConsoleColors.CYAN + "Name    : " + ConsoleColors.RESET + name);
        System.out.println(ConsoleColors.CYAN + "ID      : " + ConsoleColors.RESET + id);
        System.out.println(ConsoleColors.CYAN + "Level   : " + ConsoleColors.RESET + level + " (Year: " + level.getYear() + ")");
        System.out.println(ConsoleColors.CYAN + "Status  : " + ConsoleColors.RESET + status + " (" + status.getDescription() + ")");
        System.out.println(ConsoleColors.CYAN + "GPA     : " + ConsoleColors.RESET + String.format("%.2f", gpa));
    }
}

public class StudentManagementSystem {

    private static ArrayList<Student> students = new ArrayList<>();
    private static Scanner scanner = new Scanner(System.in);
    private static int nextId = 1001;

    public static void main(String[] args) {
        System.out.println(ConsoleColors.BLUE_BOLD + "=======================================");
        System.out.println("|                                     |");
        System.out.println("|  Welcome to the Student Management  |");
        System.out.println("|            System (SMS)             |");
        System.out.println("|                                     |");
        System.out.println("=======================================" + ConsoleColors.RESET);

        addSampleData();

        while (true) {
            showMenu();
            int choice = -1;
            try {
                choice = scanner.nextInt();
            } catch (InputMismatchException e) {
                System.out.println(ConsoleColors.RED + "[ERROR] Invalid Input. Please enter a number" + ConsoleColors.RESET);
                scanner.next();
                continue;
            } finally {
                scanner.nextLine();
            }

            switch (choice) {
                case 1: addStudent(); break;
                case 2: viewAllStudent(); break;
                case 3: searchStudent(); break;
                case 4: updateGpa(); break;
                case 5: promoteStudent(); break;
                case 6: deleteStudent(); break;
                case 7:
                    System.out.println(ConsoleColors.CYAN + "Thanks for using Student Management System!" + ConsoleColors.RESET);
                    return;
                default:
                    System.out.println(ConsoleColors.RED + "Invalid choice! Try again." + ConsoleColors.RESET);
            }
        }
    }

    private static void showMenu(){
        System.out.println(ConsoleColors.YELLOW_BOLD + " MAIN MENU" + ConsoleColors.RESET);
        System.out.println(ConsoleColors.WHITE_BOLD + "1." + ConsoleColors.RESET + " Add New Student");
        System.out.println(ConsoleColors.WHITE_BOLD + "2." + ConsoleColors.RESET + " View All Students");
        System.out.println(ConsoleColors.WHITE_BOLD + "3." + ConsoleColors.RESET + " Search Student");
        System.out.println(ConsoleColors.WHITE_BOLD + "4." + ConsoleColors.RESET + " Update GPA");
        System.out.println(ConsoleColors.WHITE_BOLD + "5." + ConsoleColors.RESET + " Promote Student Level");
        System.out.println(ConsoleColors.WHITE_BOLD + "6." + ConsoleColors.RESET + " Delete Student");
        System.out.println(ConsoleColors.WHITE_BOLD + "7." + ConsoleColors.RESET + " Exit");
        System.out.print(ConsoleColors.CYAN + "Enter choice: " + ConsoleColors.RESET);
    }

    private static void addStudent() {
        System.out.println("\n" + ConsoleColors.PURPLE + "Add New Student" + ConsoleColors.RESET);
        System.out.print(ConsoleColors.CYAN + "Enter Student Name: " + ConsoleColors.RESET);
        String name = scanner.nextLine();

        System.out.println(ConsoleColors.YELLOW + "Select Grade Level:" + ConsoleColors.RESET);
        System.out.println("1. Freshman  2. Sophomore  3. Pre-Final  4. Final");
        int levelChoice = scanner.nextInt();

        if (levelChoice < 1 || levelChoice > 4) {
            System.out.println(ConsoleColors.RED + "Invalid level choice!" + ConsoleColors.RESET);
            return;
        }
        GradeLevel level = GradeLevel.values()[levelChoice - 1];

        // Check for duplicates
        if (isStudentExists(name, level)) {
            System.out.println(ConsoleColors.YELLOW + "Warning: " + name + " already exists in " + level + " level!" + ConsoleColors.RESET);
            System.out.println("1. Add Anyway (different person)");
            System.out.println("2. Cancel");
            System.out.print("Enter Choice: ");
            int choice = scanner.nextInt();

            if (choice != 1) {
                System.out.println(ConsoleColors.CYAN + "Student addition cancelled" + ConsoleColors.RESET);
                return;
            }
        }

        Student student = new Student(name, nextId++, level);
        students.add(student);
        System.out.println(ConsoleColors.GREEN + "Student added successfully! ID: " + student.getId() + ConsoleColors.RESET);
    }

    private static boolean isStudentExists(String name, GradeLevel level) {
        for (Student student : students) {
            if (student.getName().equalsIgnoreCase(name) && student.getLevel() == level) {
                return true;
            }
        }
        return false;
    }

    private static void viewAllStudent() {
        if (students.isEmpty()) {
            System.out.println(ConsoleColors.CYAN + "No students found!" + ConsoleColors.RESET);
            return;
        }
        
        System.out.println("\n" + ConsoleColors.PURPLE + " ALL STUDENTS " + ConsoleColors.RESET);
        for (Student student : students) {
            String statusColor = student.getStatus() == StudentStatus.Graduated ? ConsoleColors.GREEN : ConsoleColors.CYAN;
            System.out.println(ConsoleColors.WHITE_BOLD + student.getId() + "." + ConsoleColors.RESET + " " + student.getName() +  " (" + ConsoleColors.YELLOW + student.getLevel() + ConsoleColors.RESET + ") - " + "GPA: " + ConsoleColors.BLUE + student.getGpa() + ConsoleColors.RESET + " - " + "Status: " + statusColor + student.getStatus() + ConsoleColors.RESET);
        }
    }

    private static void searchStudent() {
        System.out.println("\n" + ConsoleColors.PURPLE + " Search for a Student " + ConsoleColors.RESET);
        System.out.print(ConsoleColors.CYAN + "Enter student ID: " + ConsoleColors.RESET);
        int id = scanner.nextInt();

        Student student = findStudentById(id);
        if (student != null) {
            student.displayInfo();
        } else {
            System.out.println(ConsoleColors.RED + "Student not found!" + ConsoleColors.RESET);
        }
    }

    private static void updateGpa() {
        System.out.println("\n" + ConsoleColors.PURPLE + " Update GPA" + ConsoleColors.RESET);
        System.out.print(ConsoleColors.CYAN + "Enter Student ID: " + ConsoleColors.RESET);
        int id = scanner.nextInt();

        Student student = findStudentById(id);
        if (student != null) {
            System.out.print(ConsoleColors.CYAN + "Enter new GPA (0.0 - 10.0): " + ConsoleColors.RESET);
            double gpa = scanner.nextDouble();
            
            if (gpa >= 0.0 && gpa <= 10.0) {
                student.setGpa(gpa);
                System.out.println(ConsoleColors.GREEN + "GPA updated successfully!" + ConsoleColors.RESET);
            } else {
                System.out.println(ConsoleColors.RED + "Invalid GPA! Please enter value between 0.0 and 10.0" + ConsoleColors.RESET);
            }
        } else {
            System.out.println(ConsoleColors.RED + "Student not found!" + ConsoleColors.RESET);
        }
    }

    private static void promoteStudent() {
        System.out.println("\n" + ConsoleColors.PURPLE + " Promote Student " + ConsoleColors.RESET);
        System.out.print(ConsoleColors.CYAN + "Enter student ID: " + ConsoleColors.RESET);
        int id = scanner.nextInt();

        Student student = findStudentById(id);
        if (student != null) {
            GradeLevel currentLevel = student.getLevel();

            switch (currentLevel) {
                case Freshman:
                    student.setLevel(GradeLevel.Sophomore);
                    System.out.println(ConsoleColors.GREEN + "Student promoted to Sophomore!" + ConsoleColors.RESET);
                    break;
                case Sophomore:
                    student.setLevel(GradeLevel.Pre_Final);
                    System.out.println(ConsoleColors.GREEN + "Student promoted to Pre-Final year!" + ConsoleColors.RESET);
                    break;
                case Pre_Final:
                    student.setLevel(GradeLevel.Final);
                    System.out.println(ConsoleColors.GREEN + "Student promoted to Final Year!" + ConsoleColors.RESET);
                    break;
                case Final:
                    student.setStatus(StudentStatus.Graduated);
                    System.out.println(ConsoleColors.GREEN + "🎉 Student has graduated! Status changed to Graduated 🎉" + ConsoleColors.RESET);
                    break;
                default:
                    System.out.println(ConsoleColors.RED + "Error: Unknown grade level: " + currentLevel);
                    System.out.println("Cannot promote student. Please contact administrator." + ConsoleColors.RESET);
                    break;
            }
        } else {
            System.out.println(ConsoleColors.RED + "Student not found!" + ConsoleColors.RESET);
        }
    }

    private static void deleteStudent() {
        System.out.println("\n" + ConsoleColors.PURPLE + "Delete Student" + ConsoleColors.RESET);
        System.out.print(ConsoleColors.CYAN + "Enter the Student ID to delete: " + ConsoleColors.RESET);
        int id = scanner.nextInt();

        Student student = findStudentById(id);
        if (student != null) {
            students.remove(student);
            System.out.println(ConsoleColors.GREEN + "Student " + student.getName() + " (ID: " + id + ") deleted successfully!" + ConsoleColors.RESET);
        } else {
            System.out.println(ConsoleColors.RED + "Student not found!" + ConsoleColors.RESET);
        }
    }

    private static Student findStudentById(int id) {
        for (Student student : students) {
            if (student.getId() == id) {
                return student;
            }
        }
        return null;
    }

    private static void addSampleData() {
        students.add(new Student("Aman Gupta", nextId++, GradeLevel.Freshman));
        students.add(new Student("Pragya Lachhwani", nextId++, GradeLevel.Sophomore));
        students.add(new Student("Ayushi Saxena", nextId++, GradeLevel.Pre_Final));
        students.add(new Student("Rema Thakur", nextId++, GradeLevel.Final));

        students.get(0).setGpa(8.5);
        students.get(1).setGpa(7.9);
        students.get(2).setGpa(6.0);

        System.out.println(ConsoleColors.CYAN + "Current students in the system: " + students.size() + ConsoleColors.RESET);
    }
}