package Q2DataModification;

import java.util.concurrent.Semaphore;

/**
 * Class for storing student data in the memory. Contains variables based on columns in the stud file.
 * Also contains a Semaphore for exclusively editing one Students entry.
 */
public class Student {
    private String rollNo;
    private String name;
    private String mailId;
    private Integer marks;
    private String teacherCode;
    private Semaphore lock;

    /**
     * Takes basic input for student and sets semaphore to allow one thread to edit.
     */
    public Student(String rollNo, String name, String mailId, Integer marks, String teacherCode) {
        this.rollNo = rollNo;
        this.name = name;
        this.mailId = mailId;
        this.marks = marks;
        this.teacherCode = teacherCode;
        this.lock = new Semaphore(1);
    }

    /**
     * Function used for printing to file
     */
    public String fileString() {
        return rollNo + "," + name + "," + mailId + "," + marks.toString() + "," + teacherCode;
    }

    public Student() {
        this.lock = new Semaphore(1);
    }

    public void editBlocking() throws InterruptedException {
        lock.acquire();
    }

    public void finishEdit() {
        lock.release();
    }

    public String getRollNo() {
        return rollNo;
    }

    public void setRollNo(String rollNo) {
        this.rollNo = rollNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMailId() {
        return mailId;
    }

    public void setMailId(String mailId) {
        this.mailId = mailId;
    }

    public Integer getMarks() {
        return marks;
    }

    public void setMarks(Integer marks) {
        this.marks = marks;
    }

    public String getTeacherCode() {
        return teacherCode;
    }

    public void setTeacherCode(String teacherCode) {
        this.teacherCode = teacherCode;
    }


}
