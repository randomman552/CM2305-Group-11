package com.socialgame.game.HUD;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextArea;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.socialgame.game.SocialGame;
import com.socialgame.game.networking.Networking;

public class TextChat extends Table {
    private static class TextInputListener extends InputListener {
        private final TextChat chat;
        private final TextField input;

        public TextInputListener(TextChat chat, TextField input) {
            this.chat = chat;
            this.input = input;
        }

        @Override
        public boolean keyDown(InputEvent event, int keycode) {
            if (keycode == Input.Keys.ENTER) {
                chat.sendMessage(input.getText());
            }
            input.clear();
            chat.game.getMainStage().setKeyboardFocus(chat.game.mainPlayer);
            return true;
        }
    }

    private final TextArea textArea;
    protected final SocialGame game;

    public TextChat(SocialGame game) {
        this.game = game;
        textArea = new TextArea("", game.gameSkin);
        TextField textInput = new TextField("", game.gameSkin);

        textInput.addListener(new TextInputListener(this, textInput));

        setSize(300, 240);
        add(textArea).width(300).height(200);
        row();
        add(textInput).width(300).height(40);
    }

    public void sendMessage(String message) {
        String sender = Integer.toString(game.getClient().getID());
        game.getClient().sendTCP(Networking.textMessage(sender, message));
    }

    public void receiveMessage(String sender, String message) {
        textArea.appendText(sender + ": " + message + "\n");
    }
}
