package by.satell1te.programmanagement;

import by.satell1te.note.Note;
import by.satell1te.user.User;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class NoteService {
    private static final String NOTE_PATH = "src/main/resources/notes.txt";
    private static final Scanner SCANNER = new Scanner(System.in);
    private final List<Note> noteList = new ArrayList<>();
    private final Map<User, List<Note>> map = new HashMap<>();
    private final Map<String, List<Note>> notes = new HashMap<>();
    private User currentUser;

    public NoteService() {
        loadNotesFromDB();
    }

    public void setCurrentUser(User user) {
        this.currentUser = user;
    }

    public Note createNoteCommand() {
        String name = promptInput("Введите название заметки: ");
        String content = promptInput("Введите текст: ");
        Note note = new Note(name, content);
        addNoteToUser(note);
        saveNoteToDB(note, currentUser);
        return note;
    }

    private void addNoteToUser(Note note) {
        map.computeIfAbsent(currentUser, k -> new ArrayList<>()).add(note);
        notes.computeIfAbsent(currentUser.getUserName(), k -> new ArrayList<>()).add(note);
    }

    public void showNamesOfNote() {
        List<Note> userNotes = notes.get(currentUser.getUserName());
        int index = 1;
        if (userNotes == null || userNotes.isEmpty()) {
            System.out.println("Нету заметок");
        } else {
            System.out.println("Заметки пользователя: ");
            for (Note note : userNotes) {
                System.out.println(index + ": " + note.getName());
                index++;
            }
        }
    }

    public void showNoteContent() {
        String string = promptInput("Введите номер заметки: ");
        int choice;

        try {
            choice = Integer.parseInt(string);
        } catch (NumberFormatException e) {
            System.out.println("ОШИБКА! Введите число " + e.getMessage());
            return;
        }

        List<Note> userNotes = notes.get(currentUser.getUserName());

        if (userNotes == null || userNotes.isEmpty()) {
            System.out.println("У пользователя нету заметок");
        }

        if (choice < 1 || choice > userNotes.size()) {
            System.out.println("Ошибка: номер заметки вне диапазона.");
        }
        Note selectedNote = userNotes.get(choice - 1);
        System.out.println("Содержимое заметки:\n" + selectedNote.getContent());
    }

    public void loadUserNotes(User user) {
        List<Note> list = notes.get(user.getUserName());

    }

    private void loadNotesFromDB() {
        List<Note> tmp = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(getNotePath().toFile()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|", 3);
                if (parts.length < 3) {
                    System.out.println("Некорректная строка в файле заметок" + line);
                    continue;
                }

                String user = parts[0].trim();
                String name = parts[1].trim();
                String content = parts[2].trim();
                Note note = new Note(name, content);
                tmp.add(note);
                notes.computeIfAbsent(user, k -> new ArrayList<>()).add(note);
            }
        } catch (IOException e) {
            System.out.println("Не удалось загрузить заметки из файла " + e.getMessage());
        }
    }

    private void saveNoteToDB(Note note, User user) {
        if (user == null) {
            System.out.println("Ошибка: пользователь не установлен.");
            return;
        }
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(getNotePath().toFile(), true))) {
            writer.write(user.getUserName() + "|" + note.getName() + "|" + note.getContent());
            writer.newLine();
            notes.computeIfAbsent(user.getUserName(), k -> new ArrayList<>()).add(note);
        } catch (IOException e) {
            System.out.println("Не удалось сохранить заметку " + note.getName() + " в файл " + e.getMessage());
        }
    }

    private String promptInput(String message) {
        System.out.print(message);
        String input = SCANNER.nextLine().trim();
        while (input.isEmpty()) {
            System.out.print("Поле не должно быть пустым. Повторите: ");
            input = SCANNER.nextLine().trim();
        }
        return input;
    }

    private Path getNotePath() {
        return Paths.get(NOTE_PATH);
    }

    public List<Note> getNoteList() {
        return noteList;
    }


    public Map<User, List<Note>> getMap() {
        return map;
    }

}
