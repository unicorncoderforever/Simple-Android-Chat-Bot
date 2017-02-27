package Adapter;

/**
 * Created by preethirao on 2/21/17.
 */

public class ChatMessage {
    private boolean isImage, isMine;
    private String content;

    public ChatMessage(String message, boolean mine, boolean image) {
        content = message;
        isMine = mine;
        isImage = image;
    }

    public String getContent() {
        return content;
    }

    public boolean isMine() {
        return isMine;
    }



    public boolean isImage() {
        return isImage;
    }


}
