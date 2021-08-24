package source.controllers.entity;

public class Message {

    private String text;
    private int id;

    public Message() {
    }

    public Message(String name) {
        this.text = name;
    }

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
}