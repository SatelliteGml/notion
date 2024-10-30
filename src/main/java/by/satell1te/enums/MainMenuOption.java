package by.satell1te.enums;

public enum MainMenuOption {
    CREATE_USER("Создать пользователя"),
    LOGIN("Войти"),
    EXIT("Выйти"),
    SHOW_USERS("Показать список всех пользователей"),
    DELETE_USER("Удалить пользователя"),
    UNKNOWN("Неизвестный пункт меню"); // для обработки неопределенных случаев

    private final String description;

    MainMenuOption(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public static void displayMainMenu() {
        for (MainMenuOption option : values()) {
            System.out.println((option.ordinal() + 1) + ". " + option.getDescription());
        }
    }
}
