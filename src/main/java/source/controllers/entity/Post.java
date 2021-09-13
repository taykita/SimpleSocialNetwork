package source.controllers.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.sql.Timestamp;

public class Post {
    public Post() {
    }

    @JsonIgnore
    private Account account;

    @NotEmpty(message = "Пост не должен быть пустым")
    @Size(max = 200, message = "Максимальная длина - поста 200 символов")
    @JsonProperty("text")
    private String text;

    @JsonProperty("date")
    private Timestamp date;

    @JsonProperty("userName")
    private String userName;

    @JsonProperty("id")
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
        return date.toString();
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
