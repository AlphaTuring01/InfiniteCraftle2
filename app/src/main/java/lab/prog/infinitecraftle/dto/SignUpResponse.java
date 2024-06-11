package lab.prog.infinitecraftle.dto;

public class SignUpResponse {
    private String error;
    public SignUpResponse(){
        error = "";
    }
    public String getError(){
        return error;
    }
    public void setError(String error){
        this.error = error;
    }
}

