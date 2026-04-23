package gradeforge;

import java.io.Serializable;

public class Project implements Serializable {
    private static final long serialVersionUID = 5L;

    private int projectId;
    private String projectTitle;
    private String teamMembers;
    private String guideName;
    private double review1Marks;
    private double review2Marks;
    private double finalMarks;
    private String nextReviewDate;
    private String status;
    private String techStack;
    private String notes;

    public Project(int projectId, String projectTitle, String teamMembers,
                   String guideName, double review1Marks, double review2Marks,
                   double finalMarks, String nextReviewDate, String status,
                   String techStack, String notes) {
        this.projectId = projectId;
        this.projectTitle = projectTitle;
        this.teamMembers = teamMembers;
        this.guideName = guideName;
        this.review1Marks = review1Marks;
        this.review2Marks = review2Marks;
        this.finalMarks = finalMarks;
        this.nextReviewDate = nextReviewDate;
        this.status = status;
        this.techStack = techStack;
        this.notes = notes;
    }

    public int getProjectId() { return projectId; }
    public String getProjectTitle() { return projectTitle; }
    public String getTeamMembers() { return teamMembers; }
    public String getGuideName() { return guideName; }
    public double getReview1Marks() { return review1Marks; }
    public double getReview2Marks() { return review2Marks; }
    public double getFinalMarks() { return finalMarks; }
    public String getNextReviewDate() { return nextReviewDate; }
    public String getStatus() { return status; }
    public String getTechStack() { return techStack; }
    public String getNotes() { return notes; }

    public void setProjectId(int projectId) { this.projectId = projectId; }
    public void setProjectTitle(String projectTitle) { this.projectTitle = projectTitle; }
    public void setTeamMembers(String teamMembers) { this.teamMembers = teamMembers; }
    public void setGuideName(String guideName) { this.guideName = guideName; }
    public void setReview1Marks(double review1Marks) { this.review1Marks = review1Marks; }
    public void setReview2Marks(double review2Marks) { this.review2Marks = review2Marks; }
    public void setFinalMarks(double finalMarks) { this.finalMarks = finalMarks; }
    public void setNextReviewDate(String nextReviewDate) { this.nextReviewDate = nextReviewDate; }
    public void setStatus(String status) { this.status = status; }
    public void setTechStack(String techStack) { this.techStack = techStack; }
    public void setNotes(String notes) { this.notes = notes; }

    public String toString() {
        return "#" + projectId + " | " + projectTitle + " | Guide: " + guideName
                + " | R1: " + review1Marks + " R2: " + review2Marks
                + " | " + status;
    }
}
