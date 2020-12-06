package ru.atom.chat.modelDto;

public class LoginDto {
    private final String name;
    private final String password;

    public LoginDto(String name, String password) {
        this.name = name;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String toString() {
        return "\nLoginDto{" +
                "name='" + name + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
