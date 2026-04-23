package gradeforge;

import java.io.Serializable;

public class Deadline implements Serializable {
    private static final long serialVersionUID = 3L;

    private int deadlineId;
    private String title;
    private String subjectCode;
    private String dueDate;
    private String type;
    private String status;
    private String notes;

    public Deadline(int deadlineId, String title, String subjectCode,
                    String dueDate, String type, String status, String notes) {
        this.deadlineId = deadlineId;
        this.title = title;
        this.subjectCode = subjectCode;
        this.dueDate = dueDate;
        this.type = type;
        this.status = status;
        this.notes = notes;
    }

    public int getDeadlineId() { return deadlineId; }
    public String getTitle() { return title; }
    public String getSubjectCode() { return subjectCode; }
    public String getDueDate() { return dueDate; }
    public String getType() { return type; }
    public String getStatus() { return status; }
    public String getNotes() { return notes; }

    public void setDeadlineId(int deadlineId) { this.deadlineId = deadlineId; }
    public void setTitle(String title) { this.title = title; }
    public void setSubjectCode(String subjectCode) { this.subjectCode = subjectCode; }
    public void setDueDate(String dueDate) { this.dueDate = dueDate; }
    public void setType(String type) { this.type = type; }
    public void setStatus(String status) { this.status = status; }
    public void setNotes(String notes) { this.notes = notes; }

    public String toString() {
        return "[" + deadlineId + "] " + title + " | " + subjectCode
                + " | Due: " + dueDate + " | " + type + " | " + status;
    }
}
