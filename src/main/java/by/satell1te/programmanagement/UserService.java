package by.satell1te.programmanagement;

import by.satell1te.user.User;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class UserService {
    private static final Scanner SCANNER = new Scanner(System.in);
    private static final String USERS_PATH = "src/main/resources/users.txt";
    private static final String USERS_NAME_PATH = "src/main/resources/userNames.txt";
    private final NoteService noteService = new NoteService();
    private User currentUser = null;
    private final List<User> users = new ArrayList<>();
    private final List<String> userNameList = new ArrayList<>();

    public UserService() {
        loadUsersFromDB();
    }

    public User createTempUserCommand() {
        String login = promptInput("Введите почту: ");
        String password = promptInput("Введите пароль: ");
        String name = promptInput("Введите имя отображаемое на сервере: ");
        User tempUser = new User(login, password, name);
        saveUserToDB(tempUser);
        return tempUser;
    }

    public boolean loginCommand(NoteService noteService) {
        // Если текущий пользователь уже авторизован, нет необходимости в повторной аутентификации
        if (currentUser != null) {
            System.out.println("Вы уже авторизованы как: " + currentUser.getLogin());
            return true; // или false, в зависимости от логики
        }

        currentUser = signIn(); // Попытка аутентификации

        if (currentUser != null) {
            System.out.println("Успешный вход в систему для: " + currentUser.getLogin());
            noteService.setCurrentUser(currentUser);
            return true; // Успешная аутентификация
        } else {
            System.out.println("Ошибка входа: неверный логин или пароль.");
            return false; // Неуспешная аутентификация
        }
    }

    private User signIn() {
        String login = promptInput("Введите логин: ");
        String password = promptInput("Введите пароль: ");

        for (User user : users) {
            // Проверка логина и пароля
            if (login.equals(user.getLogin()) && password.equals(user.getPassword())) {
                return user; // Возвращаем существующего пользователя
            }
        }

        return null; // Если аутентификация не удалась
    }


    public void logoutCommand() {
        if (currentUser == null) {
            System.out.println("Вы не авторизованы. Невозможно выйти из системы.");
        } else {
            noteService.clearUserNotes();
            currentUser = null; // Сбрасываем текущего пользователя
            System.out.println("Вы вышли из системы.");
        }
    }


    public void showAllUsersCommand() {
        System.out.println("Всего пользователей в базе: " + users.size());
        for (String s : getUserNameList()) {
            System.out.println("User: " + s);
        }

    }


    public void deleteUserByLoginCommand() {
        String tempLogin = promptInput("Введите логин пользователя: ");
        for (int index = 0; index < users.size(); index++) {
            User user = users.get(index);
            if (tempLogin.equals(user.getLogin())) {
                users.remove(index);
                System.out.printf("Пользователь с логином - %s был удален!", tempLogin);
                updateUserFiles();
                return;
            }
        }
        System.out.printf("Пользователь с %s логином не найден", tempLogin);
    }

    private void saveUserToDB(User user) {
        users.add(user);
        userNameList.add(user.getUserName());

        try (BufferedWriter bwUser = new BufferedWriter(new FileWriter(getUsersPath().toFile(), true));
             BufferedWriter bwUserName = new BufferedWriter(new FileWriter(getUsersNamePath().toFile(), true))) {
            bwUser.write(user.getLogin() + "|" + user.getPassword() + "|" + user.getUserName());
            bwUser.newLine();
            bwUserName.write(user.getUserName());
            bwUserName.newLine();
        } catch (IOException e) {
            System.out.println("Не удалось записать пользователя " + user.getLogin() + " в файл: " + e.getMessage());
        }
    }

    public void loadUsersFromDB() {
        try (BufferedReader brUser = new BufferedReader(new FileReader(getUsersPath().toFile()));
             BufferedReader brUserName = new BufferedReader(new FileReader(getUsersNamePath().toFile()))) {

            String userLoginAndPassword;
            while ((userLoginAndPassword = brUser.readLine()) != null) {
                String[] parts = userLoginAndPassword.split("\\|");
                if (parts.length == 3) {
                    String login = parts[0].trim();
                    String password = parts[1].trim();
                    String name = parts[2].trim();
                    users.add(new User(login, password, name));
                }
            }

            String userName;
            while ((userName = brUserName.readLine()) != null) {
                userNameList.add(userName);
            }

        } catch (IOException e) {
            System.out.println("Не удалось загрузить пользователей из файла: " + e.getMessage());
        }

    }

    private void updateUserFiles() {
        // Обновляем файл с пользователями
        try (BufferedWriter bfwUser = new BufferedWriter(new FileWriter(getUsersPath().toFile()));
             BufferedWriter bfwUserName = new BufferedWriter(new FileWriter(getUsersNamePath().toFile()))) {

            for (User user : users) {
                bfwUser.write(user.getLogin() + "|" + user.getPassword());
                bfwUser.newLine();
                bfwUserName.write(user.getUserName());
                bfwUserName.newLine();
            }

        } catch (IOException e) {
            System.out.println("Не удалось обновить файлы пользователей: " + e.getMessage());
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

    private Path getUsersPath() {
        return Paths.get(USERS_PATH);
    }

    private Path getUsersNamePath() {
        return Paths.get(USERS_NAME_PATH);
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public List<User> getUsers() {
        return users;
    }

    public List<String> getUserNameList() {
        return userNameList;
    }


}
