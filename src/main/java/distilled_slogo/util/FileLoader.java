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
public abstract class FileLoader {
    /**
     * Load a file and return the string representation
     * 
     * @param path The path to the file
     * @return The string representation of the file
     * @throws IOException If an error occurred reading the file
     */
    public String loadFile(String path) throws IOException {
        return loadFile(path, this);
    }

    /**
     * Load a file and return the string representation
     * 
     * @param path The path to the file
     * @param relativeTo An object whose class is used as a reference point to
     *                   locate a file. May or may not be used, depending on
     *                   implementation and the path.
     * @return The string representation of the file
     * @throws IOException If an error occurred reading the file
     */
    public abstract String loadFile(String path, Object relativeTo) throws IOException;

    /**
     * Read out a string from a BufferedReader
     * 
     * @param reader The reader to extract strings from
     * @return The String read out
     * @throws IOException If an error occurred when reading the BufferedReader
     */
    public String getStringFromBufferedReader(BufferedReader reader) throws IOException{
        StringBuilder stringBuilder = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            stringBuilder.append(line);
            stringBuilder.append("\n");
        }
        return stringBuilder.toString();
    }
}
