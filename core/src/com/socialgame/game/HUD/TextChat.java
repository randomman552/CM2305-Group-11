package com.socialgame.game.HUD;

import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextArea;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.socialgame.game.SocialGame;
import com.socialgame.game.networking.Networking;

public class TextChat extends Table {
    private static class TextFieldListener implements TextField.TextFieldListener {
        private TextChat chat;

        public TextFieldListener(TextChat chat) {
            this.chat = chat;
        }

        @Override
        public void keyTyped(TextField textField, char c) {
            if (c == '\r') {
                chat.sendMessage(textField.getText());
                textField.setText("");
                chat.game.getUIStage().setKeyboardFocus(null);
            }
        }
    }

    private final TextArea textArea;
    public final TextField textInput;
    protected final SocialGame game;

    public TextChat(final SocialGame game) {
        this.game = game;
        textArea = new TextArea("", game.gameSkin);
        textArea.setTouchable(Touchable.disabled);

        textInput = new TextField("", game.gameSkin);
        textInput.setTextFieldListener(new TextFieldListener(this));

        setSize(300, 240);
        add(textArea).width(300).height(200);
        row();
        add(textInput).width(300).height(40);
    }

    public void sendMessage(String message) {
        if (message.length() == 0) return;
        String sender = game.getMainPlayer().getName();
        game.getClient().sendTCP(Networking.textMessage(sender, message));
    }

    public void receiveMessage(String sender, String message) {
        textArea.appendText(sender + ": " + message + "\n");
    }
}
