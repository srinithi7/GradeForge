package gradeforge;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;

public class DataHandler {

    private static final String SUBJECTS_FILE  = "subjects.dat";
    private static final String SESSIONS_FILE  = "sessions.dat";
    private static final String DEADLINES_FILE = "deadlines.dat";
    private static final String LABEXAMS_FILE  = "labexams.dat";
    private static final String PROJECTS_FILE  = "projects.dat";

    private ArrayList loadList(String filePath) {
        ArrayList list = new ArrayList();
        FileInputStream fis = null;
        ObjectInputStream ois = null;
        try {
            fis = new FileInputStream(filePath);
            ois = new ObjectInputStream(fis);
            list = (ArrayList) ois.readObject();
        } catch (FileNotFoundException e) {
        } catch (IOException e) {
            System.err.println("IO error loading " + filePath + ": " + e.getMessage());
        } catch (ClassNotFoundException e) {
            System.err.println("Class error loading " + filePath + ": " + e.getMessage());
        } finally {
            try { if (ois != null) ois.close(); } catch (IOException e) {}
            try { if (fis != null) fis.close(); } catch (IOException e) {}
        }
        return list;
    }

    private void saveList(ArrayList list, String filePath) {
        FileOutputStream fos = null;
        ObjectOutputStream oos = null;
        try {
            fos = new FileOutputStream(filePath);
            oos = new ObjectOutputStream(fos);
            oos.writeObject(list);
        } catch (IOException e) {
            System.err.println("IO error saving " + filePath + ": " + e.getMessage());
        } finally {
            try { if (oos != null) oos.close(); } catch (IOException e) {}
            try { if (fos != null) fos.close(); } catch (IOException e) {}
        }
    }

    public ArrayList<Subject> getAllSubjects() {
        return (ArrayList<Subject>) loadList(SUBJECTS_FILE);
    }

    public boolean addSubject(Subject s) {
        ArrayList<Subject> list = getAllSubjects();
        for (Subject sub : list) {
            if (sub.getSubjectCode().equalsIgnoreCase(s.getSubjectCode())) {
                return false;
            }
        }
        s.calculateInternalAverage();
        s.calculateExternalRequired();
        list.add(s);
        saveList(list, SUBJECTS_FILE);
        return true;
    }

    public Subject findSubjectByCode(String code) {
        ArrayList<Subject> list = getAllSubjects();
        for (Subject sub : list) {
            if (sub.getSubjectCode().equalsIgnoreCase(code)) {
                return sub;
            }
        }
        return null;
    }

    public boolean updateSubjectMarks(String code, double ca1, double ca2, double assign,
                                       int syllabusPercent, String semExamDate, String examHall) {
        ArrayList<Subject> list = getAllSubjects();
        boolean found = false;
        for (Subject sub : list) {
            if (sub.getSubjectCode().equalsIgnoreCase(code)) {
                sub.setCa1Marks(ca1);
                sub.setCa2Marks(ca2);
                sub.setAssignmentMarks(assign);
                sub.setSyllabusPercent(syllabusPercent);
                sub.setSemExamDate(semExamDate);
                sub.setExamHall(examHall);
                sub.calculateInternalAverage();
                sub.calculateExternalRequired();
                found = true;
                break;
            }
        }
        if (found) saveList(list, SUBJECTS_FILE);
        return found;
    }

    public boolean deleteSubject(String code) {
        ArrayList<Subject> list = getAllSubjects();
        boolean found = false;
        Iterator<Subject> it = list.iterator();
        while (it.hasNext()) {
            Subject sub = it.next();
            if (sub.getSubjectCode().equalsIgnoreCase(code)) {
                it.remove();
                found = true;
                break;
            }
        }
        if (found) saveList(list, SUBJECTS_FILE);
        return found;
    }

    public ArrayList<StudySession> getAllSessions() {
        return (ArrayList<StudySession>) loadList(SESSIONS_FILE);
    }

    public boolean addSession(StudySession s) {
        ArrayList<StudySession> list = getAllSessions();
        int maxId = 0;
        for (StudySession sess : list) {
            if (sess.getSessionId() > maxId) maxId = sess.getSessionId();
        }
        s.setSessionId(maxId + 1);
        list.add(s);
        saveList(list, SESSIONS_FILE);
        return true;
    }

    public ArrayList<StudySession> getSessionsBySubject(String code) {
        ArrayList<StudySession> all = getAllSessions();
        ArrayList<StudySession> filtered = new ArrayList<StudySession>();
        for (StudySession sess : all) {
            if (sess.getSubjectCode().equalsIgnoreCase(code)) {
                filtered.add(sess);
            }
        }
        return filtered;
    }

    public double getTotalHoursBySubject(String code) {
        ArrayList<StudySession> list = getAllSessions();
        double total = 0;
        for (StudySession sess : list) {
            if (sess.getSubjectCode().equalsIgnoreCase(code)) {
                total += sess.getHoursStudied();
            }
        }
        return total;
    }

    public boolean deleteSession(int id) {
        ArrayList<StudySession> list = getAllSessions();
        boolean found = false;
        Iterator<StudySession> it = list.iterator();
        while (it.hasNext()) {
            StudySession sess = it.next();
            if (sess.getSessionId() == id) {
                it.remove();
                found = true;
                break;
            }
        }
        if (found) saveList(list, SESSIONS_FILE);
        return found;
    }

    public ArrayList<Deadline> getAllDeadlines() {
        return (ArrayList<Deadline>) loadList(DEADLINES_FILE);
    }

    public boolean addDeadline(Deadline d) {
        ArrayList<Deadline> list = getAllDeadlines();
        int maxId = 0;
        for (Deadline dl : list) {
            if (dl.getDeadlineId() > maxId) maxId = dl.getDeadlineId();
        }
        d.setDeadlineId(maxId + 1);
        list.add(d);
        saveList(list, DEADLINES_FILE);
        return true;
    }

    public boolean updateDeadlineStatus(int id, String newStatus) {
        ArrayList<Deadline> list = getAllDeadlines();
        boolean found = false;
        for (Deadline dl : list) {
            if (dl.getDeadlineId() == id) {
                dl.setStatus(newStatus);
                found = true;
                break;
            }
        }
        if (found) saveList(list, DEADLINES_FILE);
        return found;
    }

    public ArrayList<Deadline> getPendingDeadlines() {
        ArrayList<Deadline> all = getAllDeadlines();
        ArrayList<Deadline> pending = new ArrayList<Deadline>();
        for (Deadline dl : all) {
            if (dl.getStatus().equals("Pending") || dl.getStatus().equals("Urgent")) {
                pending.add(dl);
            }
        }
        return pending;
    }

    public boolean deleteDeadline(int id) {
        ArrayList<Deadline> list = getAllDeadlines();
        boolean found = false;
        Iterator<Deadline> it = list.iterator();
        while (it.hasNext()) {
            Deadline dl = it.next();
            if (dl.getDeadlineId() == id) {
                it.remove();
                found = true;
                break;
            }
        }
        if (found) saveList(list, DEADLINES_FILE);
        return found;
    }

    public ArrayList<LabExam> getAllLabExams() {
        return (ArrayList<LabExam>) loadList(LABEXAMS_FILE);
    }

    public boolean addLabExam(LabExam l) {
        ArrayList<LabExam> list = getAllLabExams();
        int maxId = 0;
        for (LabExam lab : list) {
            if (lab.getLabId() > maxId) maxId = lab.getLabId();
        }
        l.setLabId(maxId + 1);
        list.add(l);
        saveList(list, LABEXAMS_FILE);
        return true;
    }

    public boolean updateLabExamMarks(int id, double marks, String status) {
        ArrayList<LabExam> list = getAllLabExams();
        boolean found = false;
        for (LabExam lab : list) {
            if (lab.getLabId() == id) {
                lab.setMarksScored(marks);
                lab.setStatus(status);
                found = true;
                break;
            }
        }
        if (found) saveList(list, LABEXAMS_FILE);
        return found;
    }

    public ArrayList<LabExam> getUpcomingLabExams() {
        ArrayList<LabExam> all = getAllLabExams();
        ArrayList<LabExam> upcoming = new ArrayList<LabExam>();
        for (LabExam lab : all) {
            if (lab.getStatus().equals("Upcoming")) {
                upcoming.add(lab);
            }
        }
        return upcoming;
    }

    public boolean deleteLabExam(int id) {
        ArrayList<LabExam> list = getAllLabExams();
        boolean found = false;
        Iterator<LabExam> it = list.iterator();
        while (it.hasNext()) {
            LabExam lab = it.next();
            if (lab.getLabId() == id) {
                it.remove();
                found = true;
                break;
            }
        }
        if (found) saveList(list, LABEXAMS_FILE);
        return found;
    }

    public ArrayList<Project> getAllProjects() {
        return (ArrayList<Project>) loadList(PROJECTS_FILE);
    }

    public boolean addProject(Project p) {
        ArrayList<Project> list = getAllProjects();
        int maxId = 0;
        for (Project proj : list) {
            if (proj.getProjectId() > maxId) maxId = proj.getProjectId();
        }
        p.setProjectId(maxId + 1);
        list.add(p);
        saveList(list, PROJECTS_FILE);
        return true;
    }

    public boolean updateProjectReview(int id, double r1, double r2,
                                        double finalMarks, String nextDate, String status) {
        ArrayList<Project> list = getAllProjects();
        boolean found = false;
        for (Project proj : list) {
            if (proj.getProjectId() == id) {
                proj.setReview1Marks(r1);
                proj.setReview2Marks(r2);
                proj.setFinalMarks(finalMarks);
                proj.setNextReviewDate(nextDate);
                proj.setStatus(status);
                found = true;
                break;
            }
        }
        if (found) saveList(list, PROJECTS_FILE);
        return found;
    }

    public boolean deleteProject(int id) {
        ArrayList<Project> list = getAllProjects();
        boolean found = false;
        Iterator<Project> it = list.iterator();
        while (it.hasNext()) {
            Project proj = it.next();
            if (proj.getProjectId() == id) {
                it.remove();
                found = true;
                break;
            }
        }
        if (found) saveList(list, PROJECTS_FILE);
        return found;
    }

    public double calculateCurrentCGPA() {
        ArrayList<Subject> list = getAllSubjects();
        if (list.isEmpty()) return 0.0;

        Collections.sort(list, new Comparator<Subject>() {
            public int compare(Subject a, Subject b) {
                return a.getSubjectName().compareTo(b.getSubjectName());
            }
        });

        double totalPoints = 0;
        double totalCredits = 0;
        for (Subject sub : list) {
            totalPoints += sub.getTargetGrade() * sub.getCreditHours();
            totalCredits += sub.getCreditHours();
        }
        if (totalCredits == 0) return 0.0;
        return totalPoints / totalCredits;
    }

    public int[] getDashboardStats() {
        int[] stats = new int[4];
        stats[0] = getAllSubjects().size();

        int pendingCount = 0;
        ArrayList<Deadline> dls = getAllDeadlines();
        for (Deadline dl : dls) {
            if (dl.getStatus().equals("Pending") || dl.getStatus().equals("Urgent")) {
                pendingCount++;
            }
        }
        stats[1] = pendingCount;
        stats[2] = getUpcomingLabExams().size();

        int active = 0;
        ArrayList<Project> projs = getAllProjects();
        for (Project proj : projs) {
            if (proj.getStatus().equals("Active")) active++;
        }
        stats[3] = active;
        return stats;
    }
}
