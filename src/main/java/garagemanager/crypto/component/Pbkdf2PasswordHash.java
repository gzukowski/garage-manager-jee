package garagemanager.crypto.component;

import lombok.SneakyThrows;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.security.spec.KeySpec;
import java.util.Base64;

public class Pbkdf2PasswordHash {

    private static final String ALGORITHM = "PBKDF2WithHmacSHA256";

    private static final int SALT_LENGTH = 16;

    private static final int HASH_LENGTH = 32;

    private static final int ITERATIONS = 10000;

    @SneakyThrows
    public String generate(char[] password) {
        byte[] salt = generateSalt();
        KeySpec spec = new PBEKeySpec(password, salt, ITERATIONS, HASH_LENGTH * 8);
        SecretKeyFactory factory = SecretKeyFactory.getInstance(ALGORITHM);
        byte[] hash = factory.generateSecret(spec).getEncoded();

        // Concatenate salt and hash.
        byte[] saltPlusHash = new byte[SALT_LENGTH + HASH_LENGTH];
        System.arraycopy(salt, 0, saltPlusHash, 0, SALT_LENGTH);
        System.arraycopy(hash, 0, saltPlusHash, SALT_LENGTH, HASH_LENGTH);

        // Encode salt plus hash as Base64.
        return Base64.getEncoder().encodeToString(saltPlusHash);
    }

    @SneakyThrows
    public boolean verify(char[] password, String hashedPassword) {
        byte[] saltPlusHash = Base64.getDecoder().decode(hashedPassword);
        byte[] salt = new byte[SALT_LENGTH];
        byte[] hash = new byte[HASH_LENGTH];

        // Separate salt and hash.
        System.arraycopy(saltPlusHash, 0, salt, 0, SALT_LENGTH);
        System.arraycopy(saltPlusHash, SALT_LENGTH, hash, 0, HASH_LENGTH);

        KeySpec spec = new PBEKeySpec(password, salt, ITERATIONS, HASH_LENGTH * 8);
        SecretKeyFactory factory = SecretKeyFactory.getInstance(ALGORITHM);
        byte[] computedHash = factory.generateSecret(spec).getEncoded();

        // Compare the computed hash with the stored hash.
        return MessageDigest.isEqual(hash, computedHash);
    }

    private byte[] generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[SALT_LENGTH];
        random.nextBytes(salt);
        return salt;
    }

}
