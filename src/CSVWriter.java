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
        // Create a FileWriter instance to write data to the specified file
        // The second argument (true) enables appending mode, ensuring new data is added at the end of the file
        FileWriter writer = new FileWriter(filePath, true);

        // Combine the elements of the data array into a single CSV-formatted line
        // Each element is separated by a comma
        String line = String.join(",", data);

        // Write the line to the file, followed by a newline character
        // This ensures that each record appears on a new line
        writer.write(line + "\n");

        // closes the FileWriter to free system resources and ensure data is properly saved
        writer.close();
    }
}
