import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class PaySlipWriter {
    private String filePath;


    /**
     * Constructs a PaySlipWriter with the specified file path.
     *
     * @param filePath The path to the file where payslips will be written.
     */
    public PaySlipWriter(String filePath) {
        this.filePath = filePath;
    }


    /**
     * Writes a payslip record to the PaySlips.csv file. If the file does not exist, a header row is added.
     * The date field in the payslip data is formatted to MM/YYYY before writing.
     *
     * @param payslipData An array containing the payslip details in the following order:
     *                    [id, name, date, jobTitle, scalePoint, grossPay, incomeTax, prsi, usc, unionFee, netPay].
     * @throws IOException If an error occurs while writing to the file.
     */
    public void writePayslip(String[] payslipData) throws IOException {
        boolean fileExists = Files.exists(Paths.get(filePath));

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {

            if (!fileExists) {
                writer.write("id,name,date,jobTitle,scalePoint,grossPay,incomeTax,prsi,usc,unionFee,netPay\n");
            }


            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/yyyy");
            payslipData[2] = LocalDate.parse(payslipData[2]).format(formatter);


            writer.write(String.join(",", payslipData) + "\n");
        }
    }


}
