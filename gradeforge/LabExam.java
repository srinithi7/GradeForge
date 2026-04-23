package gradeforge;

import java.io.Serializable;

public class LabExam implements Serializable {
    private static final long serialVersionUID = 4L;

    private int labId;
    private String subjectCode;
    private String subjectName;
    private String examDate;
    private double marksScored;
    private double maxMarks;
    private String status;
    private String notes;

    public LabExam(int labId, String subjectCode, String subjectName,
                   String examDate, double marksScored, double maxMarks,
                   String status, String notes) {
        this.labId = labId;
        this.subjectCode = subjectCode;
        this.subjectName = subjectName;
        this.examDate = examDate;
        this.marksScored = marksScored;
        this.maxMarks = maxMarks;
        this.status = status;
        this.notes = notes;
    }

    public int getLabId() { return labId; }
    public String getSubjectCode() { return subjectCode; }
    public String getSubjectName() { return subjectName; }
    public String getExamDate() { return examDate; }
    public double getMarksScored() { return marksScored; }
    public double getMaxMarks() { return maxMarks; }
    public String getStatus() { return status; }
    public String getNotes() { return notes; }

    public void setLabId(int labId) { this.labId = labId; }
    public void setSubjectCode(String subjectCode) { this.subjectCode = subjectCode; }
    public void setSubjectName(String subjectName) { this.subjectName = subjectName; }
    public void setExamDate(String examDate) { this.examDate = examDate; }
    public void setMarksScored(double marksScored) { this.marksScored = marksScored; }
    public void setMaxMarks(double maxMarks) { this.maxMarks = maxMarks; }
    public void setStatus(String status) { this.status = status; }
    public void setNotes(String notes) { this.notes = notes; }

    public String toString() {
        return "#" + labId + " | " + subjectName + " | " + subjectCode
                + " | " + examDate + " | " + marksScored + "/" + maxMarks
                + " | " + status;
    }
}
