package source.controllers.entity;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

public class Post {
    public Post() {
    }

    private Account account;

    @NotEmpty(message = "Пост не должен быть пустым")
    @Size(max = 200, message = "Максимальная длина - поста 200 символов")
    private String text;

    private String date;
    private String userName;

    private int id;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
