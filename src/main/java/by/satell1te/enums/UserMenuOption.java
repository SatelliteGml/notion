package by.satell1te.enums;

public enum UserMenuOption {
    CREATE_NOTE(1, "Создать заметку"),
    DELETE_NOTE(2, "Удалить заметку"),
    SHOW_ALL_NOTES(3, "Показать все заметки"),
    SHOW_NOTE_CONTENT(4,"Показать содержимое заметки"),
    EDIT_NOTE(5, "Редактирование заметки"),
    RETURN_TO_MAIN_MENU(9, "Возврат в главное меню"),
    EXIT(0, "Закрыть приложение");

    private final int commandNumber;
    private final String ddescription;

    UserMenuOption(int commandNumber, String ddescription) {
        this.commandNumber = commandNumber;
        this.ddescription = ddescription;
    }

    public static void displayMenu() {
        System.out.println("Меню пользователя: ");
        for (UserMenuOption option : values()) {
            System.out.println(option.commandNumber + ". " + option.getDdescription());
        }
    }

    public static UserMenuOption fromCommandNumber(int number) {
        for (UserMenuOption option : values()) {
            if (option.getCommandNumber() == number) {
                return option;
            }
        }
        return null;

    }

    public int getCommandNumber() {
        return commandNumber;
    }

    public String getDdescription() {
        return ddescription;
    }
}
