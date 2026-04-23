package gradeforge;

import java.io.*;
import java.util.ArrayList;

public class CsvImporter {

    public static ArrayList<Subject> importSubjectsFromCsv(String filePath) {
        ArrayList<Subject> list = new ArrayList<Subject>();
        FileInputStream fis = null;
        InputStreamReader isr = null;
        BufferedReader br = null;
        try {
            fis = new FileInputStream(filePath);
            isr = new InputStreamReader(fis);
            br = new BufferedReader(isr);
            String line = br.readLine();
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;
                String[] parts = line.split(",");
                if (parts.length < 7) continue;
                try {
                    String code = parts[0].trim();
                    String name = parts[1].trim();
                    int credits = Integer.parseInt(parts[2].trim());
                    double ca1 = Double.parseDouble(parts[3].trim());
                    double ca2 = Double.parseDouble(parts[4].trim());
                    double assign = Double.parseDouble(parts[5].trim());
                    double target = Double.parseDouble(parts[6].trim());
                    Subject s = new Subject(code, name, credits, ca1, ca2, assign, target, 0, "", "");
                    list.add(s);
                } catch (NumberFormatException e) {
                    System.err.println("Skipped bad row: " + line);
                }
            }
        } catch (FileNotFoundException e) {
            System.err.println("CSV file not found: " + filePath);
        } catch (IOException e) {
            System.err.println("Error reading CSV: " + e.getMessage());
        } finally {
            try { if (br != null) br.close(); } catch (IOException e) {}
            try { if (isr != null) isr.close(); } catch (IOException e) {}
            try { if (fis != null) fis.close(); } catch (IOException e) {}
        }
        return list;
    }

    public static ArrayList<Deadline> importDeadlinesFromCsv(String filePath) {
        ArrayList<Deadline> list = new ArrayList<Deadline>();
        FileInputStream fis = null;
        InputStreamReader isr = null;
        BufferedReader br = null;
        try {
            fis = new FileInputStream(filePath);
            isr = new InputStreamReader(fis);
            br = new BufferedReader(isr);
            String line = br.readLine();
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;
                String[] parts = line.split(",");
                if (parts.length < 4) continue;
                try {
                    String title = parts[0].trim();
                    String subCode = parts[1].trim();
                    String dueDate = parts[2].trim();
                    String type = parts[3].trim();
                    Deadline d = new Deadline(0, title, subCode, dueDate, type, "Pending", "");
                    list.add(d);
                } catch (Exception e) {
                    System.err.println("Skipped bad row: " + line);
                }
            }
        } catch (FileNotFoundException e) {
            System.err.println("CSV file not found: " + filePath);
        } catch (IOException e) {
            System.err.println("Error reading CSV: " + e.getMessage());
        } finally {
            try { if (br != null) br.close(); } catch (IOException e) {}
            try { if (isr != null) isr.close(); } catch (IOException e) {}
            try { if (fis != null) fis.close(); } catch (IOException e) {}
        }
        return list;
    }
}
