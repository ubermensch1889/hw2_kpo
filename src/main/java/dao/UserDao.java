package dao;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import model.User;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

// Класс для управления информацией о пользователях
public class UserDao implements Dao<User>{
    private final Gson gson;
    private final String path;
    private List<User> users;

    @Override
    public void saveChanges() throws IOException {
        Writer writer = Files.newBufferedWriter(Paths.get(path));

        gson.toJson(users, writer);

        writer.close();
    }

    public UserDao(String path) {
        this.path = path;
        GsonBuilder builder = new GsonBuilder();
        gson = builder.create();
    }

    @Override
    public List<User> getAll() throws IOException {
        Reader reader = Files.newBufferedReader(Paths.get(path));

        try {
            users = new ArrayList<>(Arrays.asList(gson.fromJson(reader, User[].class)));
        } catch (NullPointerException e) {
            users = new ArrayList<>();
        }

        reader.close();

        return users;
    }

    @Override
    public void save(User user) throws IOException {
        users = getAll();

        for (User value : users) {
            if (value.getName().equals(user.getName())) {
                throw new IllegalArgumentException();
            }
        }

        users.add(user);

        saveChanges();
    }

    @Override
    public void delete(User user) throws IOException {
        users.remove(user);

        saveChanges();
    }
}
