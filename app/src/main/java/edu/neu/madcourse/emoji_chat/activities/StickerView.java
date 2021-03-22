package edu.neu.madcourse.emoji_chat.activities;


public class StickerView {

    private String img_src;
    private String sender;

    public StickerView(String body, String sender) {
        img_src = body;
        this.sender = sender;
    }

    public String getImgSrc() {
        return img_src;
    }

    public String getSender() {
        return sender;
    }

}
