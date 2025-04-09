
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class CustomFileHandler {
    private final String fileUrl;
    private static final CustomLogger logger = new CustomLogger("logs");

    public CustomFileHandler(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public void writeToFile(String value) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileUrl))) {
            writer.write(value);
            logger.log("INFO", "Writen to a file: " + value);
            writer.newLine();
        } catch (IOException e) {

        }
    }

    public String readFromFile() {
        String line;
        try (BufferedReader reader = new BufferedReader(new FileReader(fileUrl))) {
            line = reader.readLine();
            logger.log("INFO", "Read From a file: " + line);
            return line;
        } catch (IOException e) {
            return null;
        }
    }
}
