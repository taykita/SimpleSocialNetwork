package source.controllers.authorization.entity;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.*;

public class Account {
    public Account() {
        setRoles(Collections.singletonList("ROLE_USER"));
    }

    public Account(String email, String pass, String userName) {
        setRoles(Collections.singletonList("ROLE_USER"));
        this.email = email;
        this.pass = pass;
        this.userName = userName;
    }

    public Account(String email, String pass, String userName, int id) {
        setRoles(Collections.singletonList("ROLE_USER"));
        this.email = email;
        this.pass = pass;
        this.userName = userName;
        this.id = id;
    }

    @NotEmpty(message = "Почта не должна быть пустой")
    @Size(min = 3, max = 50, message = "Длина почты от 3 до 50")
    private String email;

    @NotEmpty(message = "Пароль не должен быть пустым")
    @Size(min = 3, max = 50, message = "Длина пароля от 3 до 50")
    private String pass;

    @NotEmpty(message = "Имя не должен быть пустым")
    @Size(min = 3, max = 50, message = "Длина имени от 3 до 50")
    private String userName;

    private String avatarImg;
    private int id;
    private Set<Account> accountSet = new HashSet<>(0);

    private List<String> roles;

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

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }
}
