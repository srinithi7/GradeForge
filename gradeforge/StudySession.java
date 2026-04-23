package gradeforge;

import java.io.Serializable;

public class StudySession implements Serializable {
    private static final long serialVersionUID = 2L;

    private int sessionId;
    private String subjectCode;
    private String date;
    private double hoursStudied;
    private String topicsCovered;
    private String notes;

    public StudySession(int sessionId, String subjectCode, String date,
                        double hoursStudied, String topicsCovered, String notes) {
        this.sessionId = sessionId;
        this.subjectCode = subjectCode;
        this.date = date;
        this.hoursStudied = hoursStudied;
        this.topicsCovered = topicsCovered;
        this.notes = notes;
    }

    public int getSessionId() { return sessionId; }
    public String getSubjectCode() { return subjectCode; }
    public String getDate() { return date; }
    public double getHoursStudied() { return hoursStudied; }
    public String getTopicsCovered() { return topicsCovered; }
    public String getNotes() { return notes; }

    public void setSessionId(int sessionId) { this.sessionId = sessionId; }
    public void setSubjectCode(String subjectCode) { this.subjectCode = subjectCode; }
    public void setDate(String date) { this.date = date; }
    public void setHoursStudied(double hoursStudied) { this.hoursStudied = hoursStudied; }
    public void setTopicsCovered(String topicsCovered) { this.topicsCovered = topicsCovered; }
    public void setNotes(String notes) { this.notes = notes; }

    public String toString() {
        return "#" + sessionId + " | " + subjectCode + " | " + date
                + " | " + hoursStudied + " hrs | " + topicsCovered;
    }
}
