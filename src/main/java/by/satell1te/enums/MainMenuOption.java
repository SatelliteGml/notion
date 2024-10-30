package by.satell1te.enums;

public enum MainMenuOption {
    CREATE_ACCOUNT(1, "Создать аккаунт"),
    LOGIN(2, "Войти в аккаунт"),
    LOGOUT(3, "Выйти из аккаунта"),
    SHOW_ALL_USERS(4, "Показать список всех пользователей"),
    DELETE_USER(5, "Удалить пользователя"),
    EXIT(0, "Выйти из приложения");

    private final int commandNumber;
    private final String description;

    MainMenuOption(int commandNumber, String description) {
        this.commandNumber = commandNumber;
        this.description = description;
    }

    public static void displayMenu() {
        System.out.println("Главное меню:");
        for (MainMenuOption option : values()) {
            System.out.println(option.commandNumber + ". " + option.description);
        }
    }

    public static MainMenuOption fromCommandNumber(int number) {
        for (MainMenuOption option : values()) {
            if (option.getCommandNumber() == number) {
                return option;
            }
        }
        return null;
    }

    public int getCommandNumber() {
        return commandNumber;
    }

    public String getDescription() {
        return description;
    }


}
