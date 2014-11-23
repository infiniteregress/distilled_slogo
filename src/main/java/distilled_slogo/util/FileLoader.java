package distilled_slogo.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class FileLoader {
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
     * Load a file from the classpath
     * 
     * @param path The absolute or relative path to the file in the classpath
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
