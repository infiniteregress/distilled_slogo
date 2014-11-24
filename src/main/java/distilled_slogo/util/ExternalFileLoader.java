package distilled_slogo.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * Loads a file in the filesystem, either absolutely or relative to the current
 * working directory
 *
 */
public class ExternalFileLoader extends FileLoader {
    /**
     * Loads a file from the filesystem
     * 
     * @param path The path to the file; this can either be a relative or absolute path
     * @param relativeTo Not used.
     * @return The string representation of the file
     * @throws IOException If an error occurred reading the file
     */
    @Override
    public String loadFile(String path, Object relativeTo) throws IOException {
        File file = new File(path);
        try (
                BufferedReader configFileReader = new BufferedReader(new FileReader(file));
        ) {
            return getStringFromBufferedReader(configFileReader);
        }
    }
}
