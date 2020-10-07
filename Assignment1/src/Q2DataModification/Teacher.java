package Q2DataModification;

import java.util.ArrayList;
import java.util.List;

public class Teacher extends Thread {
    public static final String CC = "CC";
    public static final String TA1 = "TA1";
    public static final String TA2 = "TA2";

    private ModificationSystem modificationSystem;
    private List<Update> updates;
    private String teacherType;

    public Teacher(ModificationSystem modificationSystem, String teacherType) {
        this.modificationSystem = modificationSystem;
        this.updates = new ArrayList<Update>();
        this.teacherType = teacherType;
        setName(teacherType);
    }

    @Override
    public void run() {
        try {
            for (Update update : updates) {
                modificationSystem.updateRecord(update);
            }
        } catch (InterruptedException e) {
            System.out.println(getName() + "'s execution interrupted !!" + e.toString());
        }
        updates.clear();

    }

    public void addUpdate(Update update) {
        updates.add(update);
    }

}
