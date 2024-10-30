package by.satell1te.programmanagement;

import by.satell1te.enums.MainMenuOption;
import by.satell1te.enums.UserMenuOption;

import java.util.Scanner;

public class MenuController {
    private final UserService userService = new UserService();
    private final NoteService noteService = new NoteService();
    private boolean isRunning = true;
    private boolean isLoggedIn = false;
    private static final Scanner SCANNER = new Scanner(System.in);

    public void start() {
        while (isRunning) {
            if (!isLoggedIn) {
                MainMenuOption.displayMenu();
                System.out.print("Введите команду: ");
                try {
                    int choice = Integer.parseInt(SCANNER.nextLine().trim());
                    MainMenuOption mainOption = MainMenuOption.fromCommandNumber(choice);
                    handleMainMenuOption(mainOption);
                } catch (NumberFormatException e) {
                    System.out.println("Ошибка: введено некорректное число. Пожалуйста, попробуйте еще раз.");
                }
            } else {
                UserMenuOption.displayMenu();
                System.out.print("Введите команду: ");
                try {
                    int choice = Integer.parseInt(SCANNER.nextLine().trim());
                    UserMenuOption userOption = UserMenuOption.fromCommandNumber(choice);
                    handleUserMenuOption(userOption);
                } catch (NumberFormatException e) {
                    System.out.println("Ошибка: введено некорректное число. Пожалуйста, попробуйте еще раз.");
                }
            }
        }
        System.out.println("Приложение завершено.");
    }


    private void handleMainMenuOption(MainMenuOption option) {
        if (option == null) {
            System.out.println("Неверная команда. Попробуйте снова.");
            return;
        }

        switch (option) {
            case CREATE_ACCOUNT -> {
                userService.createTempUserCommand();
                System.out.println("Создание аккаунта прошло успешно...");
            }
            case LOGIN -> {
                if (userService.loginCommand(noteService)) {
                    isLoggedIn = true; // Переход в меню пользователя
                }
            }
            case LOGOUT -> {
                userService.logoutCommand();
                isLoggedIn = false;
            }
            case SHOW_ALL_USERS -> userService.showAllUsersCommand();
            case DELETE_USER -> {
                userService.deleteUserByLoginCommand();
                System.out.println("Пользователь удален.");
            }
            case EXIT -> isRunning = false;
        }
    }

    private void handleUserMenuOption(UserMenuOption option) {
        if (option == null) {
            System.out.println("Неверная команда. Попробуйте снова.");
            return;
        }

        switch (option) {
            case CREATE_NOTE -> noteService.createNoteCommand();
            case SHOW_ALL_NOTES -> noteService.showNamesOfNote();
            case SHOW_NOTE_CONTENT -> noteService.showNoteContent();
            case DELETE_NOTE -> System.out.println(""); //deleteNoteByIndex();
            case EDIT_NOTE -> System.out.println("");//editNote();
            case RETURN_TO_MAIN_MENU -> isLoggedIn = false; // Переход в главное меню
            case EXIT -> isRunning = false;
        }
    }
}
