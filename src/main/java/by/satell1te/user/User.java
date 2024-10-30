package by.satell1te.user;

public class User {
    private final String login;
    private final String password;
    private final String userName;

    public User(String login, String password, String userName) {
        this.password = password;
        this.userName = userName;
        this.login = login;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public String getUserName() {
        return userName;
    }

    @Override
    public String toString() {
        return """
                Username: %s
                """.formatted(getUserName());
    }
}
