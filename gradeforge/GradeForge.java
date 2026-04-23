package gradeforge;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class GradeForge extends Frame {

    static final Color CRIMSON_DARK   = new Color(139, 0, 0);
    static final Color CRIMSON_BRIGHT = new Color(211, 47, 47);
    static final Color CRIMSON_ACCENT = new Color(198, 40, 40);
    static final Color BG_LIGHT_PINK  = new Color(255, 245, 247);
    static final Color CARD_WHITE     = Color.WHITE;
    static final Color STATUS_GREEN   = new Color(67, 160, 71);
    static final Color STATUS_BLUE    = new Color(30, 136, 229);
    static final Color STATUS_ORANGE  = new Color(251, 140, 0);
    static final Color TEXT_DARK      = new Color(33, 33, 33);
    static final Color TEXT_MUTED     = new Color(150, 150, 150);
    static final Color NAV_BG         = new Color(20, 20, 20);
    static final Color NAV_BTN        = new Color(45, 45, 45);
    static final Color RESULT_BG      = new Color(255, 235, 238);

    DataHandler dh = new DataHandler();
    CardLayout cardLayout = new CardLayout();
    Panel centerPanel;
    String currentCard = "Dashboard";

    Button btnDash, btnSub, btnLog, btnLab, btnProj, btnDl;
    Button activeNavBtn;

    TextArea dashExamsTA, dashAlertsTA;
    Label dashTotalSubLbl, dashCgpaLbl, dashLabLbl, dashTaskLbl;

    TextArea subjectsTA;
    Label calcLbl;

    TextArea sessionsTA;
    Label totalHoursLbl;

    TextArea labsTA;
    Label avgLabLbl;

    TextArea projectsTA;
    Label projSummaryLbl;

    TextArea deadlinesTA;
    Panel urgencyBarPanel;

    Label cgpaValueLbl;
    TextArea cgpaBreakdownTA;
    CGPABar cgpaBar;

    public GradeForge() {
        setLayout(new BorderLayout());
        buildNorth();
        buildCenter();
        buildSouth();
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
    }

    class PaddedPanel extends Panel {
        private int pad;
        PaddedPanel(int pad) {
            this.pad = pad;
            setLayout(new BorderLayout(pad / 2, pad / 2));
        }
        public Insets getInsets() {
            return new Insets(pad, pad, pad, pad);
        }
    }

    class CGPABar extends Panel {
        double cgpa;
        CGPABar(double cgpa) {
            this.cgpa = cgpa;
            setPreferredSize(new Dimension(500, 22));
            setBackground(CARD_WHITE);
        }
        public void paint(Graphics g) {
            int w = getSize().width;
            g.setColor(new Color(230, 230, 230));
            g.fillRoundRect(0, 5, w, 12, 6, 6);
            g.setColor(CRIMSON_BRIGHT);
            int filled = (int)(w * (cgpa / 10.0));
            if (filled > 0) g.fillRoundRect(0, 5, filled, 12, 6, 6);
        }
    }

    private String bar(int percent) {
        int filled = percent / 10;
        int empty = 10 - filled;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < filled; i++) sb.append("\u2588");
        for (int i = 0; i < empty; i++) sb.append("\u2591");
        return sb.toString();
    }

    private void buildNorth() {
        Panel north = new Panel(new BorderLayout()) {
            public Insets getInsets() { return new Insets(12, 16, 12, 16); }
        };
        north.setBackground(CRIMSON_DARK);

        Panel left = new Panel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        left.setBackground(CRIMSON_DARK);

        Label title = new Label("GradeForge");
        title.setFont(new Font("SansSerif", Font.BOLD, 26));
        title.setForeground(Color.WHITE);

        Label pipe = new Label("|");
        pipe.setFont(new Font("SansSerif", Font.PLAIN, 20));
        pipe.setForeground(new Color(255, 150, 150));

        Label sub = new Label("Semester Battle Planner");
        sub.setFont(new Font("SansSerif", Font.PLAIN, 14));
        sub.setForeground(new Color(255, 200, 200));

        left.add(title);
        left.add(pipe);
        left.add(sub);

        Panel right = new Panel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        right.setBackground(CRIMSON_DARK);

        SimpleDateFormat sdf = new SimpleDateFormat("EEE, d MMM yyyy");
        Label dateLbl = new Label(sdf.format(new Date()));
        dateLbl.setFont(new Font("SansSerif", Font.PLAIN, 12));
        dateLbl.setForeground(new Color(255, 210, 210));
        right.add(dateLbl);

        north.add(left, BorderLayout.WEST);
        north.add(right, BorderLayout.EAST);
        add(north, BorderLayout.NORTH);
    }

    private void buildCenter() {
        centerPanel = new Panel(cardLayout);
        centerPanel.setBackground(BG_LIGHT_PINK);
        centerPanel.add(makeDashCard(),      "Dashboard");
        centerPanel.add(makeSubjectsCard(),  "Subjects");
        centerPanel.add(makeStudyLogCard(),  "StudyLog");
        centerPanel.add(makeLabExamsCard(),  "LabExams");
        centerPanel.add(makeProjectsCard(),  "Projects");
        centerPanel.add(makeDeadlinesCard(), "Deadlines");
        add(centerPanel, BorderLayout.CENTER);
    }

    private void buildSouth() {
        Panel sep = new Panel();
        sep.setBackground(CRIMSON_BRIGHT);
        sep.setPreferredSize(new Dimension(0, 2));

        Panel wrapper = new Panel(new BorderLayout());
        wrapper.setBackground(NAV_BG);
        wrapper.add(sep, BorderLayout.NORTH);

        Panel nav = new Panel(new FlowLayout(FlowLayout.CENTER, 8, 10));
        nav.setBackground(NAV_BG);

        btnDash = makeNavBtn("Dashboard");
        btnSub  = makeNavBtn("Subjects");
        btnLog  = makeNavBtn("Study Log");
        btnLab  = makeNavBtn("Lab Exams");
        btnProj = makeNavBtn("Projects");
        btnDl   = makeNavBtn("Deadlines");

        btnDash.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) { switchCard("Dashboard", btnDash); }
        });
        btnSub.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) { switchCard("Subjects", btnSub); }
        });
        btnLog.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) { switchCard("StudyLog", btnLog); }
        });
        btnLab.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) { switchCard("LabExams", btnLab); }
        });
        btnProj.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) { switchCard("Projects", btnProj); }
        });
        btnDl.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) { switchCard("Deadlines", btnDl); }
        });

        nav.add(btnDash); nav.add(btnSub); nav.add(btnLog);
        nav.add(btnLab);  nav.add(btnProj); nav.add(btnDl);

        wrapper.add(nav, BorderLayout.CENTER);
        add(wrapper, BorderLayout.SOUTH);

        setNavActive(btnDash);
    }

    private Button makeNavBtn(String text) {
        Button btn = new Button(text);
        btn.setBackground(NAV_BTN);
        btn.setForeground(new Color(200, 200, 200));
        btn.setFont(new Font("SansSerif", Font.PLAIN, 12));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(120, 36));
        return btn;
    }

    private void setNavActive(Button btn) {
        if (activeNavBtn != null) {
            activeNavBtn.setBackground(NAV_BTN);
            activeNavBtn.setForeground(new Color(200, 200, 200));
            activeNavBtn.setFont(new Font("SansSerif", Font.PLAIN, 12));
        }
        btn.setBackground(CRIMSON_BRIGHT);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("SansSerif", Font.BOLD, 12));
        activeNavBtn = btn;
    }

    private void switchCard(String name, Button btn) {
        setNavActive(btn);
        currentCard = name;
        cardLayout.show(centerPanel, name);
        if (name.equals("Dashboard"))  refreshDashData();
        if (name.equals("Subjects"))   refreshSubjects();
        if (name.equals("StudyLog"))   refreshSessions("");
        if (name.equals("LabExams"))   refreshLabs();
        if (name.equals("Projects"))   refreshProjects();
        if (name.equals("Deadlines"))  refreshDeadlines();
    }

    private Panel makeDashCard() {
        Panel card = new Panel(new BorderLayout(8, 8)) {
            public Insets getInsets() { return new Insets(10, 10, 10, 10); }
        };
        card.setBackground(BG_LIGHT_PINK);

        Panel statsGrid = new Panel(new GridLayout(2, 2, 10, 10));
        statsGrid.setBackground(BG_LIGHT_PINK);

        dashTotalSubLbl = new Label("0", Label.CENTER);
        dashCgpaLbl     = new Label("-.--", Label.CENTER);
        dashLabLbl      = new Label("0", Label.CENTER);
        dashTaskLbl     = new Label("0", Label.CENTER);

        statsGrid.add(makeStatCard("TOTAL SUBJECTS",  dashTotalSubLbl, "this semester", CRIMSON_BRIGHT));
        statsGrid.add(makeStatCard("CURRENT CGPA",    dashCgpaLbl,     "grade points",  STATUS_GREEN));
        statsGrid.add(makeStatCard("UPCOMING LABS",   dashLabLbl,      "to be taken",   STATUS_BLUE));
        statsGrid.add(makeStatCard("PENDING TASKS",   dashTaskLbl,     "need attention",STATUS_ORANGE));

        card.add(statsGrid, BorderLayout.NORTH);

        Panel bottom = new Panel(new GridLayout(1, 2, 10, 0));
        bottom.setBackground(BG_LIGHT_PINK);

        dashExamsTA  = makeTA(10, 30);
        dashAlertsTA = makeTA(10, 30);

        bottom.add(makeSectionPanel("UPCOMING EXAMS",  dashExamsTA,  CRIMSON_DARK));
        bottom.add(makeSectionPanel("ACTIVE ALERTS",   dashAlertsTA, STATUS_ORANGE));

        card.add(bottom, BorderLayout.CENTER);

        Button refreshBtn = new Button("Refresh Dashboard");
        styleBtn(refreshBtn, CRIMSON_BRIGHT);
        refreshBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) { refreshDashData(); }
        });

        Panel btnRow = new Panel(new FlowLayout(FlowLayout.RIGHT));
        btnRow.setBackground(BG_LIGHT_PINK);
        btnRow.add(refreshBtn);
        card.add(btnRow, BorderLayout.SOUTH);

        refreshDashData();
        return card;
    }

    private Panel makeStatCard(String title, Label valueLbl, String subtitle, Color bg) {
        Panel card = new PaddedPanel(14);
        card.setBackground(bg);

        Label titleLbl = new Label(title, Label.CENTER);
        titleLbl.setFont(new Font("SansSerif", Font.PLAIN, 11));
        titleLbl.setForeground(new Color(220, 220, 220));

        valueLbl.setFont(new Font("SansSerif", Font.BOLD, 46));
        valueLbl.setForeground(Color.WHITE);

        Label subLbl = new Label(subtitle, Label.CENTER);
        subLbl.setFont(new Font("SansSerif", Font.ITALIC, 11));
        subLbl.setForeground(new Color(220, 220, 220));

        card.add(titleLbl, BorderLayout.NORTH);
        card.add(valueLbl, BorderLayout.CENTER);
        card.add(subLbl,   BorderLayout.SOUTH);
        return card;
    }

    private Panel makeSectionPanel(String title, TextArea ta, Color titleColor) {
        Panel p = new PaddedPanel(10);
        p.setBackground(CARD_WHITE);

        Label lbl = new Label(title);
        lbl.setFont(new Font("SansSerif", Font.BOLD, 11));
        lbl.setForeground(titleColor);

        Panel underline = new Panel();
        underline.setBackground(CRIMSON_BRIGHT);
        underline.setPreferredSize(new Dimension(0, 2));

        Panel titleWrap = new Panel(new BorderLayout());
        titleWrap.setBackground(CARD_WHITE);
        titleWrap.add(lbl,       BorderLayout.NORTH);
        titleWrap.add(underline, BorderLayout.SOUTH);

        p.add(titleWrap, BorderLayout.NORTH);
        p.add(new ScrollPane(ScrollPane.SCROLLBARS_AS_NEEDED) {{ add(ta); }}, BorderLayout.CENTER);
        return p;
    }

    private void refreshDashData() {
        int[] stats = dh.getDashboardStats();
        double cgpa = dh.calculateCurrentCGPA();

        dashTotalSubLbl.setText(String.valueOf(stats[0]));
        dashCgpaLbl.setText(stats[0] == 0 ? "-.--" : String.format("%.2f", cgpa));
        dashLabLbl.setText(String.valueOf(stats[2]));
        dashTaskLbl.setText(String.valueOf(stats[1]));

        ArrayList<Subject> subs = dh.getAllSubjects();
        if (subs.isEmpty()) {
            dashExamsTA.setText("No exams scheduled yet.");
            dashExamsTA.setForeground(TEXT_MUTED);
        } else {
            StringBuilder sb = new StringBuilder();
            for (Subject s : subs) {
                sb.append("\u25b8 ").append(s.getSubjectCode()).append(" \u2014 ").append(s.getSubjectName()).append("\n");
                sb.append("  Syllabus: ").append(bar(s.getSyllabusPercent())).append(" ").append(s.getSyllabusPercent()).append("%\n");
                sb.append("  Exam: ").append(s.getSemExamDate()).append("  Hall: ").append(s.getExamHall()).append("\n\n");
            }
            dashExamsTA.setText(sb.toString());
            dashExamsTA.setForeground(TEXT_DARK);
        }

        ArrayList<Deadline> dls = dh.getAllDeadlines();
        ArrayList<LabExam> labs = dh.getUpcomingLabExams();
        boolean hasAlerts = false;
        StringBuilder sb = new StringBuilder();

        for (Deadline dl : dls) {
            if (dl.getStatus().equals("Urgent")) {
                sb.append("[URGENT] ").append(dl.getTitle()).append("\n  Due: ").append(dl.getDueDate()).append("\n\n");
                hasAlerts = true;
            } else if (dl.getStatus().equals("Pending")) {
                sb.append("[PENDING] ").append(dl.getTitle()).append("\n  Due: ").append(dl.getDueDate()).append("\n\n");
                hasAlerts = true;
            }
        }
        for (LabExam lab : labs) {
            sb.append("[LAB] ").append(lab.getSubjectName()).append("\n  Date: ").append(lab.getExamDate()).append("\n\n");
            hasAlerts = true;
        }

        if (!hasAlerts) {
            dashAlertsTA.setText("All clear! No urgent items.");
            dashAlertsTA.setForeground(TEXT_MUTED);
        } else {
            dashAlertsTA.setText(sb.toString());
            dashAlertsTA.setForeground(TEXT_DARK);
        }
    }

    private Panel makeSubjectsCard() {
        Panel card = new Panel(new BorderLayout(8, 0)) {
            public Insets getInsets() { return new Insets(10, 10, 10, 10); }
        };
        card.setBackground(BG_LIGHT_PINK);

        Panel left = new Panel(new BorderLayout());
        left.setBackground(CARD_WHITE);
        left.setPreferredSize(new Dimension(310, 0));

        Label listTitle = sectionTitle("MY SUBJECTS");
        left.add(wrapWithUnderline(listTitle, CARD_WHITE), BorderLayout.NORTH);

        subjectsTA = makeTA(22, 28);
        left.add(new ScrollPane(ScrollPane.SCROLLBARS_AS_NEEDED) {{ add(subjectsTA); }}, BorderLayout.CENTER);

        Button refreshBtn = new Button("Refresh");
        styleBtn(refreshBtn, CRIMSON_BRIGHT);
        refreshBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) { refreshSubjects(); }
        });
        Panel rp = new Panel(new FlowLayout(FlowLayout.CENTER, 4, 6));
        rp.setBackground(CARD_WHITE);
        rp.add(refreshBtn);
        left.add(rp, BorderLayout.SOUTH);
        card.add(left, BorderLayout.WEST);

        Panel right = new PaddedPanel(12);
        right.setBackground(BG_LIGHT_PINK);

        Label formTitle = sectionTitle("ADD / EDIT SUBJECT");
        right.add(wrapWithUnderline(formTitle, BG_LIGHT_PINK), BorderLayout.NORTH);

        Panel form = new Panel(new GridBagLayout());
        form.setBackground(BG_LIGHT_PINK);
        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(5, 4, 5, 4);
        gc.fill = GridBagConstraints.HORIZONTAL;

        TextField tfCode      = makeTF(14);
        TextField tfName      = makeTF(14);
        TextField tfCredits   = makeTF(14);
        TextField tfCA1       = makeTF(14);
        TextField tfCA2       = makeTF(14);
        TextField tfAssign    = makeTF(14);
        TextField tfTarget    = makeTF(14);
        TextField tfSyllabus  = makeTF(14);
        TextField tfExamDate  = makeTF(14);
        TextField tfHall      = makeTF(14);

        String[] labels = {"Subject Code*", "Subject Name*", "Credit Hours*",
                           "CA1 Marks (/10)", "CA2 Marks (/10)", "Assignment Marks (/20)",
                           "Target Grade (6-10)*", "Syllabus % (0-100)", "Exam Date (DD-MM-YYYY)", "Exam Hall"};
        TextField[] fields = {tfCode, tfName, tfCredits, tfCA1, tfCA2, tfAssign, tfTarget, tfSyllabus, tfExamDate, tfHall};

        for (int i = 0; i < labels.length; i++) {
            gc.gridx = 0; gc.gridy = i; gc.weightx = 0.3;
            Label lbl = new Label(labels[i]);
            lbl.setFont(new Font("SansSerif", Font.PLAIN, 12));
            lbl.setForeground(TEXT_DARK);
            form.add(lbl, gc);
            gc.gridx = 1; gc.weightx = 0.7;
            form.add(fields[i], gc);
        }

        right.add(form, BorderLayout.CENTER);

        calcLbl = new Label("  Internal (40): --  |  External Needed: -- / 60  ");
        calcLbl.setFont(new Font("SansSerif", Font.BOLD, 12));
        calcLbl.setForeground(CRIMSON_BRIGHT);
        Panel calcPanel = new Panel(new FlowLayout(FlowLayout.LEFT, 6, 6));
        calcPanel.setBackground(RESULT_BG);
        calcPanel.add(calcLbl);

        FocusAdapter calcListener = new FocusAdapter() {
            public void focusLost(FocusEvent e) {
                try {
                    double ca1 = tfCA1.getText().trim().isEmpty() ? 0 : Double.parseDouble(tfCA1.getText().trim());
                    double ca2 = tfCA2.getText().trim().isEmpty() ? 0 : Double.parseDouble(tfCA2.getText().trim());
                    double assign = tfAssign.getText().trim().isEmpty() ? 0 : Double.parseDouble(tfAssign.getText().trim());
                    double tg  = tfTarget.getText().trim().isEmpty() ? 0 : Double.parseDouble(tfTarget.getText().trim());
                    double internal = ca1 + ca2 + assign;
                    double ext = 0;
                    if (tg == 10) ext = 60 - internal;
                    else if (tg == 9) ext = 54 - internal;
                    else if (tg == 8) ext = 48 - internal;
                    else if (tg == 7) ext = 42 - internal;
                    else if (tg == 6) ext = 36 - internal;
                    if (ext < 0) ext = 0;
                    if (ext > 60) ext = 60;
                    calcLbl.setText("  Internal (40): " + String.format("%.1f", internal) + "  |  External Needed: " + String.format("%.1f", ext) + " / 60  ");
                } catch (Exception ex) {
                    calcLbl.setText("  Internal (40): --  |  External Needed: -- / 60  ");
                }
            }
        };
        tfCA1.addFocusListener(calcListener);
        tfCA2.addFocusListener(calcListener);
        tfAssign.addFocusListener(calcListener);
        tfTarget.addFocusListener(calcListener);

        Panel btnRow = new Panel(new FlowLayout(FlowLayout.CENTER, 6, 6));
        btnRow.setBackground(BG_LIGHT_PINK);

        Button btnAdd    = new Button("Add Subject");   styleBtn(btnAdd, CRIMSON_BRIGHT);
        Button btnLoad   = new Button("Load to Edit");  styleBtn(btnLoad, STATUS_BLUE);
        Button btnUpdate = new Button("Update");        styleBtn(btnUpdate, STATUS_GREEN);
        Button btnDel    = new Button("Delete");        styleDelBtn(btnDel);
        Button btnClear  = new Button("Clear");         styleBtn(btnClear, TEXT_MUTED);

        btnRow.add(btnAdd); btnRow.add(btnLoad);
        btnRow.add(btnUpdate); btnRow.add(btnDel); btnRow.add(btnClear);

        Panel southRight = new Panel(new BorderLayout());
        southRight.setBackground(BG_LIGHT_PINK);
        southRight.add(calcPanel, BorderLayout.NORTH);
        southRight.add(btnRow,    BorderLayout.SOUTH);
        right.add(southRight, BorderLayout.SOUTH);

        card.add(right, BorderLayout.CENTER);

        btnAdd.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    String code = tfCode.getText().trim();
                    String name = tfName.getText().trim();
                    String crStr = tfCredits.getText().trim();
                    String tgStr = tfTarget.getText().trim();
                    if (code.isEmpty() || name.isEmpty() || crStr.isEmpty() || tgStr.isEmpty()) {
                        showErr("Fill all required fields (*)"); return;
                    }
                    int cr = Integer.parseInt(crStr);
                    double ca1 = tfCA1.getText().trim().isEmpty() ? 0 : Double.parseDouble(tfCA1.getText().trim());
                    double ca2 = tfCA2.getText().trim().isEmpty() ? 0 : Double.parseDouble(tfCA2.getText().trim());
                    double assign = tfAssign.getText().trim().isEmpty() ? 0 : Double.parseDouble(tfAssign.getText().trim());
                    double tg  = Double.parseDouble(tgStr);
                    int syl    = tfSyllabus.getText().trim().isEmpty() ? 0 : Integer.parseInt(tfSyllabus.getText().trim());
                    Subject s  = new Subject(code, name, cr, ca1, ca2, assign, tg, syl, tfExamDate.getText().trim(), tfHall.getText().trim());
                    if (dh.addSubject(s)) {
                        showOk("Subject added!");
                        clearFields(tfCode, tfName, tfCredits, tfCA1, tfCA2, tfAssign, tfTarget, tfSyllabus, tfExamDate, tfHall);
                        refreshSubjects();
                        refreshDashData();
                    } else {
                        showErr("Subject code already exists!");
                    }
                } catch (NumberFormatException ex) {
                    showErr("Invalid number in one of the fields");
                }
            }
        });

        btnLoad.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String code = tfCode.getText().trim();
                if (code.isEmpty()) { showErr("Enter subject code to load"); return; }
                Subject s = dh.findSubjectByCode(code);
                if (s != null) {
                    tfName.setText(s.getSubjectName());
                    tfCredits.setText(String.valueOf(s.getCreditHours()));
                    tfCA1.setText(String.valueOf(s.getCa1Marks()));
                    tfCA2.setText(String.valueOf(s.getCa2Marks()));
                    tfAssign.setText(String.valueOf(s.getAssignmentMarks()));
                    tfTarget.setText(String.valueOf(s.getTargetGrade()));
                    tfSyllabus.setText(String.valueOf(s.getSyllabusPercent()));
                    tfExamDate.setText(s.getSemExamDate());
                    tfHall.setText(s.getExamHall());
                } else {
                    showErr("Subject not found");
                }
            }
        });

        btnUpdate.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    String code = tfCode.getText().trim();
                    if (code.isEmpty()) { showErr("Enter subject code"); return; }
                    double ca1 = tfCA1.getText().trim().isEmpty() ? 0 : Double.parseDouble(tfCA1.getText().trim());
                    double ca2 = tfCA2.getText().trim().isEmpty() ? 0 : Double.parseDouble(tfCA2.getText().trim());
                    double assign = tfAssign.getText().trim().isEmpty() ? 0 : Double.parseDouble(tfAssign.getText().trim());
                    int syl    = tfSyllabus.getText().trim().isEmpty() ? 0 : Integer.parseInt(tfSyllabus.getText().trim());
                    if (dh.updateSubjectMarks(code, ca1, ca2, assign, syl, tfExamDate.getText().trim(), tfHall.getText().trim())) {
                        showOk("Updated!");
                        refreshSubjects(); refreshDashData();
                    } else {
                        showErr("Subject not found");
                    }
                } catch (NumberFormatException ex) {
                    showErr("Invalid number");
                }
            }
        });

        btnDel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String code = tfCode.getText().trim();
                if (code.isEmpty()) { showErr("Enter subject code"); return; }
                if (dh.deleteSubject(code)) {
                    showOk("Deleted!");
                    clearFields(tfCode, tfName, tfCredits, tfCA1, tfCA2, tfAssign, tfTarget, tfSyllabus, tfExamDate, tfHall);
                    refreshSubjects(); refreshDashData();
                } else {
                    showErr("Subject not found");
                }
            }
        });

        btnClear.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                clearFields(tfCode, tfName, tfCredits, tfCA1, tfCA2, tfAssign, tfTarget, tfSyllabus, tfExamDate, tfHall);
                calcLbl.setText("  Internal (40): --  |  External Needed: -- / 60  ");
            }
        });

        refreshSubjects();
        return card;
    }

    private void refreshSubjects() {
        ArrayList<Subject> list = dh.getAllSubjects();
        if (list.isEmpty()) {
            subjectsTA.setText("No subjects added yet.\nClick 'Add Subject' to get started.");
            subjectsTA.setForeground(TEXT_MUTED);
            return;
        }
        StringBuilder sb = new StringBuilder();
        for (Subject s : list) {
            sb.append("\u25b8 ").append(s.getSubjectCode()).append(" \u2014 ").append(s.getSubjectName()).append("\n");
            sb.append("  CA1: ").append(s.getCa1Marks()).append("  CA2: ").append(s.getCa2Marks()).append("  Assign: ").append(s.getAssignmentMarks()).append("\n");
            sb.append("  Internal (40): ").append(String.format("%.1f", s.getInternalAverage())).append("  |  External Needed: ").append(String.format("%.1f", s.getExternalRequired())).append("/60\n");
            sb.append("  Syllabus: ").append(bar(s.getSyllabusPercent())).append(" ").append(s.getSyllabusPercent()).append("%\n");
            sb.append("  Exam: ").append(s.getSemExamDate()).append("  Hall: ").append(s.getExamHall()).append("\n");
            sb.append("\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\n");
        }
        subjectsTA.setText(sb.toString());
        subjectsTA.setForeground(TEXT_DARK);
    }

    private Panel makeStudyLogCard() {
        Panel card = new Panel(new BorderLayout(0, 6)) {
            public Insets getInsets() { return new Insets(10, 10, 10, 10); }
        };
        card.setBackground(BG_LIGHT_PINK);

        Panel topWrap = new PaddedPanel(10);
        topWrap.setBackground(CARD_WHITE);

        Label formTitle = sectionTitle("LOG A SESSION");
        topWrap.add(wrapWithUnderline(formTitle, CARD_WHITE), BorderLayout.NORTH);

        Panel formRow = new Panel(new GridLayout(2, 6, 6, 4));
        formRow.setBackground(CARD_WHITE);

        TextField tfSub    = makeTF(10);
        TextField tfDate   = makeTF(10);
        TextField tfHours  = makeTF(8);
        TextField tfTopics = makeTF(15);
        TextField tfNotes  = makeTF(15);
        Button logBtn = new Button("Log It!");
        styleBtn(logBtn, CRIMSON_BRIGHT);

        for (String lbl : new String[]{"Subject Code", "Date (DD-MM-YYYY)", "Hours", "Topics Covered", "Notes", ""}) {
            Label l = new Label(lbl);
            l.setFont(new Font("SansSerif", Font.PLAIN, 11));
            l.setForeground(TEXT_DARK);
            formRow.add(l);
        }
        formRow.add(tfSub); formRow.add(tfDate); formRow.add(tfHours);
        formRow.add(tfTopics); formRow.add(tfNotes); formRow.add(logBtn);

        topWrap.add(formRow, BorderLayout.CENTER);
        card.add(topWrap, BorderLayout.NORTH);

        Panel filterBar = new Panel(new FlowLayout(FlowLayout.LEFT, 10, 6));
        filterBar.setBackground(BG_LIGHT_PINK);

        Label filterLbl = new Label("Filter by subject:");
        filterLbl.setFont(new Font("SansSerif", Font.PLAIN, 12));
        filterLbl.setForeground(TEXT_DARK);
        TextField filterTF = makeTF(10);
        Button filterBtn  = new Button("Filter"); styleBtn(filterBtn, STATUS_BLUE);
        Button viewAllBtn = new Button("View All"); styleBtn(viewAllBtn, CRIMSON_BRIGHT);
        Label delLbl = new Label("  Delete ID:");
        delLbl.setFont(new Font("SansSerif", Font.PLAIN, 12));
        TextField delIdTF = makeTF(5);
        Button delBtn = new Button("Delete"); styleDelBtn(delBtn);

        filterBar.add(filterLbl); filterBar.add(filterTF);
        filterBar.add(filterBtn); filterBar.add(viewAllBtn);
        filterBar.add(delLbl); filterBar.add(delIdTF); filterBar.add(delBtn);
        card.add(filterBar, BorderLayout.CENTER);

        sessionsTA = makeTA(16, 70);
        ScrollPane sp = new ScrollPane(ScrollPane.SCROLLBARS_AS_NEEDED);
        sp.add(sessionsTA);

        totalHoursLbl = new Label("");
        totalHoursLbl.setFont(new Font("SansSerif", Font.BOLD, 13));
        totalHoursLbl.setForeground(CRIMSON_BRIGHT);
        Panel totalPanel = new Panel(new FlowLayout(FlowLayout.LEFT, 10, 6));
        totalPanel.setBackground(RESULT_BG);
        totalPanel.add(totalHoursLbl);

        Panel bottom = new Panel(new BorderLayout());
        bottom.add(sp, BorderLayout.CENTER);
        bottom.add(totalPanel, BorderLayout.SOUTH);
        card.add(bottom, BorderLayout.SOUTH);

        logBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    String sub   = tfSub.getText().trim();
                    String date  = tfDate.getText().trim();
                    String hStr  = tfHours.getText().trim();
                    if (sub.isEmpty() || date.isEmpty() || hStr.isEmpty()) {
                        showErr("Subject, Date and Hours are required"); return;
                    }
                    double hrs = Double.parseDouble(hStr);
                    StudySession sess = new StudySession(0, sub, date, hrs, tfTopics.getText().trim(), tfNotes.getText().trim());
                    dh.addSession(sess);
                    final String orig = logBtn.getLabel();
                    logBtn.setLabel("\u2713 Logged!");
                    logBtn.setBackground(STATUS_GREEN);
                    new Thread() {
                        public void run() {
                            try { sleep(2000); } catch (InterruptedException ie) {}
                            logBtn.setLabel(orig);
                            logBtn.setBackground(CRIMSON_BRIGHT);
                        }
                    }.start();
                    clearFields(tfSub, tfDate, tfHours, tfTopics, tfNotes);
                    refreshSessions("");
                    refreshDashData();
                } catch (NumberFormatException ex) {
                    showErr("Hours must be a valid number");
                }
            }
        });

        filterBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) { refreshSessions(filterTF.getText().trim()); }
        });

        viewAllBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) { filterTF.setText(""); refreshSessions(""); }
        });

        delBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    int id = Integer.parseInt(delIdTF.getText().trim());
                    if (dh.deleteSession(id)) {
                        showOk("Session deleted!");
                        refreshSessions("");
                    } else {
                        showErr("Session ID not found");
                    }
                } catch (NumberFormatException ex) {
                    showErr("Invalid session ID");
                }
            }
        });

        refreshSessions("");
        return card;
    }

    private void refreshSessions(String filter) {
        ArrayList<StudySession> list;
        if (filter.isEmpty()) {
            list = dh.getAllSessions();
            totalHoursLbl.setText("");
        } else {
            list = dh.getSessionsBySubject(filter);
            double total = dh.getTotalHoursBySubject(filter);
            totalHoursLbl.setText("  Total study hours for " + filter + ": " + String.format("%.1f", total) + " hrs");
        }

        if (list.isEmpty()) {
            sessionsTA.setText("No sessions logged yet.");
            sessionsTA.setForeground(TEXT_MUTED);
            return;
        }

        StringBuilder sb = new StringBuilder();
        for (StudySession s : list) {
            sb.append("#").append(String.format("%03d", s.getSessionId()));
            sb.append("  ").append(s.getSubjectCode());
            sb.append("  \u00b7  ").append(s.getDate());
            sb.append("  \u00b7  ").append(s.getHoursStudied()).append(" hrs\n");
            sb.append("  Topics: ").append(s.getTopicsCovered()).append("\n");
            if (!s.getNotes().isEmpty()) sb.append("  Notes: ").append(s.getNotes()).append("\n");
            sb.append("\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\n");
        }
        sessionsTA.setText(sb.toString());
        sessionsTA.setForeground(TEXT_DARK);
    }

    private Panel makeLabExamsCard() {
        Panel card = new Panel(new BorderLayout(8, 0)) {
            public Insets getInsets() { return new Insets(10, 10, 10, 10); }
        };
        card.setBackground(BG_LIGHT_PINK);

        Panel left = new Panel(new BorderLayout());
        left.setBackground(CARD_WHITE);
        left.setPreferredSize(new Dimension(320, 0));

        Label listTitle = sectionTitle("LAB EXAMS");
        left.add(wrapWithUnderline(listTitle, CARD_WHITE), BorderLayout.NORTH);

        labsTA = makeTA(20, 28);
        left.add(new ScrollPane(ScrollPane.SCROLLBARS_AS_NEEDED) {{ add(labsTA); }}, BorderLayout.CENTER);
        card.add(left, BorderLayout.WEST);

        Panel right = new PaddedPanel(12);
        right.setBackground(BG_LIGHT_PINK);

        Label formTitle = sectionTitle("ADD / EDIT LAB EXAM");
        right.add(wrapWithUnderline(formTitle, BG_LIGHT_PINK), BorderLayout.NORTH);

        Panel form = new Panel(new GridBagLayout());
        form.setBackground(BG_LIGHT_PINK);
        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(5, 4, 5, 4);
        gc.fill = GridBagConstraints.HORIZONTAL;

        TextField tfSubCode  = makeTF(14);
        TextField tfSubName  = makeTF(14);
        TextField tfExamDate = makeTF(14);
        TextField tfMarks    = makeTF(14);
        TextField tfMaxMarks = makeTF(14);
        TextField tfNotes    = makeTF(14);
        TextField tfLabId    = makeTF(10);

        Choice statusChoice = new Choice();
        statusChoice.add("Upcoming");
        statusChoice.add("Completed");
        statusChoice.add("Missed");

        String[] lbls = {"Subject Code*", "Subject Name*", "Exam Date (DD-MM-YYYY)*",
                         "Marks Scored", "Max Marks (default 100)", "Status",
                         "Notes", "Lab ID (for update/delete)"};
        Component[] comps = {tfSubCode, tfSubName, tfExamDate, tfMarks, tfMaxMarks, statusChoice, tfNotes, tfLabId};

        for (int i = 0; i < lbls.length; i++) {
            gc.gridx = 0; gc.gridy = i; gc.weightx = 0.35;
            Label l = new Label(lbls[i]);
            l.setFont(new Font("SansSerif", Font.PLAIN, 12));
            l.setForeground(TEXT_DARK);
            form.add(l, gc);
            gc.gridx = 1; gc.weightx = 0.65;
            form.add(comps[i], gc);
        }

        right.add(form, BorderLayout.CENTER);

        avgLabLbl = new Label("  Average Lab Score: -- / 100");
        avgLabLbl.setFont(new Font("SansSerif", Font.ITALIC, 12));
        avgLabLbl.setForeground(CRIMSON_BRIGHT);
        Panel avgPanel = new Panel(new FlowLayout(FlowLayout.LEFT, 6, 6));
        avgPanel.setBackground(RESULT_BG);
        avgPanel.add(avgLabLbl);

        Panel btnRow = new Panel(new FlowLayout(FlowLayout.CENTER, 8, 6));
        btnRow.setBackground(BG_LIGHT_PINK);
        Button btnAdd = new Button("Add Lab Exam"); styleBtn(btnAdd, CRIMSON_BRIGHT);
        Button btnUpd = new Button("Update Marks"); styleBtn(btnUpd, STATUS_GREEN);
        Button btnDel = new Button("Delete");       styleDelBtn(btnDel);
        btnRow.add(btnAdd); btnRow.add(btnUpd); btnRow.add(btnDel);

        Panel southRight = new Panel(new BorderLayout());
        southRight.setBackground(BG_LIGHT_PINK);
        southRight.add(avgPanel, BorderLayout.NORTH);
        southRight.add(btnRow,   BorderLayout.SOUTH);
        right.add(southRight, BorderLayout.SOUTH);

        card.add(right, BorderLayout.CENTER);

        btnAdd.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    String sc = tfSubCode.getText().trim();
                    String sn = tfSubName.getText().trim();
                    String dt = tfExamDate.getText().trim();
                    if (sc.isEmpty() || sn.isEmpty() || dt.isEmpty()) {
                        showErr("Fill required fields (*)"); return;
                    }
                    double marks    = tfMarks.getText().trim().isEmpty() ? 0 : Double.parseDouble(tfMarks.getText().trim());
                    double maxMarks = tfMaxMarks.getText().trim().isEmpty() ? 100 : Double.parseDouble(tfMaxMarks.getText().trim());
                    LabExam lab = new LabExam(0, sc, sn, dt, marks, maxMarks, statusChoice.getSelectedItem(), tfNotes.getText().trim());
                    dh.addLabExam(lab);
                    showOk("Lab exam added!");
                    clearFields(tfSubCode, tfSubName, tfExamDate, tfMarks, tfMaxMarks, tfNotes);
                    refreshLabs(); refreshDashData();
                } catch (NumberFormatException ex) {
                    showErr("Invalid number in marks");
                }
            }
        });

        btnUpd.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    int id = Integer.parseInt(tfLabId.getText().trim());
                    double marks = tfMarks.getText().trim().isEmpty() ? 0 : Double.parseDouble(tfMarks.getText().trim());
                    if (dh.updateLabExamMarks(id, marks, statusChoice.getSelectedItem())) {
                        showOk("Updated!"); refreshLabs();
                    } else {
                        showErr("Lab ID not found");
                    }
                } catch (NumberFormatException ex) {
                    showErr("Invalid ID or marks");
                }
            }
        });

        btnDel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    int id = Integer.parseInt(tfLabId.getText().trim());
                    if (dh.deleteLabExam(id)) {
                        showOk("Deleted!"); refreshLabs(); refreshDashData();
                    } else {
                        showErr("Lab ID not found");
                    }
                } catch (NumberFormatException ex) {
                    showErr("Invalid ID");
                }
            }
        });

        refreshLabs();
        return card;
    }

    private void refreshLabs() {
        ArrayList<LabExam> list = dh.getAllLabExams();
        if (list.isEmpty()) {
            labsTA.setText("No lab exams added yet.");
            labsTA.setForeground(TEXT_MUTED);
            avgLabLbl.setText("  Average Lab Score: -- / 100");
            return;
        }
        StringBuilder sb = new StringBuilder();
        double total = 0; int count = 0;
        for (LabExam lab : list) {
            String prefix = "? ";
            if (lab.getStatus().equals("Upcoming"))  prefix = "\u23f3 ";
            else if (lab.getStatus().equals("Completed")) { prefix = "\u2713 "; total += lab.getMarksScored(); count++; }
            else prefix = "\u2717 ";

            sb.append(prefix).append("#").append(String.format("%03d", lab.getLabId()));
            sb.append("  ").append(lab.getSubjectName()).append(" \u00b7 ").append(lab.getSubjectCode()).append("\n");
            sb.append("   Date: ").append(lab.getExamDate()).append("\n");
            sb.append("   Marks: ").append(lab.getMarksScored()).append("/").append(lab.getMaxMarks()).append("\n");
            sb.append("   Status: ").append(lab.getStatus()).append("\n");
            sb.append("\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\n");
        }
        labsTA.setText(sb.toString());
        labsTA.setForeground(TEXT_DARK);
        double avg = count > 0 ? total / count : 0;
        avgLabLbl.setText("  Average Lab Score: " + String.format("%.1f", avg) + " / 100");
    }

    private Panel makeProjectsCard() {
        Panel card = new Panel(new BorderLayout(8, 0)) {
            public Insets getInsets() { return new Insets(10, 10, 10, 10); }
        };
        card.setBackground(BG_LIGHT_PINK);

        Panel left = new Panel(new BorderLayout());
        left.setBackground(CARD_WHITE);
        left.setPreferredSize(new Dimension(320, 0));

        Label listTitle = sectionTitle("MY PROJECTS");
        left.add(wrapWithUnderline(listTitle, CARD_WHITE), BorderLayout.NORTH);

        projectsTA = makeTA(20, 28);
        left.add(new ScrollPane(ScrollPane.SCROLLBARS_AS_NEEDED) {{ add(projectsTA); }}, BorderLayout.CENTER);
        card.add(left, BorderLayout.WEST);

        Panel right = new PaddedPanel(12);
        right.setBackground(BG_LIGHT_PINK);

        Label formTitle = sectionTitle("ADD / EDIT PROJECT");
        right.add(wrapWithUnderline(formTitle, BG_LIGHT_PINK), BorderLayout.NORTH);

        Panel form = new Panel(new GridBagLayout());
        form.setBackground(BG_LIGHT_PINK);
        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(5, 4, 5, 4);
        gc.fill = GridBagConstraints.HORIZONTAL;

        TextField tfTitle      = makeTF(14);
        TextField tfGuide      = makeTF(14);
        TextField tfTeam       = makeTF(14);
        TextField tfStack      = makeTF(14);
        TextField tfR1         = makeTF(14);
        TextField tfR2         = makeTF(14);
        TextField tfFinal      = makeTF(14);
        TextField tfNextReview = makeTF(14);
        TextField tfNotes      = makeTF(14);
        TextField tfProjId     = makeTF(10);

        Choice projStatus = new Choice();
        projStatus.add("Active");
        projStatus.add("Submitted");
        projStatus.add("Completed");

        String[] lbls = {"Project Title*", "Guide Name*", "Team Members",
                         "Tech Stack*", "Review 1 Marks (/50)", "Review 2 Marks (/50)",
                         "Final Marks (/100)", "Next Review Date", "Status",
                         "Notes", "Project ID (update/delete)"};
        Component[] comps = {tfTitle, tfGuide, tfTeam, tfStack, tfR1, tfR2, tfFinal, tfNextReview, projStatus, tfNotes, tfProjId};

        for (int i = 0; i < lbls.length; i++) {
            gc.gridx = 0; gc.gridy = i; gc.weightx = 0.4;
            Label l = new Label(lbls[i]);
            l.setFont(new Font("SansSerif", Font.PLAIN, 12));
            l.setForeground(TEXT_DARK);
            form.add(l, gc);
            gc.gridx = 1; gc.weightx = 0.6;
            form.add(comps[i], gc);
        }
        right.add(form, BorderLayout.CENTER);

        projSummaryLbl = new Label("  Active: 0  |  Completed: 0");
        projSummaryLbl.setFont(new Font("SansSerif", Font.ITALIC, 12));
        projSummaryLbl.setForeground(CRIMSON_BRIGHT);
        Panel sumPanel = new Panel(new FlowLayout(FlowLayout.LEFT, 6, 6));
        sumPanel.setBackground(RESULT_BG);
        sumPanel.add(projSummaryLbl);

        Panel btnRow = new Panel(new FlowLayout(FlowLayout.CENTER, 8, 6));
        btnRow.setBackground(BG_LIGHT_PINK);
        Button btnAdd = new Button("Add Project");    styleBtn(btnAdd, CRIMSON_BRIGHT);
        Button btnUpd = new Button("Update Review");  styleBtn(btnUpd, STATUS_GREEN);
        Button btnDel = new Button("Delete");         styleDelBtn(btnDel);
        btnRow.add(btnAdd); btnRow.add(btnUpd); btnRow.add(btnDel);

        Panel southRight = new Panel(new BorderLayout());
        southRight.setBackground(BG_LIGHT_PINK);
        southRight.add(sumPanel, BorderLayout.NORTH);
        southRight.add(btnRow,   BorderLayout.SOUTH);
        right.add(southRight, BorderLayout.SOUTH);
        card.add(right, BorderLayout.CENTER);

        btnAdd.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    String title = tfTitle.getText().trim();
                    String guide = tfGuide.getText().trim();
                    String stack = tfStack.getText().trim();
                    if (title.isEmpty() || guide.isEmpty() || stack.isEmpty()) {
                        showErr("Fill required fields (*)"); return;
                    }
                    double r1  = tfR1.getText().trim().isEmpty() ? 0 : Double.parseDouble(tfR1.getText().trim());
                    double r2  = tfR2.getText().trim().isEmpty() ? 0 : Double.parseDouble(tfR2.getText().trim());
                    double fin = tfFinal.getText().trim().isEmpty() ? 0 : Double.parseDouble(tfFinal.getText().trim());
                    Project p  = new Project(0, title, tfTeam.getText().trim(), guide, r1, r2, fin, tfNextReview.getText().trim(), projStatus.getSelectedItem(), stack, tfNotes.getText().trim());
                    dh.addProject(p);
                    showOk("Project added!");
                    clearFields(tfTitle, tfGuide, tfTeam, tfStack, tfR1, tfR2, tfFinal, tfNextReview, tfNotes);
                    refreshProjects(); refreshDashData();
                } catch (NumberFormatException ex) {
                    showErr("Invalid number in marks");
                }
            }
        });

        btnUpd.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    int id = Integer.parseInt(tfProjId.getText().trim());
                    double r1  = tfR1.getText().trim().isEmpty() ? 0 : Double.parseDouble(tfR1.getText().trim());
                    double r2  = tfR2.getText().trim().isEmpty() ? 0 : Double.parseDouble(tfR2.getText().trim());
                    double fin = tfFinal.getText().trim().isEmpty() ? 0 : Double.parseDouble(tfFinal.getText().trim());
                    if (dh.updateProjectReview(id, r1, r2, fin, tfNextReview.getText().trim(), projStatus.getSelectedItem())) {
                        showOk("Updated!"); refreshProjects();
                    } else {
                        showErr("Project ID not found");
                    }
                } catch (NumberFormatException ex) {
                    showErr("Invalid ID or marks");
                }
            }
        });

        btnDel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    int id = Integer.parseInt(tfProjId.getText().trim());
                    if (dh.deleteProject(id)) {
                        showOk("Deleted!"); refreshProjects(); refreshDashData();
                    } else {
                        showErr("Project ID not found");
                    }
                } catch (NumberFormatException ex) {
                    showErr("Invalid ID");
                }
            }
        });

        refreshProjects();
        return card;
    }

    private void refreshProjects() {
        ArrayList<Project> list = dh.getAllProjects();
        if (list.isEmpty()) {
            projectsTA.setText("No projects added yet.");
            projectsTA.setForeground(TEXT_MUTED);
            projSummaryLbl.setText("  Active: 0  |  Completed: 0");
            return;
        }
        StringBuilder sb = new StringBuilder();
        int active = 0, completed = 0;
        for (Project p : list) {
            String prefix = ">> ";
            if (p.getStatus().equals("Active")) { prefix = "[A] "; active++; }
            else if (p.getStatus().equals("Submitted")) prefix = "[S] ";
            else { prefix = "[C] "; completed++; }

            sb.append(prefix).append("#").append(String.format("%03d", p.getProjectId())).append("  ").append(p.getProjectTitle()).append("\n");
            sb.append("   Guide: ").append(p.getGuideName()).append("\n");
            sb.append("   Stack: ").append(p.getTechStack()).append("\n");
            sb.append("   R1: ").append(p.getReview1Marks()).append("/50  R2: ");
            sb.append(p.getReview2Marks() == 0 ? "--" : String.valueOf(p.getReview2Marks())).append("/50");
            sb.append("  Final: ").append(p.getFinalMarks() == 0 ? "--" : String.valueOf(p.getFinalMarks())).append("/100\n");
            sb.append("   Next Review: ").append(p.getNextReviewDate()).append("  Status: ").append(p.getStatus()).append("\n");
            sb.append("\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\n");
        }
        projectsTA.setText(sb.toString());
        projectsTA.setForeground(TEXT_DARK);
        projSummaryLbl.setText("  Active: " + active + "  |  Completed: " + completed);
    }

    private Panel makeDeadlinesCard() {
        Panel card = new Panel(new BorderLayout(0, 0)) {
            public Insets getInsets() { return new Insets(10, 10, 10, 10); }
        };
        card.setBackground(BG_LIGHT_PINK);

        urgencyBarPanel = new Panel(new FlowLayout(FlowLayout.CENTER, 12, 8));
        urgencyBarPanel.setBackground(new Color(245, 245, 245));
        urgencyBarPanel.setPreferredSize(new Dimension(0, 44));
        refreshUrgencyBar();
        card.add(urgencyBarPanel, BorderLayout.NORTH);

        Panel mid = new Panel(new BorderLayout(8, 0));
        mid.setBackground(BG_LIGHT_PINK);

        Panel left = new Panel(new BorderLayout());
        left.setBackground(CARD_WHITE);
        left.setPreferredSize(new Dimension(320, 0));

        Label listTitle = sectionTitle("ALL DEADLINES");
        left.add(wrapWithUnderline(listTitle, CARD_WHITE), BorderLayout.NORTH);

        deadlinesTA = makeTA(20, 28);
        left.add(new ScrollPane(ScrollPane.SCROLLBARS_AS_NEEDED) {{ add(deadlinesTA); }}, BorderLayout.CENTER);
        mid.add(left, BorderLayout.WEST);

        Panel right = new PaddedPanel(12);
        right.setBackground(BG_LIGHT_PINK);

        Label formTitle = sectionTitle("ADD DEADLINE");
        right.add(wrapWithUnderline(formTitle, BG_LIGHT_PINK), BorderLayout.NORTH);

        Panel form = new Panel(new GridBagLayout());
        form.setBackground(BG_LIGHT_PINK);
        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(5, 4, 5, 4);
        gc.fill = GridBagConstraints.HORIZONTAL;

        TextField tfTitle   = makeTF(14);
        TextField tfSubCode = makeTF(14);
        TextField tfDueDate = makeTF(14);
        TextField tfNotes   = makeTF(14);
        TextField tfDlId    = makeTF(10);

        Choice typeChoice = new Choice();
        typeChoice.add("Assignment");
        typeChoice.add("Lab Record");
        typeChoice.add("Project");
        typeChoice.add("Other");

        Choice dlStatus = new Choice();
        dlStatus.add("Pending");
        dlStatus.add("Urgent");
        dlStatus.add("Done");

        String[] lbls = {"Title*", "Subject Code*", "Due Date (DD-MM-YYYY)*", "Type", "Status", "Notes", "Deadline ID (update/delete)"};
        Component[] comps = {tfTitle, tfSubCode, tfDueDate, typeChoice, dlStatus, tfNotes, tfDlId};

        for (int i = 0; i < lbls.length; i++) {
            gc.gridx = 0; gc.gridy = i; gc.weightx = 0.4;
            Label l = new Label(lbls[i]);
            l.setFont(new Font("SansSerif", Font.PLAIN, 12));
            l.setForeground(TEXT_DARK);
            form.add(l, gc);
            gc.gridx = 1; gc.weightx = 0.6;
            form.add(comps[i], gc);
        }
        right.add(form, BorderLayout.CENTER);

        Panel btnRow = new Panel(new FlowLayout(FlowLayout.CENTER, 6, 6));
        btnRow.setBackground(BG_LIGHT_PINK);
        Button btnAdd     = new Button("Add Deadline");  styleBtn(btnAdd, CRIMSON_BRIGHT);
        Button btnDone    = new Button("Mark Done");     styleBtn(btnDone, STATUS_GREEN);
        Button btnUrgent  = new Button("Mark Urgent");   styleBtn(btnUrgent, STATUS_ORANGE);
        Button btnDel     = new Button("Delete");        styleDelBtn(btnDel);
        btnRow.add(btnAdd); btnRow.add(btnDone); btnRow.add(btnUrgent); btnRow.add(btnDel);

        Button csvBtn = new Button("Import CSV");
        styleBtn(csvBtn, STATUS_BLUE);
        btnRow.add(csvBtn);

        right.add(btnRow, BorderLayout.SOUTH);
        mid.add(right, BorderLayout.CENTER);
        card.add(mid, BorderLayout.CENTER);

        btnAdd.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String title = tfTitle.getText().trim();
                String sub   = tfSubCode.getText().trim();
                String due   = tfDueDate.getText().trim();
                if (title.isEmpty() || sub.isEmpty() || due.isEmpty()) {
                    showErr("Fill required fields (*)"); return;
                }
                Deadline dl = new Deadline(0, title, sub, due, typeChoice.getSelectedItem(), dlStatus.getSelectedItem(), tfNotes.getText().trim());
                dh.addDeadline(dl);
                showOk("Deadline added!");
                clearFields(tfTitle, tfSubCode, tfDueDate, tfNotes);
                refreshDeadlines(); refreshDashData();
            }
        });

        btnDone.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    int id = Integer.parseInt(tfDlId.getText().trim());
                    if (dh.updateDeadlineStatus(id, "Done")) {
                        showOk("Marked Done!"); refreshDeadlines(); refreshDashData();
                    } else { showErr("Deadline ID not found"); }
                } catch (NumberFormatException ex) { showErr("Invalid ID"); }
            }
        });

        btnUrgent.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    int id = Integer.parseInt(tfDlId.getText().trim());
                    if (dh.updateDeadlineStatus(id, "Urgent")) {
                        showOk("Marked Urgent!"); refreshDeadlines(); refreshDashData();
                    } else { showErr("Deadline ID not found"); }
                } catch (NumberFormatException ex) { showErr("Invalid ID"); }
            }
        });

        btnDel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    int id = Integer.parseInt(tfDlId.getText().trim());
                    if (dh.deleteDeadline(id)) {
                        showOk("Deleted!"); refreshDeadlines(); refreshDashData();
                    } else { showErr("Deadline ID not found"); }
                } catch (NumberFormatException ex) { showErr("Invalid ID"); }
            }
        });

        csvBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                FileDialog fd = new FileDialog(GradeForge.this, "Select Deadlines CSV", FileDialog.LOAD);
                fd.setVisible(true);
                String dir  = fd.getDirectory();
                String file = fd.getFile();
                if (dir != null && file != null) {
                    ArrayList<Deadline> imported = CsvImporter.importDeadlinesFromCsv(dir + file);
                    for (Deadline dl : imported) { dh.addDeadline(dl); }
                    showOk("Imported " + imported.size() + " deadlines!");
                    refreshDeadlines(); refreshDashData();
                }
            }
        });

        refreshDeadlines();
        return card;
    }

    private void refreshUrgencyBar() {
        urgencyBarPanel.removeAll();
        ArrayList<Deadline> list = dh.getAllDeadlines();
        int urgent = 0, pending = 0, done = 0;
        for (Deadline dl : list) {
            if (dl.getStatus().equals("Urgent")) urgent++;
            else if (dl.getStatus().equals("Pending")) pending++;
            else done++;
        }
        Label urgLbl = new Label("  Urgent: " + urgent + "  ");
        urgLbl.setBackground(new Color(183, 28, 28));
        urgLbl.setForeground(Color.WHITE);
        urgLbl.setFont(new Font("SansSerif", Font.BOLD, 12));

        Label pendLbl = new Label("  Pending: " + pending + "  ");
        pendLbl.setBackground(STATUS_ORANGE);
        pendLbl.setForeground(Color.WHITE);
        pendLbl.setFont(new Font("SansSerif", Font.BOLD, 12));

        Label doneLbl = new Label("  Done: " + done + "  ");
        doneLbl.setBackground(STATUS_GREEN);
        doneLbl.setForeground(Color.WHITE);
        doneLbl.setFont(new Font("SansSerif", Font.BOLD, 12));

        urgencyBarPanel.add(urgLbl);
        urgencyBarPanel.add(pendLbl);
        urgencyBarPanel.add(doneLbl);
        urgencyBarPanel.validate();
        urgencyBarPanel.repaint();
    }

    private void refreshDeadlines() {
        refreshUrgencyBar();
        ArrayList<Deadline> list = dh.getAllDeadlines();
        if (list.isEmpty()) {
            deadlinesTA.setText("No deadlines added yet.");
            deadlinesTA.setForeground(TEXT_MUTED);
            return;
        }
        StringBuilder sb = new StringBuilder();
        for (Deadline dl : list) {
            String prefix = ">> ";
            if (dl.getStatus().equals("Urgent")) prefix = "[!] ";
            else if (dl.getStatus().equals("Pending")) prefix = "[~] ";
            else prefix = "[v] ";
            sb.append(prefix).append("[").append(String.format("%03d", dl.getDeadlineId())).append("] ").append(dl.getTitle()).append("\n");
            sb.append("   ").append(dl.getSubjectCode()).append("  \u00b7  Due: ").append(dl.getDueDate()).append("\n");
            sb.append("   Type: ").append(dl.getType()).append("  \u00b7  Status: ").append(dl.getStatus()).append("\n");
            sb.append("\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\n");
        }
        deadlinesTA.setText(sb.toString());
        deadlinesTA.setForeground(TEXT_DARK);
    }

    private TextArea makeTA(int rows, int cols) {
        TextArea ta = new TextArea("", rows, cols, TextArea.SCROLLBARS_VERTICAL_ONLY);
        ta.setEditable(false);
        ta.setFont(new Font("Monospaced", Font.PLAIN, 13));
        ta.setBackground(new Color(252, 252, 252));
        ta.setForeground(TEXT_DARK);
        return ta;
    }

    private TextField makeTF(int cols) {
        TextField tf = new TextField(cols);
        tf.setFont(new Font("SansSerif", Font.PLAIN, 13));
        tf.setBackground(Color.WHITE);
        return tf;
    }

    private void styleBtn(Button btn, Color bg) {
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("SansSerif", Font.BOLD, 12));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private void styleDelBtn(Button btn) {
        btn.setBackground(new Color(183, 28, 28));
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("SansSerif", Font.BOLD, 12));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private Label sectionTitle(String text) {
        Label l = new Label(text);
        l.setFont(new Font("SansSerif", Font.BOLD, 11));
        l.setForeground(CRIMSON_DARK);
        return l;
    }

    private Panel wrapWithUnderline(Label title, Color bg) {
        Panel wrap = new Panel(new BorderLayout());
        wrap.setBackground(bg);
        wrap.add(title, BorderLayout.NORTH);
        Panel line = new Panel();
        line.setBackground(CRIMSON_BRIGHT);
        line.setPreferredSize(new Dimension(0, 2));
        wrap.add(line, BorderLayout.SOUTH);
        return wrap;
    }

    private void clearFields(TextField... fields) {
        for (TextField f : fields) f.setText("");
    }

    private void showErr(String msg) {
        Dialog d = new Dialog(this, "Error", true);
        d.setSize(320, 130);
        d.setLocationRelativeTo(this);
        d.setLayout(new BorderLayout(8, 8));
        Label l = new Label(msg, Label.CENTER);
        l.setForeground(CRIMSON_BRIGHT);
        l.setFont(new Font("SansSerif", Font.PLAIN, 13));
        d.add(l, BorderLayout.CENTER);
        Button ok = new Button("OK");
        styleBtn(ok, CRIMSON_BRIGHT);
        ok.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) { d.dispose(); }
        });
        Panel p = new Panel(new FlowLayout(FlowLayout.CENTER));
        p.add(ok);
        d.add(p, BorderLayout.SOUTH);
        d.setVisible(true);
    }

    private void showOk(String msg) {
        Dialog d = new Dialog(this, "Done", true);
        d.setSize(280, 120);
        d.setLocationRelativeTo(this);
        d.setLayout(new BorderLayout(8, 8));
        Label l = new Label(msg, Label.CENTER);
        l.setForeground(STATUS_GREEN);
        l.setFont(new Font("SansSerif", Font.BOLD, 13));
        d.add(l, BorderLayout.CENTER);
        Button ok = new Button("OK");
        styleBtn(ok, STATUS_GREEN);
        ok.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) { d.dispose(); }
        });
        Panel p = new Panel(new FlowLayout(FlowLayout.CENTER));
        p.add(ok);
        d.add(p, BorderLayout.SOUTH);
        d.setVisible(true);
    }

    public static void main(String[] args) {
        GradeForge app = new GradeForge();
        app.setTitle("GradeForge \u2014 Semester Battle Planner");
        app.setSize(1050, 680);
        app.setLocationRelativeTo(null);
        app.setVisible(true);
    }
}
