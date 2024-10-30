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
    private final Map<User, List<Note>> map = new HashMap<>();
    private User currentUser;

    public NoteService() {
    }

    public void setCurrentUser(User user) {
        this.currentUser = user;
        loadNotesFromDB();
    }

    public void clearUserNotes() {
        if (currentUser != null) {
            map.remove(currentUser);
            currentUser = null;
        }
    }


    public void createNoteCommand() {
        String name = promptInput("Введите название заметки: ");
        String content = promptInput("Введите текст: ");
        Note note = new Note(name, content);
        addNoteToUser(note);
        saveNoteToDB(note);
    }

    private void addNoteToUser(Note note) {
        map.computeIfAbsent(currentUser, k -> new ArrayList<>()).add(note);
    }

    public void showNamesOfNote() {
        List<Note> userNotes = map.get(currentUser);
        if (userNotes == null || userNotes.isEmpty()) {
            System.out.println("Нету заметок");
            return;
        }

        System.out.println("Заметки пользователя: ");
        for (int index = 0; index < userNotes.size(); index++) {
            System.out.println((index + 1) + ": " + userNotes.get(index).getName());
        }
    }


    public void showNoteContent() {
        if (currentUser == null) {
            System.out.println("Ошибка: пользователь не установлен.");
            return;
        }

        String string = promptInput("Введите номер заметки: ");
        int choice;

        try {
            choice = Integer.parseInt(string);
        } catch (NumberFormatException e) {
            System.out.println("ОШИБКА! Введите число " + e.getMessage());
            return;
        }

        List<Note> userNotes = map.get(currentUser);

        if (userNotes == null || userNotes.isEmpty()) {
            System.out.println("У пользователя нету заметок");
        }

        if (userNotes != null && (choice < 1 || choice > userNotes.size())) {
            System.out.println("Ошибка: номер заметки вне диапазона.");
        }
        assert userNotes != null;
        Note selectedNote = userNotes.get(choice - 1);
        System.out.println("Автор заметки: " + currentUser.getUserName());
        System.out.println("Содержимое заметки:\n" + selectedNote.getContent());
    }

//    public void loadUserNotes(User user) {
//        List<Note> list = map.get(currentUser);
//    }

    //    private void loadNotesFromDB() {
//        if (currentUser == null) {
//            System.out.println("Ошибка: текущий пользователь не установлен.");
//            return;
//        }
//
//        map.put(currentUser, new ArrayList<>());
//        try (BufferedReader reader = new BufferedReader(new FileReader(getNotePath().toFile()))) {
//            String line;
//            while ((line = reader.readLine()) != null) {
//                String[] parts = line.split("\\|", 3);
//                if (parts.length < 3) {
//                    System.out.println("Некорректная строка в файле заметок" + line);
//                    continue;
//                }
//
//                String userName = parts[0].trim();
//                String name = parts[1].trim();
//                String content = parts[2].trim();
//
//                if (currentUser.getUserName().equals(userName)) {
//                    Note note = new Note(name, content);
//                    addNoteToUser(note);
//                }
//            }
//        } catch (IOException e) {
//            System.out.println("Не удалось загрузить заметки из файла " + e.getMessage());
//        }
//    }
    private void loadNotesFromDB() {
        try (BufferedReader reader = new BufferedReader(new FileReader(getNotePath().toFile()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|", 3);
                if (parts.length < 3) continue;

                String userName = parts[0];
                String noteName = parts[1];
                String noteContent = parts[2];

                User user = new User("", "", userName);
                Note note = new Note(noteName, noteContent);
                map.computeIfAbsent(user, k -> new ArrayList<>()).add(note);
            }
        } catch (IOException e) {
            System.out.println("Ошибка при загрузке заметок: " + e.getMessage());
        }
    }

//    private void saveNoteToDB(Note note) {
//        if (currentUser == null) {
//            System.out.println("Ошибка: пользователь не установлен.");
//            return;
//        }
//        try (BufferedWriter writer = new BufferedWriter(new FileWriter(getNotePath().toFile(), true))) {
//            writer.write(currentUser.getUserName() + "|" + note.getName() + "|" + note.getContent());
//            writer.newLine();
//        } catch (IOException e) {
//            System.out.println("Не удалось сохранить заметку " + note.getName() + " в файл " + e.getMessage());
//        }
//    }

    private void saveNoteToDB(Note note) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(getNotePath().toFile(), true))) {
            for (Map.Entry<User, List<Note>> entry : map.entrySet()) {
                String userName = entry.getKey().getUserName();
                for (Note value : entry.getValue()) {
                    writer.write(userName + "|" + note.getName() + "|" + note.getContent());
                    writer.newLine();
                }
            }
            System.out.println("Заметки сохранены в базу");

        } catch (IOException e) {
            System.out.println("Ошибка при сохранении заметок: " + e.getMessage());
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

    public Map<User, List<Note>> getMap() {
        return map;
    }

}
