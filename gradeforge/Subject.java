package gradeforge;

import java.io.Serializable;

public class Subject implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String subjectCode;
    private String subjectName;
    private int creditHours;
    private double ca1Marks;
    private double ca2Marks;
    private double assignmentMarks;
    private double internalAverage;
    private double targetGrade;
    private double externalRequired;
    private int syllabusPercent;
    private String semExamDate;
    private String examHall;

    public Subject(String subjectCode, String subjectName, int creditHours,
            double ca1Marks, double ca2Marks, double assignmentMarks, double targetGrade,
            int syllabusPercent, String semExamDate, String examHall) {
        this.subjectCode = subjectCode;
        this.subjectName = subjectName;
        this.creditHours = creditHours;
        this.ca1Marks = ca1Marks;
        this.ca2Marks = ca2Marks;
        this.assignmentMarks = assignmentMarks;
        this.targetGrade = targetGrade;
        this.syllabusPercent = syllabusPercent;
        this.semExamDate = semExamDate;
        this.examHall = examHall;
        calculateInternalAverage();
        calculateExternalRequired();
    }

    public double calculateInternalAverage() {
        internalAverage = ca1Marks + ca2Marks + assignmentMarks;
        return internalAverage;
    }

    public double calculateExternalRequired() {
        double targetTotal = 0;
        if (targetGrade == 10) targetTotal = 60;
        else if (targetGrade == 9) targetTotal = 54;
        else if (targetGrade == 8) targetTotal = 48;
        else if (targetGrade == 7) targetTotal = 42;
        else if (targetGrade == 6) targetTotal = 36;
        
        externalRequired = targetTotal - internalAverage;
        if (externalRequired < 0) externalRequired = 0;
        if (externalRequired > 60) externalRequired = 60;
        return externalRequired;
    }

    public String getSubjectCode() { return subjectCode; }
    public void setSubjectCode(String subjectCode) { this.subjectCode = subjectCode; }
    
    public String getSubjectName() { return subjectName; }
    public void setSubjectName(String subjectName) { this.subjectName = subjectName; }
    
    public int getCreditHours() { return creditHours; }
    public void setCreditHours(int creditHours) { this.creditHours = creditHours; }
    
    public double getCa1Marks() { return ca1Marks; }
    public void setCa1Marks(double ca1Marks) { this.ca1Marks = ca1Marks; }
    
    public double getCa2Marks() { return ca2Marks; }
    public void setCa2Marks(double ca2Marks) { this.ca2Marks = ca2Marks; }
    
    public double getAssignmentMarks() { return assignmentMarks; }
    public void setAssignmentMarks(double assignmentMarks) { this.assignmentMarks = assignmentMarks; }
    
    public double getInternalAverage() { return internalAverage; }
    public void setInternalAverage(double internalAverage) { this.internalAverage = internalAverage; }
    
    public double getTargetGrade() { return targetGrade; }
    public void setTargetGrade(double targetGrade) { this.targetGrade = targetGrade; }
    
    public double getExternalRequired() { return externalRequired; }
    public void setExternalRequired(double externalRequired) { this.externalRequired = externalRequired; }
    
    public int getSyllabusPercent() { return syllabusPercent; }
    public void setSyllabusPercent(int syllabusPercent) { this.syllabusPercent = syllabusPercent; }
    
    public String getSemExamDate() { return semExamDate; }
    public void setSemExamDate(String semExamDate) { this.semExamDate = semExamDate; }
    
    public String getExamHall() { return examHall; }
    public void setExamHall(String examHall) { this.examHall = examHall; }

    @Override
    public String toString() {
        return subjectCode + " — " + subjectName + "\n" +
               "Credits: " + creditHours + " | Target: " + targetGrade + "\n" +
               "CA1: " + ca1Marks + " | CA2: " + ca2Marks + " | Assignment: " + assignmentMarks + "\n" +
               "Internal (40): " + internalAverage + "\n" +
               "External Needed: " + externalRequired + "/60\n" +
               "Syllabus: " + syllabusPercent + "%\n" +
               "Exam: " + semExamDate + " | " + examHall;
    }
}
