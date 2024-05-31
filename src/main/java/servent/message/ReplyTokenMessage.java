package servent.message;

import app.ServentInfo;
import app.mutex.Token;

public class ReplyTokenMessage extends BasicMessage{
    private static final long serialVersionUID = 2084490973699262440L;

    private final Token token;

    public ReplyTokenMessage(ServentInfo sender, ServentInfo receiver, Token token) {
        super(MessageType.REPLY_TOKEN, sender, receiver);
        this.token = token;
    }

    public Token getToken() {
        return token;
    }
}
