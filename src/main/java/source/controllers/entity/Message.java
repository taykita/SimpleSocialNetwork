package source.controllers.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Message {
    @JsonIgnore
    private int chatId;
    @JsonProperty("date")
    private String date;
    @JsonProperty("text")
    private String text;
    @JsonProperty("name")
    private String name;
    @JsonProperty("id")
    private int id;

//TODO Подумать над @JsonProperty
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
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}