import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class PaySlipWriter {
    private String filePath;

    /**
     * Constructor to initialize the path to the PaySlips.csv file.
     *
     * @param filePath Path to the CSV file where payslips will be written.
     */
    public PaySlipWriter(String filePath) {
        this.filePath = filePath;
    }

    /**
     * Writes a single payslip record to the CSV file.
     * If the file doesn't exist, it creates the file and writes the header.
     *
     * @param payslipData An array of strings representing the payslip details.
     * @throws IOException If an error occurs while writing to the file.
     */
    public void writePayslip(String[] payslipData) throws IOException {
        boolean fileExists = Files.exists(Paths.get(filePath));

        try (FileWriter writer = new FileWriter(filePath, true)) { // Append mode
            // Add header if the file is newly created
            if (!fileExists) {
                writer.write("id,jobTitle,scalePoint,grossSalary,incomeTax,prsi,usc,unionFee,netSalary,date\n");
            }

            // Write the payslip data
            String line = String.join(",", payslipData);
            writer.write(line + "\n");
        }
    }
}