package springbook.learningtest.spring.factorybean;

public class Message {
    String text;
    
    private Message(final String text) {
        this.text = text;
    }
    
    public String getText() {
        return this.text;
    }
    
    public static Message newMessage(final String text) {
        return new Message(text);
    }
}
