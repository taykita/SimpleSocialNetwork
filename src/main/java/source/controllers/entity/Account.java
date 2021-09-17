package source.controllers.entity;

import source.controllers.authorization.validation.ValidEmail;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.*;

public class Account {
    public Account() {
        setRoles(Collections.singletonList("ROLE_USER"));
    }

    @NotEmpty(message = "Почта не должна быть пустой")
    @Size(min = 3, max = 50, message = "Длина почты от 3 до 50")
    @ValidEmail(message = "На эту почту уже зарегистрирован аккаунт")
    private String email;

    @NotEmpty(message = "Пароль не должен быть пустым")
    @Size(min = 3, max = 100, message = "Длина пароля от 3 до 50")
    private String pass;

    @NotEmpty(message = "Имя не должен быть пустым")
    @Size(min = 3, max = 50, message = "Длина имени от 3 до 50")
    private String name;

    private int id;
    private Set<Account> accountSet = new HashSet<>(0);
    private List<Post> posts = new ArrayList<>(0);

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public List<Post> getPosts() {
        return posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }

    @Override
    public String toString() {
        return name;
    }

}
