import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class PayClaimsReader {
    private String filePath;

    public PayClaimsReader(String filePath) {
        this.filePath = filePath;
    }

    public boolean hasPayClaim(String employeeId) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            reader.readLine(); // Skip the header row

            String line;
            while ((line = reader.readLine()) != null) {
                String[] fields = line.split(",");
                if (fields.length >= 1) {
                    String id = fields[0].trim();
                    if (id.equals(employeeId)) {
                        return true; // Pay claim found for the employee
                    }
                }
            }
        }

        return false; // No pay claim found
    }
}