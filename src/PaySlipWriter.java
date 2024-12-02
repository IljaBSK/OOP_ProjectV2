import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class PaySlipWriter {
    private String filePath;

    public PaySlipWriter(String filePath) {
        this.filePath = filePath;
    }

    public void writePayslip(String[] payslipData, LocalDate today) throws IOException {
        boolean fileExists = Files.exists(Paths.get(filePath));

        DateTimeFormatter monthYearFormatter = DateTimeFormatter.ofPattern("MM/yyyy");

        try (FileWriter writer = new FileWriter(filePath, true)) { // Append mode

            if (!fileExists) {
                writer.write("id,name,date,jobTitle,scalePoint,grossSalary,incomeTax,prsi,usc,unionFee,netSalary\n");
            }

            for (int i = 5; i <= 10; i++) {
                payslipData[i] = String.valueOf(Double.parseDouble(payslipData[i]));
            }

            payslipData[2] = today.format(monthYearFormatter);

            String line = String.join(",", payslipData);
            writer.write(line + "\n");
            System.out.println("Payslip written for ID: " + payslipData[0]);
        } catch (IOException e) {
            throw new IOException("Error writing payslip for ID: " + payslipData[0], e);
        }
    }
}