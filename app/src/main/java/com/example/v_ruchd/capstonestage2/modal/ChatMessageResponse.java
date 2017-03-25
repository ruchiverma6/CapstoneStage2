package com.example.v_ruchd.capstonestage2.modal;

import java.util.List;

/**
 * Created by ruchi on 25/3/17.
 */

public class ChatMessageResponse extends Data {

    public ChatMessage[] messageArray;

    public ChatMessage[] getChatMessages ()
    {
        return messageArray;
    }

    public void setChatMessages (ChatMessage[] messageArray)
    {
        this.messageArray = messageArray;
    }
}
