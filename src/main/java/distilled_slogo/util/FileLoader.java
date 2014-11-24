package distilled_slogo.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * A utility class to simplify loading files
 *
 */
public class FileLoader {
    /**
     * Load a file and return the string representation
     * @param path The path to the file
     * @param isExternal True if the file is relative to the filesystem,
     *                   false if it is relative to a class
     * @param object The class relative which to evaluate the path
     * @return The string representation of the file
     * @throws IOException If an error occurred reading the file
     */
    public static String loadFile(String path, boolean isExternal, Object object) throws IOException {
        if (isExternal) {
            return loadExternalFile(path);
        }
        else {
            return loadInternalFile(path, object);
        }
    }
    /**
     * Loads a file from the filesystem
     * 
     * @param path The path to the file; this can either be a relative or absolute path
     * @return The string representation of the file
     * @throws IOException If an error occurred reading the file
     */
    public static String loadExternalFile(String path) throws IOException {
        File file = new File(path);
        try (
                BufferedReader configFileReader = new BufferedReader(new FileReader(file));
        ) {
            return getStringFromBufferedReader(configFileReader);
        }
    }
    
    /**
     * Load a file relative to a class, e.g. from within a jar
     * 
     * @param path The absolute or relative path to the file relative to a class
     * @param object The object to reference the file from
     * @return The string representation of the file
     * @throws IOException If an error occurred reading the file
     */
    public static String loadInternalFile(String path, Object object) throws IOException {
        InputStream fileStream = object.getClass().getResourceAsStream(path);
        if (fileStream == null) {
            return "";
        }
        BufferedReader fileReader = new BufferedReader(new InputStreamReader(fileStream));
        return getStringFromBufferedReader(fileReader);
    }
    
    /**
     * Read out a string from a BufferedReader
     * 
     * @param reader The reader to extract strings from
     * @return The String read out
     * @throws IOException If an error occurred when reading the BufferedReader
     */
    public static String getStringFromBufferedReader(BufferedReader reader) throws IOException{
        StringBuilder stringBuilder = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            stringBuilder.append(line);
            stringBuilder.append("\n");
        }
        return stringBuilder.toString();
    }
}
