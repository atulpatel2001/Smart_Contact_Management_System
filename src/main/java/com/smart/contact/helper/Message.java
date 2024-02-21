package com.smart.contact.helper;

public class Message {
    
    private String content;
    private String typeOfMessage;
    public Message(String content, String typeOfMessage) {
        this.content = content;
        this.typeOfMessage = typeOfMessage;
    }
    public Message() {
    }
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public String getTypeOfMessage() {
        return typeOfMessage;
    }
    public void setTypeOfMessage(String typeOfMessage) {
        this.typeOfMessage = typeOfMessage;
    }

    
}
