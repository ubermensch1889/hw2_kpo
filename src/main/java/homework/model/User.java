package homework.model;

import java.util.Objects;

public class User {
    private final String name;
    private final String encryptedPassword;
    private final UserRole role;

    public User(String name, String encryptedPassword, UserRole userRole) {
        this.name = name;
        this.encryptedPassword = encryptedPassword;
        this.role = userRole;
    }

    public String getName() {
        return name;
    }

    public String getEncryptedPassword() {
        return encryptedPassword;
    }

    public UserRole getRole() {
        return role;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(name, user.name) && Objects.equals(encryptedPassword, user.encryptedPassword) && role == user.role;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, encryptedPassword, role);
    }
}
