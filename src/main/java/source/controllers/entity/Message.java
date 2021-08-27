package source.controllers.entity;

public class Message {

    private String text;
    private int chatId;
    private String name;

//TODO Подумать над @JsonProperty
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }


    public int getChatId() {
        return chatId;
    }

    public void setChatId(int chatId) {
        this.chatId = chatId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}