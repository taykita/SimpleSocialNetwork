package SignLogIn;

public class Account {
    public Account(String email, String pass) {
        this.email = email;
        this.pass = pass;
    }

    private String email;
    private String pass;

    public String getEmail() {
        return email;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }
}
