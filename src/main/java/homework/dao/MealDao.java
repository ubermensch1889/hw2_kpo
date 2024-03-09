package homework.dao;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import homework.model.Meal;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

// Класс для управления информацией о блюдах
public class MealDao implements Dao<Meal>{
    private final Gson gson;
    private final String path;
    private List<Meal> Meal;

    @Override
    public void saveChanges() throws IOException {
        Writer writer = Files.newBufferedWriter(Paths.get(path));

        gson.toJson(Meal, writer);

        writer.close();
    }

    public MealDao(String path) {
        this.path = path;
        GsonBuilder builder = new GsonBuilder();
        gson = builder.create();
    }

    @Override
    public List<Meal> getAll() throws IOException {
        Reader reader = Files.newBufferedReader(Paths.get(path));

        try {
            Meal = new ArrayList<>(Arrays.asList(gson.fromJson(reader, Meal[].class)));
        } catch (NullPointerException e) {
            Meal = new ArrayList<>();
        }

        reader.close();

        return Meal;
    }

    @Override
    public void save(Meal user) throws IOException {
        Meal = getAll();

        for (Meal value : Meal) {
            if (value.getName().equals(user.getName())) {
                throw new IllegalArgumentException();
            }
        }

        Meal.add(user);

        saveChanges();
    }

    @Override
    public void delete(Meal user) throws IOException {
        Meal.remove(user);

        saveChanges();
    }
}
