package Q2DataModification;

import java.util.ArrayList;
import java.util.List;

/**
 * Runnable Thread executing the updates of a particular teacher.
 */
public class Teacher extends Thread {
    /**
     * Constants for representing different teachers.
     */
    public static final String CC = "CC";
    public static final String TA1 = "TA1";
    public static final String TA2 = "TA2";

    /**
     * Storing the Parent modification system along with the list of updates and teacher code.
     */
    private ModificationSystem modificationSystem;
    private List<Update> updates;
    private String teacherType;

    public Teacher(ModificationSystem modificationSystem, String teacherType) {
        this.modificationSystem = modificationSystem;
        this.updates = new ArrayList<Update>();
        this.teacherType = teacherType;
        setName(teacherType);
    }

    /**
     * Try to execute all the updates scheduled by this teacher
     */
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

    /**
     * Add updates to the list of scheduled updates
     */
    public void addUpdate(Update update) {
        updates.add(update);
    }

}
