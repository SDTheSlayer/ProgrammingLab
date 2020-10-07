package Q2DataModification;

public class Update {
    private String rollNo;
    private Integer changeInMarks;
    private String teacherCode;

    public Update(String rollNo, Integer changeInMarks, String teacherCode) {
        this.rollNo = rollNo;
        this.changeInMarks = changeInMarks;
        this.teacherCode = teacherCode;
    }

    public Update() {
    }

    public String getRollNo() {
        return rollNo;
    }

    public void setRollNo(String rollNo) {
        this.rollNo = rollNo;
    }

    public Integer getChangeInMarks() {
        return changeInMarks;
    }

    public void setChangeInMarks(Integer changeInMarks) {
        this.changeInMarks = changeInMarks;
    }

    public String getTeacherCode() {
        return teacherCode;
    }

    public void setTeacherCode(String teacherCode) {
        this.teacherCode = teacherCode;
    }
}
