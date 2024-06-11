package lab.prog.infinitecraftle;

import java.util.HashMap;
import java.util.Map;

/**
 * Class to map error codes to error messages.
 */
public class ErrorCodeDictionary {
    /**
     * Static dictionary mapping error codes to error messages.
     */
    private static final Map<Integer, String> errorMessages = new HashMap<>();

    static {
        errorMessages.put(0, "Success");
        errorMessages.put(1, "Registration failed: user already exists");
        errorMessages.put(2, "Authentication failed: incorrect password");
        errorMessages.put(3, "Authentication failed: user does not exist");
        errorMessages.put(4, "Add element failed: element already exists");
        errorMessages.put(5, "Searching failed: element not found");
        errorMessages.put(6, "Database connection failed");
        errorMessages.put(7, "Query failed: element not built");
        errorMessages.put(-1, "Unknown error occurred");
    }

    /**
     * Method to get the error message corresponding to an error code.
     * @param errorCode the error code
     * @return the error message corresponding to the error code
     */
    public static String getErrorMessage(int errorCode) {
        return errorMessages.getOrDefault(errorCode, "Unknown Error");
    }
}
