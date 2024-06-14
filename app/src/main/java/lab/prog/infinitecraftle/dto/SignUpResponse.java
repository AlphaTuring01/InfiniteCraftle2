package lab.prog.infinitecraftle.dto;

/**
 * Class representing the response to a sign up request.
 */
public class SignUpResponse {
    private String error;

    /**
     * Constructor for SignUpResponse.
     */
    public SignUpResponse(){
        error = "";
    }
    /**
     * Getter for the error message.
     * @return The error message.
     */
    public String getError(){
        return error;
    }
    /**
     * Setter for the error message.
     * @param error The error message.
     */
    public void setError(String error){
        this.error = error;
    }
}

