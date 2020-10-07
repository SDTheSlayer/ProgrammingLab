package Q2DataModification;

import java.io.*;
import java.util.*;

public class ModificationSystem {
    private static final String STUD_INFO = "./resources/Q2DataModification/stud_info.txt";
    private static final String STUD_INFO_ROLL_NUMBER_SORTED = "./resources/Q2DataModification/stud_info_roll_sorted.txt";
    private static final String STUD_INFO_NAME_SORTED = "./resources/Q2DataModification/stud_info_name_sorted.txt";
    private final Scanner userScanner;

    private Map<String, Student> studentData;
    private List<Update> updateList;

    public ModificationSystem() {
        studentData = new HashMap<String, Student>();
        userScanner = new Scanner(System.in);
        updateList = new ArrayList<Update>();
    }

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

    public void scheduleUpdate() {
        Update update = new Update();
        System.out.println("Enter Teacher's Code: ");
        String teacherCode = userScanner.next();
        if (teacherCode.equals(Teacher.CC) || teacherCode.equals(Teacher.TA1) || teacherCode.equals(Teacher.TA2)) {
            update.setTeacherCode(teacherCode);
        } else {
            System.out.println("Invalid teacher name. Couldn't schedule Update. Try Again!!");
            return;
        }
        System.out.println("Enter Roll Number : ");
        update.setRollNo(userScanner.next());
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
        updateList.add(update);
        System.out.println("Update Scheduled Successfully!");
    }

    public void executePending() {

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

    public void updateRecord(Update update) throws InterruptedException {
        String rollNo = update.getRollNo();
        if (studentData.get(rollNo) == null) {
            System.out.println("The student with Roll No " + rollNo + " doesn't exist.+" +
                    " Data for the student not updated!");
        } else {
            studentData.get(rollNo).editBlocking();
            if (studentData.get(rollNo).getTeacherCode().equals(Teacher.CC) && !update.getTeacherCode().equals(Teacher.CC)) {
                System.out.println(update.getTeacherCode() + " tried to update marks for Roll No " + rollNo +
                        ", however he doesn't have permission! Data for the student not updated!");
            } else {
                Integer marks = studentData.get(rollNo).getMarks() + update.getChangeInMarks();
                studentData.get(rollNo).setMarks(marks);
                studentData.get(rollNo).setTeacherCode(update.getTeacherCode());
                System.out.println("Marks for Roll No " + rollNo + "successfully updated by " + update.getTeacherCode() +
                        " with a modification of " + update.getChangeInMarks() + " marks.");
            }
            studentData.get(rollNo).finishEdit();
        }
    }

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
