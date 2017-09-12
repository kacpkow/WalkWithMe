package com.example.kacper.walkwithme.MainActivity.Chat;

/**
 * @author Kacper Kowalik
 * @version 1.0
 */

public class ChatPresenterImpl implements ChatPresenter {
    private ChatView chatView;
    public ChatPresenterImpl(ChatView chatView){
        this.chatView = chatView;
    }

    @Override
    public void onDestroy() {
        this.onDestroy();
    }

    @Override
    public void refresh_after_map() {
        chatView.refresh_after_map();
    }
}
