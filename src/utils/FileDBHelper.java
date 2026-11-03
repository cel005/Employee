package utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public final class FileDBHelper {

    private static final Path DATA_DIR = Paths.get(
            System.getProperty("user.home"),
            ".employee-attendance-system"
    );

    private FileDBHelper() {
    }

    public static void save(Object object, String fileName)
            throws IOException {

        Files.createDirectories(DATA_DIR);

        Path file = DATA_DIR.resolve(fileName).normalize();

        if (!file.getParent().equals(DATA_DIR)) {
            throw new IOException("Invalid file name.");
        }

        try (ObjectOutputStream output =
                new ObjectOutputStream(
                        new BufferedOutputStream(
                                Files.newOutputStream(file)))) {

            output.writeObject(object);
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> T load(String fileName, T defaultValue)
            throws IOException, ClassNotFoundException {

        Path file = DATA_DIR.resolve(fileName).normalize();

        if (!file.getParent().equals(DATA_DIR)) {
            throw new IOException("Invalid file name.");
        }

        if (!Files.exists(file)) {
            return defaultValue;
        }

        try (ObjectInputStream input =
                new ObjectInputStream(
                        new BufferedInputStream(
                                Files.newInputStream(file)))) {

            return (T) input.readObject();
        }
    }
}