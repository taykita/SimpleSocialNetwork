package SignLogIn;

public class Account {
    public Account(String email, String pass) {
        this.email = email;
        this.pass = pass;
    }

    private String email;
    private String pass;
    private String userName;

    public String getEmail() {
        return email;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
