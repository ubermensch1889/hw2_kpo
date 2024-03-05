package dao;

import java.io.IOException;
import java.util.List;

public interface Dao<T> {
    void saveChanges() throws IOException;

    List<T> getAll() throws IOException;

    void save(T t) throws IOException;

    void delete(T t) throws IOException;
}