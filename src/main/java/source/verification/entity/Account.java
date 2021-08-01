package source.verification.entity;

import java.util.HashSet;
import java.util.Set;

public class Account extends Object{
    public Account() {
    }

    public Account(String email, String pass, String userName) {
        this.email = email;
        this.pass = pass;
        this.userName = userName;
    }

    public Account(String email, String pass, String userName, int id) {
        this.email = email;
        this.pass = pass;
        this.userName = userName;
        this.id = id;
    }

    private String email;
    private String pass;
    private String userName;
    private String avatarImg;
    private int id;
    private Set<Account> accountSet = new HashSet<>(0);

    public String getEmail() {
        return email;
    }

    public String getPass() {
        return pass;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getAvatarImg() {
        return avatarImg;
    }

    public void setAvatarImg(String avatarImg) {
        this.avatarImg = avatarImg;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Set<Account> getAccountSet() {
        return accountSet;
    }

    public void setAccountSet(Set<Account> accountSet) {
        this.accountSet = accountSet;
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof Account) {
            Account account = (Account) object;
            return this.id == account.id;
        }
        return false;
    }
}
