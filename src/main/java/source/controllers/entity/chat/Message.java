package source.controllers.entity.chat;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.sql.Timestamp;

public class Message {
    @JsonProperty("accId")
    private int accId;
    @JsonProperty("chatId")
    private int chatId;
    @JsonProperty("date")
    private Timestamp date;
    @JsonProperty("text")
    private String text;
    @JsonProperty("name")
    private String name;
    @JsonProperty("id")
    private int id;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
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

    public int getChatId() {
        return chatId;
    }

    public void setChatId(int chatId) {
        this.chatId = chatId;
    }

    public String getDate() {
        return date.toString();
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

    public int getAccId() {
        return accId;
    }

    public void setAccId(int accId) {
        this.accId = accId;
    }
}