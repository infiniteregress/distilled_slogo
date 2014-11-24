package distilled_slogo.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Loads a file, using a path which is either absolute to the classpath or relative
 * to this class's directory
 */
public class InternalFileLoader extends FileLoader {
    /**
     * Load a file relative to a class, e.g. from within a jar
     * 
     * @param path The absolute or relative path to the file relative to a class
     * @param relativeTo The object to reference the file from
     * @return The string representation of the file
     * @throws IOException If an error occurred reading the file
     */
    @Override
    public String loadFile(String path, Object relativeTo) throws IOException {
        InputStream fileStream = relativeTo.getClass().getResourceAsStream(path);
        if (fileStream == null) {
            return "";
        }
        BufferedReader fileReader = new BufferedReader(new InputStreamReader(fileStream));
        return getStringFromBufferedReader(fileReader);
    }
}
