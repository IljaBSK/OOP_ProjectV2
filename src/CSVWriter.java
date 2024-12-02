import java.io.FileWriter;
import java.io.IOException;

public class CSVWriter {
    /**
     * Writes a single line of data to a CSV file.
     * Appends data to the file if it already exists.
     *
     * @param data     An array of strings, where each element represents a column in the CSV.
     * @param filePath The path to the CSV file where the data will be written.
     * @throws IOException If an error occurs while writing to the file.
     */
    public void writeToCSV(String[] data, String filePath) throws IOException {
        FileWriter writer = new FileWriter(filePath, true);
        String line = String.join(",", data);
        writer.write(line + "\n");
        writer.close();
    }
}
