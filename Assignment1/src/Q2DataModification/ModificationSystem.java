package Q2DataModification;

import java.io.*;
import java.util.*;

/**
 * A modification system for storing the updates scheduled and executing them together.
 */
public class ModificationSystem {

    /**
     * Constants for storing file locations
     */
    private static final String STUD_INFO = "./resources/Q2DataModification/stud_info.txt";
    private static final String STUD_INFO_ROLL_NUMBER_SORTED = "./resources/Q2DataModification/stud_info_roll_sorted.txt";
    private static final String STUD_INFO_NAME_SORTED = "./resources/Q2DataModification/stud_info_name_sorted.txt";
    private final Scanner userScanner;

    /**
     * Storing the entire student info as student objects mapped by their roll No
     */
    private Map<String, Student> studentData;
    /**
     * Storing the Scheduled updates for all Teachers
     */
    private List<Update> updateList;

    public ModificationSystem() {
        studentData = new HashMap<String, Student>();
        userScanner = new Scanner(System.in);
        updateList = new ArrayList<Update>();
    }

    /**
     * Read the Student Data from file into the studentData Map
     */
    public void readDataFromFile() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(STUD_INFO));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                Student student = new Student(data[0], data[1], data[2], Integer.parseInt(data[3].trim()), data[4]);
                studentData.put(student.getRollNo(), student);
            }
        } catch (IOException e) {
            System.out.println("Couldn't read from file!! " + e.toString());
        }
    }

    /**
     * Schedule an update to one student by a teacher.
     */
    public void scheduleUpdate() {
        Update update = new Update();

        // Scans the Teacher Code and verifies that it is a valid teacher.
        System.out.println("Enter Teacher's Code: ");
        String teacherCode = userScanner.next();
        if (teacherCode.equals(Teacher.CC) || teacherCode.equals(Teacher.TA1) || teacherCode.equals(Teacher.TA2)) {
            update.setTeacherCode(teacherCode);
        } else {
            System.out.println("Invalid teacher name. Couldn't schedule Update. Try Again!!");
            return;
        }

        // Scans the Roll number of the student.
        System.out.println("Enter Roll Number : ");
        update.setRollNo(userScanner.next());

        // Scans whether to increase or decrese marks adn by how much
        System.out.println("Chose how to update Marks \n" +
                "   1] Increase marks \n" +
                "   2] Decrease marks ");
        int choice = userScanner.nextInt();
        if (choice == 1) {
            System.out.println("Increase the marks by: ");
            int change = userScanner.nextInt();
            update.setChangeInMarks(change);
        } else if (choice == 2) {
            System.out.println("Decrease the marks by : ");
            int change = userScanner.nextInt();
            update.setChangeInMarks(-1 * change);
        } else {
            System.out.println("Invalid Option given. Couldn't schedule Update. Try Again!!");
            return;
        }

        //Adds the update in the scheduled update list
        updateList.add(update);
        System.out.println("Update Scheduled Successfully!");
    }


    /**
     *  Executes all pending updates and generates the 3 data files
     */
    public void executePending() {

        // Creates threads for each Teacher and provides it with the corresponding updates
        Teacher courseCoord = new Teacher(this, Teacher.CC);
        Teacher teachingAss1 = new Teacher(this, Teacher.TA1);
        Teacher teachingAss2 = new Teacher(this, Teacher.TA2);
        for (Update update : updateList) {
            switch (update.getTeacherCode()) {
                case Teacher.TA1:
                    teachingAss1.addUpdate(update);
                    break;
                case Teacher.TA2:
                    teachingAss2.addUpdate(update);
                    break;
                case Teacher.CC:
                    courseCoord.addUpdate(update);
                    break;
            }
        }
        updateList.clear();

        // First Starts the CC thread and waits for it to finish as it needs to perform its updates first.
        // And then the other threads.
        try {
            courseCoord.start();
            courseCoord.join();
            teachingAss1.start();
            teachingAss2.start();
            teachingAss1.join();
            teachingAss2.join();
        } catch (InterruptedException e) {
            System.out.println("Execution interrupted !!" + e.toString());
        }
        writeToFiles();
    }

    /**
     * Update fucntion used by teacher threads to update a particular record
     * @param update The scheduled update
     * @throws InterruptedException if while waiting for permission to edit it is interrupted.
     */
    public void updateRecord(Update update) throws InterruptedException {
        String rollNo = update.getRollNo();
        // Checks validity of roll number
        if (studentData.get(rollNo) == null) {
            System.out.println("The student with Roll No " + rollNo + " doesn't exist.+" +
                    " Data for the student not updated!");
        }
        else {
            // Waits for lock to start editing
            studentData.get(rollNo).editBlocking();

            // Checks if the teacher as the required permission to edit
            if (studentData.get(rollNo).getTeacherCode().equals(Teacher.CC) && !update.getTeacherCode().equals(Teacher.CC)) {
                System.out.println(update.getTeacherCode() + " tried to update marks for Roll No " + rollNo +
                        ", however he doesn't have permission! Data for the student not updated!");
            } else {
                Integer marks = studentData.get(rollNo).getMarks() + update.getChangeInMarks();
                studentData.get(rollNo).setMarks(marks);
                studentData.get(rollNo).setTeacherCode(update.getTeacherCode());
                System.out.println("Marks for Roll No " + rollNo + " successfully updated by " + update.getTeacherCode() +
                        " with a modification of " + update.getChangeInMarks() + " marks. Current Marks: " + marks);
            }

            // Release lock for editing
            studentData.get(rollNo).finishEdit();
        }
    }

    /**
     * Write the Data to files sorted int the specified order
     */
    private void writeToFiles() {
        try (BufferedWriter originalWriter = new BufferedWriter(new FileWriter(STUD_INFO))) {
            for (Student student : studentData.values()) {
                originalWriter.append(student.fileString());
                originalWriter.append('\n');
            }
        } catch (IOException e) {
            System.out.println("Error in writing to Original File !!" + e.toString());
        }

        try (BufferedWriter rollSortedWriter = new BufferedWriter(new FileWriter(STUD_INFO_ROLL_NUMBER_SORTED))) {
            List<String> sortedKeys = new ArrayList<String>(studentData.keySet());
            Collections.sort(sortedKeys);
            for (String key : sortedKeys) {
                rollSortedWriter.append(studentData.get(key).fileString());
                rollSortedWriter.append('\n');
            }
        } catch (IOException e) {
            System.out.println("Error in writing to Roll Sorted File !!" + e.toString());
        }

        try (BufferedWriter nameSortedWriter = new BufferedWriter(new FileWriter(STUD_INFO_NAME_SORTED))) {
            List<Student> sortedNames = new ArrayList<Student>(studentData.values());
            sortedNames.sort(Comparator.comparing(Student::getName));
            for (Student student : sortedNames) {
                nameSortedWriter.append(student.fileString());
                nameSortedWriter.append('\n');
            }
        } catch (IOException e) {
            System.out.println("Error in writing to Name Sorted File !!" + e.toString());
        }
    }

}
