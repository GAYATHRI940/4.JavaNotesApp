import java.io.*;
import java.nio.file.*;
import java.util.*;

public class NotesApp {
    private static final String FILENAME = "notes.txt";

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.println("\n--- Notes App ---");
            System.out.println("1) Add note");
            System.out.println("2) View notes");
            System.out.println("3) Delete note (by number)");
            System.out.println("4) Clear all notes");
            System.out.println("5) Exit");
            System.out.print("Choice: ");
            String choice = sc.nextLine().trim();

            switch (choice) {
                case "1": addNote(sc); break;
                case "2": viewNotes(); break;
                case "3": deleteNote(sc); break;
                case "4": clearNotes(sc); break;
                case "5": System.out.println("Goodbye!"); sc.close(); return;
                default: System.out.println("Invalid choice. Try again.");
            }
        }
    }

    private static void addNote(Scanner sc) {
        System.out.print("Enter note: ");
        String note = sc.nextLine();
        try (FileWriter fw = new FileWriter(FILENAME, true);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter out = new PrintWriter(bw)) {
            out.println(note);
            System.out.println("Note saved.");
        } catch (IOException e) {
            System.out.println("Error writing file: " + e.getMessage());
        }
    }

    private static void viewNotes() {
        Path path = Paths.get(FILENAME);
        if (!Files.exists(path)) {
            System.out.println("No notes yet.");
            return;
        }
        try (BufferedReader br = Files.newBufferedReader(path)) {
            String line;
            int idx = 1;
            while ((line = br.readLine()) != null) {
                System.out.printf("%d) %s%n", idx++, line);
            }
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
    }

    private static void deleteNote(Scanner sc) {
        Path path = Paths.get(FILENAME);
        if (!Files.exists(path)) { System.out.println("No notes to delete."); return; }
        try {
            List<String> lines = Files.readAllLines(path);
            if (lines.isEmpty()) { System.out.println("No notes to delete."); return; }

            for (int i = 0; i < lines.size(); i++) {
                System.out.printf("%d) %s%n", i+1, lines.get(i));
            }
            System.out.print("Enter note number to delete: ");
            String input = sc.nextLine().trim();
            int num = Integer.parseInt(input);
            if (num < 1 || num > lines.size()) {
                System.out.println("Invalid number.");
                return;
            }
            lines.remove(num - 1);
            Files.write(path, lines); // overwrite with remaining lines
            System.out.println("Note deleted.");
        } catch (NumberFormatException e) {
            System.out.println("Please enter a valid number.");
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void clearNotes(Scanner sc) {
        System.out.print("Are you sure? (y/N): ");
        String ans = sc.nextLine().trim().toLowerCase();
        if (!ans.equals("y")) { System.out.println("Canceled."); return; }
        try {
            Files.deleteIfExists(Paths.get(FILENAME));
            System.out.println("All notes cleared.");
        } catch (IOException e) {
            System.out.println("Could not clear notes: " + e.getMessage());
        }
    }
}