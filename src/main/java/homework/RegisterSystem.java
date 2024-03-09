package homework;

import homework.dao.Dao;
import homework.dao.UserDao;
import homework.model.User;
import homework.model.UserRole;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Objects;
import java.util.Base64;

// Система регистрации пользователей, по сути оболочка над UserDao, позволяющая шифровать и дешифровать пароли
public class RegisterSystem {
    private Cipher encryptCipher;
    private Cipher decryptCipher;
    private Dao<User> userDao;

    public void Init(String pathToKey, String pathToUserJson) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IOException, InvalidAlgorithmParameterException {
        FileReader reader = new FileReader(pathToKey);
        userDao = new UserDao(pathToUserJson);

        // Читаем ключ посимвольно
        int c;
        StringBuilder key = new StringBuilder();
        while((c = reader.read())!=-1){
            key.append((char) c);
        }

        byte[] keyBytes = Base64.getDecoder().decode(key.toString());

        encryptCipher = Cipher.getInstance("AES");
        SecretKeySpec secretKeySpec = new SecretKeySpec(keyBytes, "AES");
        encryptCipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);

        decryptCipher = Cipher.getInstance("AES");
        decryptCipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
    }

    public User findUser(String name, String password) throws IllegalBlockSizeException, BadPaddingException, IOException {
        // Проверка, зарегистрирован ли пользователь с таким логином и паролем в системе
        List<User> users = userDao.getAll();
        for (User user : users) {
            byte[] decryptedPasswordBytes = decryptCipher.doFinal(Base64.getDecoder().decode(user.getEncryptedPassword()));
            String decryptedPassword = new String(decryptedPasswordBytes);
            if (Objects.equals(user.getName(), name) &&
                    Objects.equals(decryptedPassword, password)) {
                return user;
            }
        }

        return null;
    }

    public void addUser(String name, String password, UserRole role) throws IllegalBlockSizeException, BadPaddingException, IOException {
        // Регистрируем пользователя в системе
        byte[] encryptedPasswordBytes = encryptCipher.doFinal(password.getBytes(StandardCharsets.UTF_8));
        userDao.save(new User(name, Base64.getEncoder().encodeToString(encryptedPasswordBytes), role));
    }
}
